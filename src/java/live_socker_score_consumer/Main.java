/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package live_socker_score_consumer;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 *
 * @author SlickleZ
 */
public class Main {
    @Resource(mappedName = "jms/SimpleJMSTopic")
    private static Topic topic;
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    
    public static void main(String[] args) {
        Destination dest = null;
        Connection connection = null;
        
        try {
            dest = (Destination) topic;
        } catch (Exception e) {
            System.err.println("Error setting destination: " + e.toString());
            System.exit(1);
        }
        
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(dest);
            
            connection.start();
            
            System.out.println("Welcome to live football score subscriber!");
            
            while (true) {
                Message msg = consumer.receive();

                if (msg != null && msg instanceof TextMessage) {
                    TextMessage message = (TextMessage) msg;
                    String bodyText = message.getText();
                    
                    if (bodyText != null) {
                        System.out.println("Updated!: " + bodyText);
                    }   
                }
            }
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
            System.exit(1);
        } finally {
            try {
                if (connection != null) { connection.close(); }
            } catch (JMSException ex) {
                System.err.println("Exception occurred when closing connection: " + ex.toString());
            }
        }
    }
}
