package dev.ikeepcalm.software.backend.entities.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data for updating a repair request")
public class RepairRequestUpdateDto {

    @Schema(description = "Updated description of the problem", example = "Updated description of the issue")
    private String description;

    @Schema(description = "Updated equipment details")
    private RepairRequestCreateDto.EquipmentDetailsDto equipmentDetails;

    @Schema(description = "Updated service level ID", example = "3")
    private Long serviceLevelId;
}