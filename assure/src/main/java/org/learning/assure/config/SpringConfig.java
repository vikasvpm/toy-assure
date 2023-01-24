package org.learning.assure.config;

import org.springframework.context.annotation.*;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@ComponentScan("org.learning.assure")
@PropertySources({ //
        @PropertySource(value = "file:./assure.properties", ignoreResourceNotFound = true) //
})
public class SpringConfig {
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver cmr = new CommonsMultipartResolver();
        int maxUploadSizeInMb = 5 * 1024 * 1024;// 5 MB
        cmr.setMaxUploadSize(maxUploadSizeInMb * 2);
        cmr.setMaxUploadSizePerFile(maxUploadSizeInMb); //bytes
        return cmr;
    }
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
