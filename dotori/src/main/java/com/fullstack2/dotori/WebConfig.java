package com.fullstack2.dotori;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	 String resourceLocation = "classpath:/static/";
		 registry.addResourceHandler("/**")
		 .addResourceLocations("file:src/main/resources/templates/", "file:src/main/resources/static/");
		 
		    registry.addResourceHandler("/static/**").addResourceLocations(resourceLocation).setCachePeriod(60*60*24*365);	
		/* '/js/**'로 호출하는 자원은 '/static/js/' 폴더 아래에서 찾는다. */ 
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/").setCachePeriod(60 * 60 * 24 * 365); 
		/* '/css/**'로 호출하는 자원은 '/static/css/' 폴더 아래에서 찾는다. */ 
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/").setCachePeriod(60 * 60 * 24 * 365); 
		
        registry.addResourceHandler("/imgs/**").addResourceLocations("classpath:/static/imgs/").setCachePeriod(60 * 60 * 24 * 365); 
		/* '/font/**'로 호출하는 자원은 '/static/font/' 폴더 아래에서 찾는다. */ 
        registry.addResourceHandler("/font/**").addResourceLocations("classpath:/static/font/").setCachePeriod(60 * 60 * 24 * 365); 
        registry.addResourceHandler("/jquery/**").addResourceLocations("classpath:/static/jquery/").setCachePeriod(60 * 60 * 24 * 365); 
	
	
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:E:\\uploadEx\\");
    }
}
