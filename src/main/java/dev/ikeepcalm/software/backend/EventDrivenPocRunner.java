package dev.ikeepcalm.software.backend;

import dev.ikeepcalm.software.backend.rest.entities.dto.RepairRequestCreateDto;
import dev.ikeepcalm.software.backend.rest.entities.dto.RepairRequestResponseDto;
import dev.ikeepcalm.software.backend.rest.entities.dto.StatusUpdateDto;
import dev.ikeepcalm.software.backend.rest.enums.EquipmentType;
import dev.ikeepcalm.software.backend.rest.enums.RequestStatus;
import dev.ikeepcalm.software.backend.rest.services.RepairRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("poc")
@RequiredArgsConstructor
@Slf4j
public class EventDrivenPocRunner implements CommandLineRunner {

    private final RepairRequestService repairRequestService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting Event-Driven Architecture PoC");
        
        // Create a repair request
        RepairRequestCreateDto createDto = createSampleRequest();
        RepairRequestResponseDto createdRequest = repairRequestService.createRepairRequest(createDto);
        log.info("Created repair request with ID: {}", createdRequest.getId());
        
        // Let's simulate a delay
        Thread.sleep(2000);
        
        // Update the request status to IN_PROGRESS
        StatusUpdateDto inProgressUpdate = new StatusUpdateDto(RequestStatus.IN_PROGRESS, "Starting repair process");
        RepairRequestResponseDto updatedRequest = repairRequestService.updateStatus(createdRequest.getId(), inProgressUpdate);
        log.info("Updated repair request status to IN_PROGRESS");
        
        // Let's simulate another delay
        Thread.sleep(2000);
        
        // Update the request status to RESOLVED
        StatusUpdateDto resolvedUpdate = new StatusUpdateDto(RequestStatus.RESOLVED, "Repair completed successfully");
        repairRequestService.updateStatus(createdRequest.getId(), resolvedUpdate);
        log.info("Updated repair request status to RESOLVED");
        
        log.info("Event-Driven Architecture PoC completed");
    }
    
    private RepairRequestCreateDto createSampleRequest() {
        RepairRequestCreateDto.EquipmentDetailsDto equipmentDetails = new RepairRequestCreateDto.EquipmentDetailsDto(
                "Dell",
                "XPS 15",
                "ABC123456",
                "Screen is cracked and needs replacement"
        );
        
        return RepairRequestCreateDto.builder()
                .description("Laptop screen repair")
                .equipmentType(EquipmentType.LAPTOP)
                .equipmentDetails(equipmentDetails)
                .serviceLevelId(1L)
                .build();
    }
}