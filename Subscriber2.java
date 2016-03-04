import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSubscriber;
import javax.jms.TopicSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class Subscriber2 {

    public static void main(String[] args){
    	
    	Context context = null;
    	TopicConnection connection = null;
    	
        try {            
            System.out.println("SUBSCRIBER");
            
            // context
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, 
                    "org.exolab.jms.jndi.InitialContextFactory");    
            props.put(Context.PROVIDER_URL, "tcp://localhost:3035/");
            context = new InitialContext(props);
            System.out.println("Context OK");
            
            // connection
            TopicConnectionFactory connectionFactory = 
                    (TopicConnectionFactory)context.lookup("ConnectionFactory");
            connection = connectionFactory.createTopicConnection();
            System.out.println("Connection OK");
            
            // session & Topic
            TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE); 
            Topic topic = (Topic)context.lookup("topic1");             
            System.out.println("Topic OK");
            
            // CONSUMER
            TopicSubscriber subscriber = session.createDurableSubscriber(topic, "sub");
            connection.start();
            System.out.println("Connection started");
            
            while(true){
                System.out.println("Waiting for message...");
                TextMessage message = (TextMessage) subscriber.receive();         // blocking
                System.out.println("Received message: " + message.getText());
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
