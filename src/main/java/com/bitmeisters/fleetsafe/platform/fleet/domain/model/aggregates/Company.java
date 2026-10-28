package com.bitmeisters.fleetsafe.platform.fleet.domain.model.aggregates;

import com.bitmeisters.fleetsafe.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

/**
 * A transport company that owns one or more fleets.
 */
@Getter
@Entity
public class Company extends AuditableAbstractAggregateRoot<Company> {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String taxId;

    private String address;

    private String phone;

    private String email;

    protected Company() {}

    public Company(String name, String taxId, String address, String phone, String email) {
        this.name = name;
        this.taxId = taxId;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }
}
