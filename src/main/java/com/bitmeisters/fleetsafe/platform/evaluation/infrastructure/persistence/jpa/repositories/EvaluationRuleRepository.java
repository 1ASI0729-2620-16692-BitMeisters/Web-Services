package com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.repositories;

import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.aggregates.EvaluationRule;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.InspectionItemId;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.ResultValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EvaluationRuleRepository extends JpaRepository<EvaluationRule, UUID> {

    List<EvaluationRule> findAllByActiveTrue();

    boolean existsByInspectionItemIdAndFoundResultAndActiveTrue(InspectionItemId inspectionItemId, ResultValue foundResult);
}
