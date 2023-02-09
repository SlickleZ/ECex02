/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package live_socker_score_consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author sarun
 */
public class TextListener implements MessageListener {
    @Override
    public void onMessage(Message msg) {
        try {
            if (msg != null && msg instanceof TextMessage) {
                TextMessage message = (TextMessage) msg;
                String bodyText = message.getText();

                if (bodyText != null) {
                    System.out.println("Updated!: " + bodyText);
                }
            }
        } catch (JMSException e) {
            System.err.println("JMSException in onMessage(): " + e.toString());
        } catch (Throwable t) {
            System.err.println("Exception in onMessage():" + t.getMessage());
        }
    }
    
}
