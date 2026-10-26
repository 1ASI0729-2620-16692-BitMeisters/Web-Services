package com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.adapters;

import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.aggregates.Evaluation;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.InspectionId;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.repositories.EvaluationRepository;
import com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.assemblers.EvaluationPersistenceAssembler;
import com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.repositories.EvaluationPersistenceRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EvaluationRepositoryImpl implements EvaluationRepository {

    private final EvaluationPersistenceRepository evaluationPersistenceRepository;
    private final ApplicationEventPublisher eventPublisher;

    public EvaluationRepositoryImpl(EvaluationPersistenceRepository evaluationPersistenceRepository,
                                    ApplicationEventPublisher eventPublisher) {
        this.evaluationPersistenceRepository = evaluationPersistenceRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public boolean existsByInspectionId(InspectionId inspectionId) {
        return evaluationPersistenceRepository.existsByInspectionId(inspectionId.value());
    }

    @Override
    public Optional<Evaluation> findByInspectionId(InspectionId inspectionId) {
        return evaluationPersistenceRepository.findByInspectionId(inspectionId.value())
                .map(EvaluationPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Evaluation save(Evaluation evaluation) {
        var events = List.copyOf(evaluation.domainEvents());
        var saved = evaluationPersistenceRepository.save(EvaluationPersistenceAssembler.toPersistenceFromDomain(evaluation));
        events.forEach(eventPublisher::publishEvent);
        evaluation.clearDomainEvents();
        return EvaluationPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
