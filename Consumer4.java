import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class Consumer4 {

    public static void main(String[] args){
    	
    	Context context = null;
    	QueueConnection connection = null;
    	
        try {            
            System.out.println("CONSUMER");
            
            // context
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, 
                    "org.exolab.jms.jndi.InitialContextFactory");    
            props.put(Context.PROVIDER_URL, "tcp://localhost:3035/");
            context = new InitialContext(props);
            System.out.println("Context OK");
            
            // connection
            QueueConnectionFactory factory = 
                    (QueueConnectionFactory)context.lookup("ConnectionFactory");
            connection = factory.createQueueConnection();
            System.out.println("Connection OK");
            
            // session & queue
            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE); 
            Queue queue = (Queue)context.lookup("queue1");             
            System.out.println("Queue OK");
            
            // CONSUMER
            QueueReceiver receiver = session.createReceiver(queue);
            receiver.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message message) {
					try {
						System.out.println("Received message: " + ((TextMessage)message).getText());
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
            connection.start();
            System.out.println("Connection started");
            
            while(true){
                /*System.out.println("Waiting for message...");
                TextMessage message = (TextMessage) receiver.receive();         // blocking
                System.out.println("Received message: " + message.getText());*/
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
         } finally {
			
            // close the context
            if (context != null) {
                try {
                    context.close();
                } catch (NamingException exception) {
                    exception.printStackTrace();
                }
            }

            // close the connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }    
}
