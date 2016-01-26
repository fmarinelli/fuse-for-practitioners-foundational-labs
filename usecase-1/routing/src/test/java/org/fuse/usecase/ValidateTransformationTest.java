package org.fuse.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileInputStream;
import java.io.IOException;

public class ValidateTransformationTest extends CamelSpringTestSupport {


    @Test
    public void transform() throws Exception {
        String inputCustomer = readFile("src/test/data/customer.csv");
        String expectedAccount = readFile("src/test/data/account.json");

        // setup expectations
        getMockEndpoint("mock:csv2json").expectedMessageCount(1);

        // set expected body as the unpretty print version of the json
        // (flattened)
        getMockEndpoint("mock:csv2json").allMessages().predicate(
                bodyAs(String.class).isEqualTo(jsonUnprettyPrint(expectedAccount))
        );

        // run test
        template.sendBody("direct:csv2json", inputCustomer);

        // verify results
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() throws Exception {
                from("direct:csv2json")
                        .to(endpoint("csv2json"))
                        .to("mock:csv2json");
            }
        };
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }

    private String readFile(String filePath) throws Exception {
        String content;
        FileInputStream fis = new FileInputStream(filePath);
        try {
            content = createCamelContext().getTypeConverter().convertTo(String.class, fis);
        } finally {
            fis.close();
        }
        return content;
    }

    private String jsonUnprettyPrint(String jsonString) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        JsonNode node = mapper.readTree(jsonString);
        return node.toString();
    }
}
