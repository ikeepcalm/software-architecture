package dev.ikeepcalm.software.backend.rest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ikeepcalm.software.backend.eventdriven.EventPublisher;
import dev.ikeepcalm.software.backend.eventdriven.events.NotificationEvent;
import dev.ikeepcalm.software.backend.eventdriven.events.RepairRequestStatusChangedEvent;
import dev.ikeepcalm.software.backend.rest.entities.PageResponse;
import dev.ikeepcalm.software.backend.rest.entities.RepairRequest;
import dev.ikeepcalm.software.backend.rest.entities.dto.RepairRequestCreateDto;
import dev.ikeepcalm.software.backend.rest.entities.dto.RepairRequestResponseDto;
import dev.ikeepcalm.software.backend.rest.entities.dto.RepairRequestUpdateDto;
import dev.ikeepcalm.software.backend.rest.entities.dto.StatusUpdateDto;
import dev.ikeepcalm.software.backend.rest.enums.RequestStatus;
import dev.ikeepcalm.software.backend.rest.error.exceptions.ResourceNotFoundException;
import dev.ikeepcalm.software.backend.rest.services.repositories.RepairRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepairRequestService {

    private final RepairRequestRepository repairRequestRepository;
    private final ObjectMapper objectMapper;
    private final EventPublisher eventPublisher;
    private final CustomerService customerService;

    @Transactional(readOnly = true)
    public PageResponse<RepairRequestResponseDto> getRepairRequests(int page, int size, String status, String sortBy, String sortType) {
        Sort sort = createSort(sortBy, sortType);
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);

        Page<RepairRequest> requestsPage;
        if (status != null && !status.isEmpty()) {
            requestsPage = repairRequestRepository.findByStatus(
                    RequestStatus.valueOf(status.toUpperCase()),
                    pageRequest
            );
        } else {
            requestsPage = repairRequestRepository.findAll(pageRequest);
        }

        List<RepairRequestResponseDto> requestDtos = requestsPage.getContent().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());

        PageResponse.PaginationInfo paginationInfo = PageResponse.PaginationInfo.builder()
                .total(requestsPage.getTotalElements())
                .page(page)
                .size(size)
                .totalPages(requestsPage.getTotalPages())
                .hasNext(requestsPage.hasNext())
                .hasPrevious(requestsPage.hasPrevious())
                .build();

        return PageResponse.<RepairRequestResponseDto>builder()
                .data(requestDtos)
                .pagination(paginationInfo)
                .build();
    }

    @Transactional(readOnly = true)
    public RepairRequestResponseDto getRepairRequest(Long requestId) {
        RepairRequest request = findRequestById(requestId);
        return mapToResponseDto(request);
    }

    @Transactional
    public RepairRequestResponseDto createRepairRequest(RepairRequestCreateDto createDto) {
        Long currentUserId = getCurrentUserId();

        RepairRequest request = new RepairRequest();
        request.setCustomerId(currentUserId);
        request.setDescription(createDto.getDescription());
        request.setEquipmentType(createDto.getEquipmentType());

        try {
            String equipmentDetailsJson = objectMapper.writeValueAsString(createDto.getEquipmentDetails());
            request.setEquipmentDetails(equipmentDetailsJson);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing equipment details", e);
        }

        request.setServiceLevelId(createDto.getServiceLevelId());
        request.setStatus(RequestStatus.PENDING);
        request.setEstimatedCost(calculateEstimatedCost(createDto));

        ZonedDateTime now = ZonedDateTime.now();
        request.setCreatedAt(now);
        request.setUpdatedAt(now);

        RepairRequest savedRequest = repairRequestRepository.save(request);

        RepairRequestStatusChangedEvent event = RepairRequestStatusChangedEvent.builder()
                .requestId(savedRequest.getId())
                .newStatus(RequestStatus.PENDING)
                .timestamp(now)
                .build();
        eventPublisher.publishStatusChangeEvent(event);

        sendStatusNotification(savedRequest, null, RequestStatus.PENDING);

        return mapToResponseDto(savedRequest);
    }

    @Transactional
    public RepairRequestResponseDto updateRepairRequest(Long requestId, RepairRequestUpdateDto updateDto) {
        RepairRequest request = findRequestById(requestId);

        checkPermission(request);

        if (updateDto.getDescription() != null) {
            request.setDescription(updateDto.getDescription());
        }

        if (updateDto.getEquipmentDetails() != null) {
            try {
                String equipmentDetailsJson = objectMapper.writeValueAsString(updateDto.getEquipmentDetails());
                request.setEquipmentDetails(equipmentDetailsJson);
            } catch (Exception e) {
                throw new RuntimeException("Error serializing equipment details", e);
            }
        }

        if (updateDto.getServiceLevelId() != null) {
            request.setServiceLevelId(updateDto.getServiceLevelId());
            request.setEstimatedCost(recalculateEstimatedCost(request));
        }

        request.setUpdatedAt(ZonedDateTime.now());

        RepairRequest savedRequest = repairRequestRepository.save(request);

        return mapToResponseDto(savedRequest);
    }

    @Transactional
    public RepairRequestResponseDto updateStatus(Long requestId, StatusUpdateDto statusUpdateDto) {
        RepairRequest request = findRequestById(requestId);

        checkTechnicianPermission();

        RequestStatus oldStatus = request.getStatus();
        RequestStatus newStatus = statusUpdateDto.getStatus();

        request.setStatus(newStatus);

        if (newStatus == RequestStatus.IN_PROGRESS && request.getTechnicianId() == null) {
            request.setTechnicianId(getCurrentUserId());
        }

        ZonedDateTime now = ZonedDateTime.now();
        request.setUpdatedAt(now);

        RepairRequest savedRequest = repairRequestRepository.save(request);

        RepairRequestStatusChangedEvent event = RepairRequestStatusChangedEvent.builder()
                .requestId(savedRequest.getId())
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .notes(statusUpdateDto.getNotes())
                .technicianId(savedRequest.getTechnicianId())
                .timestamp(now)
                .build();

        eventPublisher.publishStatusChangeEvent(event);

        sendStatusNotification(savedRequest, oldStatus, newStatus);

        return mapToResponseDto(savedRequest);
    }

    @Transactional
    public void deleteRepairRequest(Long requestId) {
        RepairRequest request = findRequestById(requestId);

        checkPermission(request);

        repairRequestRepository.delete(request);
    }

    private void sendStatusNotification(RepairRequest request, RequestStatus oldStatus, RequestStatus newStatus) {
        String customerEmail = customerService.getCustomerEmail(request.getCustomerId());

        String subject;
        String content;

        if (oldStatus == null) {
            subject = "Your Repair Request #" + request.getId() + " has been created";
            content = "Your repair request for " + request.getEquipmentType() + " has been created and is pending review.";
        } else {
            subject = "Your Repair Request #" + request.getId() + " status has been updated";
            content = "Your repair request status has been updated from " + oldStatus + " to " + newStatus + ".";

            if (newStatus == RequestStatus.RESOLVED) {
                content += " Your device is ready for pickup.";
            }
        }

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .recipientId(request.getCustomerId())
                .recipientEmail(customerEmail)
                .subject(subject)
                .content(content)
                .type(NotificationEvent.NotificationType.STATUS_UPDATE)
                .timestamp(ZonedDateTime.now())
                .build();

        eventPublisher.publishNotificationEvent(notificationEvent);
    }

    private RepairRequest findRequestById(Long requestId) {
        return repairRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Repair request not found with id: " + requestId));
    }

    private RepairRequestResponseDto mapToResponseDto(RepairRequest request) {
        RepairRequestCreateDto.EquipmentDetailsDto equipmentDetails = null;
        try {
            if (request.getEquipmentDetails() != null) {
                equipmentDetails = objectMapper.readValue(
                        request.getEquipmentDetails(),
                        RepairRequestCreateDto.EquipmentDetailsDto.class
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing equipment details", e);
        }

        return RepairRequestResponseDto.builder()
                .id(request.getId())
                .customerId(request.getCustomerId())
                .technicianId(request.getTechnicianId())
                .description(request.getDescription())
                .status(request.getStatus())
                .equipmentType(request.getEquipmentType())
                .equipmentDetails(equipmentDetails)
                .serviceLevelId(request.getServiceLevelId())
                .estimatedCost(request.getEstimatedCost())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }

    private Sort createSort(String sortBy, String sortType) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortType != null && sortType.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        if (sortBy == null || sortBy.isEmpty()) {
            return Sort.by(direction, "createdAt");
        }

        return Sort.by(direction, sortBy);
    }

    private Long getCurrentUserId() {
        return 1L;
    }

    private void checkPermission(RepairRequest request) {
        Long currentUserId = getCurrentUserId();
        if (!request.getCustomerId().equals(currentUserId)) {
            throw new RuntimeException("You don't have permission to update this request");
        }
    }

    private void checkTechnicianPermission() {
    }

    private Double calculateEstimatedCost(RepairRequestCreateDto createDto) {
        return 150.0;
    }

    private Double recalculateEstimatedCost(RepairRequest request) {
        return 180.0;
    }
}