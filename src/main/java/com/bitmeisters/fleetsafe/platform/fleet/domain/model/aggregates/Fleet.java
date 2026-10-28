package com.bitmeisters.fleetsafe.platform.fleet.domain.model.aggregates;

import com.bitmeisters.fleetsafe.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.util.UUID;

/**
 * A group of vehicles that a company operates together. A fleet cannot exist outside its company.
 */
@Getter
@Entity
public class Fleet extends AuditableAbstractAggregateRoot<Fleet> {

    @Column(nullable = false)
    private UUID companyId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    protected Fleet() {}

    public Fleet(UUID companyId, String name, String description) {
        this.companyId = companyId;
        this.name = name;
        this.description = description;
    }
}
