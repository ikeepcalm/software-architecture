package dev.ikeepcalm.software.backend.rest.entities.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.ikeepcalm.software.backend.rest.enums.EquipmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "Data for creating a new repair request")
public class RepairRequestCreateDto {

    @NotBlank(message = "Description is required")
    @Schema(description = "Description of the problem", example = "My laptop won't boot up")
    private String description;

    @NotNull(message = "Equipment type is required")
    @Schema(description = "Type of equipment", example = "LAPTOP")
    private EquipmentType equipmentType;

    @Schema(description = "Detailed information about the equipment")
    private EquipmentDetailsDto equipmentDetails;

    @Schema(description = "ID of the service level", example = "2")
    private Long serviceLevelId;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EquipmentDetailsDto {

        @Schema(description = "Brand of the equipment", example = "Dell")
        private String brand;

        @Schema(description = "Model of the equipment", example = "XPS 15")
        private String model;

        @Schema(description = "Serial number of the equipment", example = "ABC123456")
        private String serialNumber;

        @Schema(description = "Detailed description of the problem", example = "It shows a blue screen when I try to turn it on")
        private String problemDetails;
    }
}