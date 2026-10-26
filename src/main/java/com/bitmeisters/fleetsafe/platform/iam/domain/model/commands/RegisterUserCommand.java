package com.bitmeisters.fleetsafe.platform.iam.domain.model.commands;

import com.bitmeisters.fleetsafe.platform.iam.domain.model.valueobjects.CompanyId;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.valueobjects.EmailAddress;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.valueobjects.PersonName;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.valueobjects.Role;

public record RegisterUserCommand(
        CompanyId companyId,
        EmailAddress email,
        String passwordHash,
        PersonName name,
        Role role) {

    public RegisterUserCommand {
        if (companyId == null) throw new IllegalArgumentException("iam.user.error.company-id.required");
        if (email == null) throw new IllegalArgumentException("iam.user.error.email.required");
        if (passwordHash == null || passwordHash.isBlank()) throw new IllegalArgumentException("iam.user.error.password.required");
        if (name == null) throw new IllegalArgumentException("iam.user.error.first-name.required");
        if (role == null) throw new IllegalArgumentException("iam.user.error.role.required");
    }
}
