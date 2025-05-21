package org.spring.bsdev.starter.log_starter.config;

import org.spring.bsdev.starter.log_starter.aspect.LoggingAspect;
import org.spring.bsdev.starter.log_starter.props.LoggingProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnProperty(prefix = "logging.starter", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableAspectJAutoProxy
public class LoggingAutoConfig {

    @Bean
    public LoggingAspect loggingAspect(LoggingProperties props) {
        return new LoggingAspect(props);
    }
}
