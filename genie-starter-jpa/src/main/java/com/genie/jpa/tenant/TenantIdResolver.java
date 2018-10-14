package com.genie.jpa.tenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class TenantIdResolver implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tendantId = ThreadLocalUtil.tendantId.get();
        tendantId  = "dataSource1:db1";
        return tendantId;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

}
