import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public class Bootstrap {
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) throws Exception {        
        logger.info("boot info.");
        //PropertySourcesPlaceholderConfigurer placeholder = ctx.getBean(PropertySourcesPlaceholderConfigurer.class);     

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(ServerConfig.class);
        ctx.refresh();
        
        ScheduledServer schedServer = ctx.getBean(ScheduledServer.class);
        //serverSched.start();
    }
    
}