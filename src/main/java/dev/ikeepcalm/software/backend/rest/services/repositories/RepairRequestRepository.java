package dev.ikeepcalm.software.backend.rest.services.repositories;

import dev.ikeepcalm.software.backend.rest.entities.RepairRequest;
import dev.ikeepcalm.software.backend.rest.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {

    Page<RepairRequest> findByStatus(RequestStatus status, Pageable pageable);

    Page<RepairRequest> findByCustomerId(Long customerId, Pageable pageable);

    Page<RepairRequest> findByTechnicianId(Long technicianId, Pageable pageable);
}