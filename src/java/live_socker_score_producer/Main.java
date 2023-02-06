/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package live_socker_score_producer;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import java.util.Scanner;

/**
 *
 * @author SlickleZ
 */
public class Main {
    @Resource(mappedName = "jms/SimpleJMSTopic")
    private static Topic topic;
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Connection connection = null;
        Destination dest = null;
        Scanner userListener;
        
        try {
            dest = (Destination) topic;
        } catch (Exception e) {
            System.err.println("Error setting destination: " + e.toString());
            System.exit(1);
        }
        
        try {
            userListener = new Scanner(System.in);
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(null);
            TextMessage message = session.createTextMessage();
            
            System.out.println("Welcome to live score broadcast!");
            
            while(true) {
                System.out.print("Enter the live score: ");
                String userInput = userListener.nextLine();
                
                message.setText(userInput);
                
                producer.send(dest, message);
                producer.send(dest, session.createMessage()); // ending message
            }
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
        } finally {
            try {
                if (connection != null) { connection.close(); }
            } catch (JMSException ex) {
                System.err.println("Exception occurred when close connection: " + ex.toString());
            }
        }
    }
    
}
