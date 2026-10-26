package com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.adapters;

import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.aggregates.EvaluationRule;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.InspectionItemId;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.ResultValue;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.repositories.EvaluationRuleRepository;
import com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.assemblers.EvaluationRulePersistenceAssembler;
import com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.repositories.EvaluationRulePersistenceRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EvaluationRuleRepositoryImpl implements EvaluationRuleRepository {

    private final EvaluationRulePersistenceRepository evaluationRulePersistenceRepository;
    private final ApplicationEventPublisher eventPublisher;

    public EvaluationRuleRepositoryImpl(EvaluationRulePersistenceRepository evaluationRulePersistenceRepository,
                                        ApplicationEventPublisher eventPublisher) {
        this.evaluationRulePersistenceRepository = evaluationRulePersistenceRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Optional<EvaluationRule> findById(UUID id) {
        return evaluationRulePersistenceRepository.findById(id).map(EvaluationRulePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<EvaluationRule> findAllByActiveTrue() {
        return evaluationRulePersistenceRepository.findAllByActiveTrue().stream()
                .map(EvaluationRulePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public boolean existsByInspectionItemIdAndFoundResultAndActiveTrue(InspectionItemId inspectionItemId, ResultValue foundResult) {
        return evaluationRulePersistenceRepository.existsByInspectionItemIdAndFoundResultAndActiveTrue(inspectionItemId.value(), foundResult);
    }

    @Override
    public EvaluationRule save(EvaluationRule evaluationRule) {
        var events = List.copyOf(evaluationRule.domainEvents());
        var saved = evaluationRulePersistenceRepository.save(EvaluationRulePersistenceAssembler.toPersistenceFromDomain(evaluationRule));
        events.forEach(eventPublisher::publishEvent);
        evaluationRule.clearDomainEvents();
        return EvaluationRulePersistenceAssembler.toDomainFromPersistence(saved);
    }
}
