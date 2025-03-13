package dev.ikeepcalm.software.backend.entities;

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
@Schema(description = "Paginated response with data and pagination information")
public class PageResponse<T> {

    @Schema(description = "List of items")
    private List<T> data;

    @Schema(description = "Pagination information")
    private PaginationInfo pagination;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PaginationInfo {

        @Schema(description = "Total number of items", example = "100")
        private long total;

        @Schema(description = "Current page number", example = "1")
        private int page;

        @Schema(description = "Number of items per page", example = "20")
        private int size;

        @Schema(description = "Total number of pages", example = "5")
        private int totalPages;

        @Schema(description = "Whether there is a next page", example = "true")
        private boolean hasNext;

        @Schema(description = "Whether there is a previous page", example = "false")
        private boolean hasPrevious;
    }
}