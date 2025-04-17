package dev.ikeepcalm.software.backend.rest.entities.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.ikeepcalm.software.backend.rest.enums.RequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data for updating repair request status")
public class StatusUpdateDto {

    @NotNull(message = "Status is required")
    @Schema(description = "New status for the repair request", example = "IN_PROGRESS")
    private RequestStatus status;

    @Schema(description = "Optional notes about the status change", example = "Starting repair process")
    private String notes;
}