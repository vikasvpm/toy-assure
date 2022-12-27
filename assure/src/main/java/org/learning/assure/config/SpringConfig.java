package org.learning.assure.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ComponentScan("org.learning.assure")
@PropertySources({ //
        @PropertySource(value = "file:./assure.properties", ignoreResourceNotFound = true) //
})
public class SpringConfig {


}
