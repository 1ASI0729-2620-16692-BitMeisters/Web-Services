package com.bitmeisters.fleetsafe.platform.fleet.application.queryservices;

import com.bitmeisters.fleetsafe.platform.fleet.domain.model.aggregates.Vehicle;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.queries.GetAllVehiclesQuery;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.queries.GetVehicleByIdQuery;

import java.util.List;
import java.util.Optional;

public interface VehicleQueryService {

    List<Vehicle> handle(GetAllVehiclesQuery query);

    Optional<Vehicle> handle(GetVehicleByIdQuery query);
}
