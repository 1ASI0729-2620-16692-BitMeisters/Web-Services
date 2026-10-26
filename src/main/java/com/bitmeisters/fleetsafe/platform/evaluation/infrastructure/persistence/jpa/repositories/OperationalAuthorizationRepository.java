package com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.repositories;

import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.aggregates.OperationalAuthorization;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.VehicleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OperationalAuthorizationRepository extends JpaRepository<OperationalAuthorization, UUID> {

    Optional<OperationalAuthorization> findFirstByVehicleIdOrderByAuthorizedAtDesc(VehicleId vehicleId);

    List<OperationalAuthorization> findAllByVehicleIdOrderByAuthorizedAtDesc(VehicleId vehicleId);
}
