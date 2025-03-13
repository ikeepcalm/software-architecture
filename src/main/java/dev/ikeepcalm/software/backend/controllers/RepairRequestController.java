package dev.ikeepcalm.software.backend.controllers;


import dev.ikeepcalm.software.backend.entities.dto.RepairRequestCreateDto;
import dev.ikeepcalm.software.backend.entities.dto.RepairRequestResponseDto;
import dev.ikeepcalm.software.backend.entities.dto.RepairRequestUpdateDto;
import dev.ikeepcalm.software.backend.entities.dto.StatusUpdateDto;
import dev.ikeepcalm.software.backend.error.ErrorResponse;
import dev.ikeepcalm.software.backend.entities.PageResponse;
import dev.ikeepcalm.software.backend.services.RepairRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/requests")
@RequiredArgsConstructor
@Tag(name = "Repair Requests", description = "API for managing repair requests")
@SecurityRequirement(name = "bearerAuth")
public class RepairRequestController {

    private final RepairRequestService repairRequestService;

    @GetMapping
    @Operation(
        summary = "Get all repair requests",
        description = "Returns a paginated list of repair requests with optional filtering and sorting"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved repair requests",
            content = @Content(schema = @Schema(implementation = PageResponse.class))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Unauthorized - Authentication required",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public ResponseEntity<PageResponse<RepairRequestResponseDto>> getRepairRequests(
            @Parameter(description = "Page number (1-based)") 
            @RequestParam(defaultValue = "1") int page,
            
            @Parameter(description = "Number of items per page") 
            @RequestParam(defaultValue = "20") int size,
            
            @Parameter(description = "Filter by status (PENDING, IN_PROGRESS, RESOLVED, CANCELLED)") 
            @RequestParam(required = false) String status,
            
            @Parameter(description = "Sort by field") 
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            
            @Parameter(description = "Sort direction (asc or desc)") 
            @RequestParam(required = false, defaultValue = "desc") String sortType) {

        if (size > 100) size = 100;
        
        return ResponseEntity.ok(
            repairRequestService.getRepairRequests(page, size, status, sortBy, sortType)
        );
    }

    @GetMapping("/{requestId}")
    @Operation(
        summary = "Get repair request by ID",
        description = "Returns details of a specific repair request"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved repair request",
            content = @Content(schema = @Schema(implementation = RepairRequestResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Repair request not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Unauthorized - Authentication required",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public ResponseEntity<RepairRequestResponseDto> getRepairRequest(
            @Parameter(description = "ID of the repair request") 
            @PathVariable Long requestId) {
        
        return ResponseEntity.ok(repairRequestService.getRepairRequest(requestId));
    }

    @PostMapping
    @Operation(
        summary = "Create repair request",
        description = "Creates a new repair request"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Repair request created successfully",
            content = @Content(schema = @Schema(implementation = RepairRequestResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Unauthorized - Authentication required",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public ResponseEntity<RepairRequestResponseDto> createRepairRequest(
            @Parameter(description = "Repair request data") 
            @Valid @RequestBody RepairRequestCreateDto createDto) {
        
        RepairRequestResponseDto createdRequest = repairRequestService.createRepairRequest(createDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdRequest);
    }

    @PutMapping("/{requestId}")
    @Operation(
        summary = "Update repair request",
        description = "Updates an existing repair request"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Repair request updated successfully",
            content = @Content(schema = @Schema(implementation = RepairRequestResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Repair request not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Unauthorized - Authentication required",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public ResponseEntity<RepairRequestResponseDto> updateRepairRequest(
            @Parameter(description = "ID of the repair request") 
            @PathVariable Long requestId,
            
            @Parameter(description = "Updated repair request data") 
            @Valid @RequestBody RepairRequestUpdateDto updateDto) {
        
        return ResponseEntity.ok(repairRequestService.updateRepairRequest(requestId, updateDto));
    }

    @PatchMapping("/{requestId}/status")
    @Operation(
        summary = "Update repair request status",
        description = "Updates the status of a repair request"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Status updated successfully",
            content = @Content(schema = @Schema(implementation = RepairRequestResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid status",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Repair request not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Unauthorized - Authentication required",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Forbidden - Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public ResponseEntity<RepairRequestResponseDto> updateStatus(
            @Parameter(description = "ID of the repair request") 
            @PathVariable Long requestId,
            
            @Parameter(description = "Status update data") 
            @Valid @RequestBody StatusUpdateDto statusUpdateDto) {
        
        return ResponseEntity.ok(repairRequestService.updateStatus(requestId, statusUpdateDto));
    }

    @DeleteMapping("/{requestId}")
    @Operation(
        summary = "Delete repair request",
        description = "Deletes a repair request"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Repair request deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Repair request not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Unauthorized - Authentication required",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Forbidden - Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public ResponseEntity<Void> deleteRepairRequest(
            @Parameter(description = "ID of the repair request") 
            @PathVariable Long requestId) {
        
        repairRequestService.deleteRepairRequest(requestId);
        return ResponseEntity.noContent().build();
    }
}