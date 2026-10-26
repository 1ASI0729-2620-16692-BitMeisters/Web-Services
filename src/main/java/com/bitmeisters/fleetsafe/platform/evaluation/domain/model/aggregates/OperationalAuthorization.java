package com.bitmeisters.fleetsafe.platform.evaluation.domain.model.aggregates;

import com.bitmeisters.fleetsafe.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.bitmeisters.fleetsafe.platform.shared.domain.model.valueobjects.Timestamps;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.commands.LiftBlockCommand;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.events.InspectionEvaluatedEvent;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.AuthorizationStatus;
import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.VehicleId;
import com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.converters.VehicleIdAttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
public class OperationalAuthorization extends AuditableAbstractAggregateRoot<OperationalAuthorization> {

    @Column(nullable = false)
    private UUID evaluationId;

    @Column(nullable = false)
    @Convert(converter = VehicleIdAttributeConverter.class)
    private VehicleId vehicleId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthorizationStatus status;

    @Column(name = "is_override", nullable = false)
    private boolean override;

    @Column(columnDefinition = "text")
    private String overrideReason;

    private UUID authorizedBy;

    @Column(nullable = false)
    private Instant authorizedAt;

    private Instant validUntil;

    protected OperationalAuthorization() {}

    public OperationalAuthorization(Evaluation evaluation, VehicleId vehicleId) {
        this.evaluationId = evaluation.getId();
        this.vehicleId = vehicleId;
        this.status = AuthorizationStatus.fromImpact(evaluation.resolveMostRestrictiveImpact());
        this.override = false;
        this.authorizedBy = evaluation.getEvaluatedBy();
        this.authorizedAt = Timestamps.now();
        addDomainEvent(new InspectionEvaluatedEvent(evaluation.getInspectionId(), vehicleId, status));
    }

    public OperationalAuthorization(OperationalAuthorization blocked, LiftBlockCommand command) {
        if (blocked.status != AuthorizationStatus.NOT_ENABLED) {
            throw new IllegalArgumentException("evaluation.authorization.error.not-blocked");
        }
        this.evaluationId = blocked.evaluationId;
        this.vehicleId = blocked.vehicleId;
        this.status = AuthorizationStatus.ENABLED;
        this.override = true;
        this.overrideReason = command.reason();
        this.authorizedBy = command.authorizedBy();
        this.authorizedAt = Timestamps.now();
    }

    public boolean isBlocked() {
        return status == AuthorizationStatus.NOT_ENABLED;
    }

    public boolean liftsBlock() {
        return override;
    }

    public boolean isValidOn(Instant moment) {
        return validUntil == null || !moment.isAfter(validUntil);
    }
}
