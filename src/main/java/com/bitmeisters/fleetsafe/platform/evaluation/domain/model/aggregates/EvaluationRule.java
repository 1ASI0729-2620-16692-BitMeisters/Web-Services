package com.bitmeisters.fleetsafe.platform.evaluation.domain.model.aggregates;

import com.bitmeisters.fleetsafe.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.commands.DefineEvaluationRuleCommand;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.InspectionItemId;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.ResultValue;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.RuleImpact;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.RuleName;
import com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.converters.InspectionItemIdAttributeConverter;
import com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.converters.RuleNameAttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
@Entity
public class EvaluationRule extends AuditableAbstractAggregateRoot<EvaluationRule> {

    @Column(nullable = false)
    @Convert(converter = InspectionItemIdAttributeConverter.class)
    private InspectionItemId inspectionItemId;

    @Column(nullable = false, length = RuleName.MAX_LENGTH)
    @Convert(converter = RuleNameAttributeConverter.class)
    private RuleName name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ResultValue foundResult;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RuleImpact resultingImpact;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    protected EvaluationRule() {}

    public EvaluationRule(DefineEvaluationRuleCommand command) {
        this.inspectionItemId = command.inspectionItemId();
        this.name = command.name();
        this.foundResult = command.foundResult();
        this.resultingImpact = command.resultingImpact();
        this.description = command.description();
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean appliesTo(InspectionItemId inspectionItemId, ResultValue result) {
        return active && this.inspectionItemId.equals(inspectionItemId) && this.foundResult == result;
    }
}
