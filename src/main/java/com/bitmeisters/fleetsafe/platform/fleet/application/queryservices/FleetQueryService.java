package com.bitmeisters.fleetsafe.platform.fleet.application.queryservices;

import com.bitmeisters.fleetsafe.platform.fleet.domain.model.aggregates.Fleet;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.queries.GetAllFleetsQuery;

import java.util.List;

public interface FleetQueryService {

    List<Fleet> handle(GetAllFleetsQuery query);
}
