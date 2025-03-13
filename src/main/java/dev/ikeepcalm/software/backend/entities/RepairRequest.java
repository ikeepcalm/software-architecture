package dev.ikeepcalm.software.backend.entities;

import dev.ikeepcalm.software.backend.enums.EquipmentType;
import dev.ikeepcalm.software.backend.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "repair_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;

    private Long technicianId;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentType equipmentType;

    @Column(columnDefinition = "json")
    private String equipmentDetails;

    private Long serviceLevelId;

    private Double estimatedCost;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column(nullable = false)
    private ZonedDateTime updatedAt;
}