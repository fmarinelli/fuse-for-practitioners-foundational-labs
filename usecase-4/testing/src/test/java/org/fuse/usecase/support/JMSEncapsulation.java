package org.fuse.usecase.support;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class JMSEncapsulation {

    private final static Logger log = LoggerFactory.getLogger(JMSEncapsulation.class);
    private final static ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616?jms.watchTopicAdvisories=false");

    private Connection connection = null;
    private Session session = null;

    public Session createSession(String userName, String password) {
        try {
            connection = connectionFactory.createConnection(userName, password);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            return session;
        } catch (JMSException e) {
            // Wrapping the exception for a fail fast
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                log.warn(e.getMessage(), e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                log.warn(e.getMessage(), e);
            }
        }
    }
}
