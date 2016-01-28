package org.fuse.usecase;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.globex.Account;
import org.globex.support.AccountError;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProcessorBean {

    public void debug(Exchange exchange) {
        Object body = (Object) exchange.getIn().getBody();
        Map<String, Object> headers = (Map<String, Object>) exchange.getIn().getHeaders();
        System.out.println(">> TO DEBUG >>");
    }

    public Map errorNamedParameters(@Body AccountError error) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ERROR_CODE", error.getErrorCode());
        parameters.put("ERROR_MESSAGE", error.getErrorMessage());
        parameters.put("MESSAGE", error.getMessage());
        parameters.put("STATUS", error.getStatus().toString());
        return parameters;
    }

    public Map<String, Object> defineNamedParameters(@Body Account ac) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("CLIENT_ID", 0);
        map.put("SALES_CONTACT", "");
        map.put("COMPANY_NAME", ac.getCompany().getName());
        map.put("COMPANY_GEO", ac.getCompany().getGeo());
        map.put("COMPANY_ACTIVE", ac.getCompany().isActive());
        map.put("CONTACT_FIRST_NAME", ac.getContact().getFirstName());
        map.put("CONTACT_LAST_NAME", ac.getContact().getLastName());
        map.put("CONTACT_ADDRESS", ac.getContact().getStreetAddr());
        map.put("CONTACT_CITY", ac.getContact().getCity());
        map.put("CONTACT_STATE", ac.getContact().getState());
        map.put("CONTACT_ZIP", ac.getContact().getZip());
        map.put("CONTACT_PHONE", ac.getContact().getPhone());
        map.put("CREATION_DATE", getCurrentTime());
        map.put("CREATION_USER", "fuse_usecase");
        return map;
    }

    public AccountError buildAccountError(Exchange exchange) {
        Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        if (exception == null) {
            throw new RuntimeException("Call build account error object without an exception in the exchange");
        }
        String body = exchange.getIn().getBody(String.class);
        return new AccountError(111, exception.getMessage(), body);
    }

    private static Timestamp getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        return currentTimestamp;
    }
}
