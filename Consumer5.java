import java.util.Enumeration;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class Consumer5 {

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
            QueueBrowser browser = session.createBrowser(queue);
            connection.start();
            System.out.println("Connection started");
            
            while(true){
            	
                Enumeration msgs = browser.getEnumeration();
                while(msgs.hasMoreElements()) {
                    System.out.println("Received message: " + ((TextMessage) msgs.nextElement()).getText());
                }
                
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
