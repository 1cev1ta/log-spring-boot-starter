package org.spring.bsdev.starter.log_starter.props;

import lombok.Data;
import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "logging.starter")
public class LoggingProperties {
    private boolean enabled = true;
    private Level level = Level.INFO;
}
