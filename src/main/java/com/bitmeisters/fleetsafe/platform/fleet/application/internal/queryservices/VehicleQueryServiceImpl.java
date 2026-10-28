package com.bitmeisters.fleetsafe.platform.fleet.application.internal.queryservices;

import com.bitmeisters.fleetsafe.platform.fleet.application.queryservices.VehicleQueryService;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.aggregates.Vehicle;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.queries.GetAllVehiclesQuery;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.queries.GetVehicleByIdQuery;
import com.bitmeisters.fleetsafe.platform.fleet.infrastructure.persistence.jpa.repositories.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {

    private final VehicleRepository vehicleRepository;

    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Vehicle> handle(GetAllVehiclesQuery query) {
        var results = vehicleRepository.findAll();
        log.debug("Found {} vehicles", results.size());
        return results;
    }

    @Override
    public Optional<Vehicle> handle(GetVehicleByIdQuery query) {
        var result = vehicleRepository.findById(query.vehicleId());
        if (result.isEmpty()) log.debug("No vehicle found for id: {}", query.vehicleId());
        return result;
    }
}