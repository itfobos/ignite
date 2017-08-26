package cache;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableIgniteRepositories
public class SpringCacheConfig {
    @Bean
    public Ignite igniteInstance() {
        Ignition.setClientMode(true);

        return Ignition.start("example-default.xml");
    }
}
