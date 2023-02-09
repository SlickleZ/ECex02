/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package live_socker_score_consumer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
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
        InputStreamReader inputStreamReader = null;
        TextListener messageListener = null;
        char answer = '\0';
        
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
            messageListener = new TextListener();
            consumer.setMessageListener(messageListener);

            connection.start();
            
            System.out.println("============================================");
            System.out.println(" Welcome to live football score subscriber!");
            System.out.println("============================================\n");
            
            System.out.println(
                    "To end program, type Q or q, " + "then <return>");
            inputStreamReader = new InputStreamReader(System.in);

            while (!((answer == 'q') || (answer == 'Q'))) {
                try {
                    answer = (char) inputStreamReader.read();
                } catch (IOException e) {
                    System.err.println("I/O exception: " + e.toString());
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
