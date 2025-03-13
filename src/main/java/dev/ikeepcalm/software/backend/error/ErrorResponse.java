package dev.ikeepcalm.software.backend.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Error response with details")
public class ErrorResponse {

    @Schema(description = "Error details")
    private ErrorDetails error;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorDetails {

        @Schema(description = "Error code", example = "VALIDATION_ERROR")
        private String code;

        @Schema(description = "Human-readable error message", example = "The request was invalid")
        private String message;

        @Schema(description = "List of detailed error information (optional)")
        private List<FieldError> details;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FieldError {

        @Schema(description = "Field name with error", example = "email")
        private String field;

        @Schema(description = "Error message for the field", example = "Email is required")
        private String message;
    }
}