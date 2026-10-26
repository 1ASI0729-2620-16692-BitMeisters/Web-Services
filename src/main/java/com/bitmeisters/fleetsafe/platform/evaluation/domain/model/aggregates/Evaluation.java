package com.bitmeisters.fleetsafe.platform.evaluation.domain.model.aggregates;

import com.bitmeisters.fleetsafe.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.bitmeisters.fleetsafe.platform.shared.domain.model.valueobjects.Timestamps;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.commands.EvaluateInspectionCommand;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.entities.EvaluationDetail;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.InspectionId;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.InspectionResultSnapshot;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.RuleImpact;
import com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.converters.InspectionIdAttributeConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
public class Evaluation extends AuditableAbstractAggregateRoot<Evaluation> {

    @Column(nullable = false, unique = true)
    @Convert(converter = InspectionIdAttributeConverter.class)
    private InspectionId inspectionId;

    @Column(nullable = false)
    private Instant evaluatedAt;

    private UUID evaluatedBy;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvaluationDetail> details = new ArrayList<>();

    protected Evaluation() {}

    public Evaluation(EvaluateInspectionCommand command, List<EvaluationRule> activeRules) {
        this.inspectionId = command.inspectionId();
        this.evaluatedAt = Timestamps.now();
        this.evaluatedBy = command.evaluatedBy();
        command.results().forEach(result -> applyMatchingRule(result, activeRules));
    }

    private void applyMatchingRule(InspectionResultSnapshot result, List<EvaluationRule> activeRules) {
        activeRules.stream()
                .filter(rule -> rule.appliesTo(result.inspectionItemId(), result.value()))
                .findFirst()
                .ifPresent(rule -> details.add(new EvaluationDetail(
                        this, result.inspectionResultId(), rule.getId(), rule.getResultingImpact())));
    }

    public RuleImpact resolveMostRestrictiveImpact() {
        return details.stream()
                .map(EvaluationDetail::getAppliedImpact)
                .reduce(RuleImpact.NONE, RuleImpact::mostRestrictive);
    }

    public boolean isAutomatic() {
        return evaluatedBy == null;
    }

    public List<EvaluationDetail> getDetails() {
        return Collections.unmodifiableList(details);
    }
}
