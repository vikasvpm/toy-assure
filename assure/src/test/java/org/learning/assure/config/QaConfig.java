package org.learning.assure.config;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(//
        basePackages = { "org.learning.assure" }, //
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = { SpringConfig.class })//
)
@PropertySources({ //
        @PropertySource(value ="classpath:test.properties", ignoreResourceNotFound = true) //
})
public class QaConfig {

}
