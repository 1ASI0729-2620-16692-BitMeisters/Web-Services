package com.bitmeisters.fleetsafe.platform.iam.domain.model.queries;

import com.bitmeisters.fleetsafe.platform.iam.domain.model.valueobjects.CompanyId;

public record GetUsersByCompanyIdQuery(CompanyId companyId) {

    public GetUsersByCompanyIdQuery {
        if (companyId == null) throw new IllegalArgumentException("iam.user.error.company-id.required");
    }
}
