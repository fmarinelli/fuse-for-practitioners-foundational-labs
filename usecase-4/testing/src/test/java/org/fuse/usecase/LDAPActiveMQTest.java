package org.fuse.usecase;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.directory.server.annotations.CreateLdapServer;
import org.apache.directory.server.annotations.CreateTransport;
import org.apache.directory.server.core.annotations.ApplyLdifFiles;
import org.apache.directory.server.core.integ.AbstractLdapTestUnit;
import org.apache.directory.server.core.integ.FrameworkRunner;
import org.apache.directory.server.ldap.LdapServer;
import org.fuse.usecase.support.JMSEncapsulation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

@RunWith(FrameworkRunner.class)
@CreateLdapServer(transports = {
        @CreateTransport(protocol = "LDAP", port = 1024)
})
@ApplyLdifFiles("org/fuse/usecase/activemq.ldif")
public class LDAPActiveMQTest
        extends AbstractLdapTestUnit {

    public BrokerService broker;
    public static LdapServer ldapServer;

    @Before
    public void setup() throws Exception {
        System.setProperty("ldapPort", String.valueOf(getLdapServer().getPort()));
        broker = BrokerFactory.createBroker("xbean:org/fuse/usecase/activemq-broker.xml");
        broker.start();
        broker.waitUntilStarted();
    }

    @After
    public void shutdown() throws Exception {
        broker.stop();
        broker.waitUntilStopped();
    }

    @Test
    public void testFailCreateSessionNotEnoughRight() throws Exception {
    }

    @Test
    public void testCreateQueuePublishConsume() throws Exception {
        JMSEncapsulation encapsulation = new JMSEncapsulation();
        try {
            Session session = encapsulation.createSession("jdoe", "sunflower");
            Queue queue = session.createQueue("usecase-input");
            MessageProducer producer = session.createProducer(queue);
            TextMessage test = session.createTextMessage("test");
            producer.send(test);
        } finally {
            encapsulation.close();
        }
    }

    @Test(expected = JMSException.class)
    public void testFailCreateQueuePublishConsume() throws Exception {
        JMSEncapsulation encapsulation = new JMSEncapsulation();
        try {
            Session session = encapsulation.createSession("jdoe", "sunflower");
            Queue queue = session.createQueue("usecase-input2");
            MessageProducer producer = session.createProducer(queue);
            TextMessage test = session.createTextMessage("test");
            producer.send(test);
        } finally {
            encapsulation.close();
        }
    }

}