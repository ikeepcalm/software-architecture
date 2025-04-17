package dev.ikeepcalm.software.backend.eventdriven.events;

import dev.ikeepcalm.software.backend.rest.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairRequestStatusChangedEvent {
    private Long requestId;
    private RequestStatus oldStatus;
    private RequestStatus newStatus;
    private String notes;
    private Long technicianId;
    private ZonedDateTime timestamp;
}