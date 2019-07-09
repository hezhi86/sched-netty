
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@PropertySource("classpath:server.properties")
@Configuration
public class ServerConfig {

	@Value("${registry.address}")
	private String registryAddress;

	@Value("${server.address}")
	private String serverAddress;

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	} 

	public static final String SchedServer = "SchedServer";
	@Bean(name = "SchedServer")
	public ScheduledServer schedServer() {
		return new ScheduledServer(serverAddress);
	}

}
