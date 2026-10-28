package com.bitmeisters.fleetsafe.platform.fleet.application.internal.queryservices;

import com.bitmeisters.fleetsafe.platform.fleet.application.queryservices.FleetQueryService;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.aggregates.Fleet;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.queries.GetAllFleetsQuery;
import com.bitmeisters.fleetsafe.platform.fleet.infrastructure.persistence.jpa.repositories.FleetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FleetQueryServiceImpl implements FleetQueryService {

    private final FleetRepository fleetRepository;

    public FleetQueryServiceImpl(FleetRepository fleetRepository) {
        this.fleetRepository = fleetRepository;
    }

    @Override
    public List<Fleet> handle(GetAllFleetsQuery query) {
        var results = fleetRepository.findAll();
        log.debug("Found {} fleets", results.size());
        return results;
    }
}