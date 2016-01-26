package org.fuse.usecase.service;

import org.globex.Account;
import org.globex.Company;

public class CustomerRestImpl implements CustomerRest {

    private static final String NA_REGION = "NORTH_AMERICA";
    private static final String SA_REGION = "SOUTH_AMERICA";
    private static final String WA_REGION = "WEST_AMERICA";
    private static final String EA_REGION = "EAST_AMERICA";

    @Override
    public Account enrich(Account account) {
        Company company = account.getCompany();
        String region = company.getGeo();
        switch (Region.valueOf(region)) {
            case NA : company.setGeo(NA_REGION); break;
            case SA : company.setGeo(SA_REGION); break;
            case WA: company.setGeo(WA_REGION); break;
            case EA : company.setGeo(EA_REGION); break;
        }
        return account;
    }
}
