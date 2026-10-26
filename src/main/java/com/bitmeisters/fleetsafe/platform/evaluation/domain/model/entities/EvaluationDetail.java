package com.bitmeisters.fleetsafe.platform.evaluation.domain.model.entities;

import com.bitmeisters.fleetsafe.platform.shared.domain.model.entities.AuditableModel;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.aggregates.Evaluation;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.InspectionResultId;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.RuleImpact;
import com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.converters.InspectionResultIdAttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
public class EvaluationDetail extends AuditableModel {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private Evaluation evaluation;

    @Column(nullable = false)
    @Convert(converter = InspectionResultIdAttributeConverter.class)
    private InspectionResultId inspectionResultId;

    @Column(nullable = false)
    private UUID evaluationRuleId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RuleImpact appliedImpact;

    protected EvaluationDetail() {}

    public EvaluationDetail(Evaluation evaluation, InspectionResultId inspectionResultId, UUID evaluationRuleId, RuleImpact appliedImpact) {
        this.evaluation = evaluation;
        this.inspectionResultId = inspectionResultId;
        this.evaluationRuleId = evaluationRuleId;
        this.appliedImpact = appliedImpact;
    }
}
