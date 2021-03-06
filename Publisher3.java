import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class Publisher3 {
    
    public static void main(String[] args){
    	
    	Context context = null;
    	TopicConnection connection = null;
    	
        try {            
            System.out.println("PUBLISHER");
            
            // context
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, 
                    "org.exolab.jms.jndi.InitialContextFactory");    
            props.put(Context.PROVIDER_URL, "tcp://localhost:3035/");
            context = new InitialContext(props);
            System.out.println("Context OK");
            
            // connection
            TopicConnectionFactory factory = 
                    (TopicConnectionFactory)context.lookup("ConnectionFactory");
            connection = factory.createTopicConnection();
            System.out.println("Connection OK");
            
            // session & queue
            TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE); 
            Topic topic = (Topic)context.lookup("topic1");             
            System.out.println("Topic OK");
            
            // PRODUCER
            TopicPublisher publisher = session.createPublisher(topic);
            connection.start();
            System.out.println("Connection started");
            
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            int priority = 0;
            
            while(true){
                System.out.println("Type message: ");
                String msg = br.readLine();
                TextMessage message = session.createTextMessage(msg); 
                message.setIntProperty("priority", priority++ % 2);
                publisher.send(message);                
                System.out.println("Message sent");          
            }   
            
        } catch (Exception ex) {
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
