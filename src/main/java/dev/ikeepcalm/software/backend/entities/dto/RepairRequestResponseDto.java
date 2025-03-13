package dev.ikeepcalm.software.backend.entities.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.ikeepcalm.software.backend.enums.EquipmentType;
import dev.ikeepcalm.software.backend.enums.RequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Repair request data with all details")
public class RepairRequestResponseDto {

    @Schema(description = "ID of the repair request", example = "123")
    private Long id;

    @Schema(description = "ID of the customer who created the request", example = "456")
    private Long customerId;

    @Schema(description = "ID of the assigned technician (if any)", example = "789")
    private Long technicianId;

    @Schema(description = "Description of the problem", example = "My laptop won't boot up")
    private String description;

    @Schema(description = "Current status of the repair request")
    private RequestStatus status;

    @Schema(description = "Type of equipment to be repaired")
    private EquipmentType equipmentType;

    @Schema(description = "Detailed information about the equipment")
    private RepairRequestCreateDto.EquipmentDetailsDto equipmentDetails;

    @Schema(description = "ID of the service level", example = "2")
    private Long serviceLevelId;

    @Schema(description = "Estimated cost of the repair", example = "150.0")
    private Double estimatedCost;

    @Schema(description = "When the request was created", example = "2023-05-15T10:30:00Z")
    private ZonedDateTime createdAt;

    @Schema(description = "When the request was last updated", example = "2023-05-15T11:45:00Z")
    private ZonedDateTime updatedAt;
}