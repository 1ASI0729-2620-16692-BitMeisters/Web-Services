package com.bitmeisters.fleetsafe.platform.iam.domain.model.aggregates;

import com.bitmeisters.fleetsafe.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.commands.RegisterUserCommand;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.valueobjects.CompanyId;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.valueobjects.EmailAddress;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.valueobjects.PersonName;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.valueobjects.Role;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class User extends AbstractDomainAggregateRoot<User> {

    private UUID id;
    private CompanyId companyId;
    private EmailAddress email;
    private String passwordHash;
    private PersonName name;
    private Role role;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public User(RegisterUserCommand command) {
        this.companyId = command.companyId();
        this.email = command.email();
        this.passwordHash = command.passwordHash();
        this.name = command.name();
        this.role = command.role();
        this.active = true;
    }

    public static User restore(UUID id, CompanyId companyId, EmailAddress email, String passwordHash,
                               PersonName name, Role role, boolean active, Instant createdAt, Instant updatedAt) {
        var user = new User(new RegisterUserCommand(companyId, email, passwordHash, name, role));
        user.id = id;
        user.active = active;
        user.createdAt = createdAt;
        user.updatedAt = updatedAt;
        return user;
    }

    public void changeRole(Role role) {
        this.role = role;
    }

    public void deactivate() {
        this.active = false;
    }
}
