package vuetest.aydinrest.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static vuetest.aydinrest.controller.MessageController.uploadPath;


@Configuration
public class MvcConfig implements WebMvcConfigurer {





    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///"+uploadPath+"/");
    }


}
