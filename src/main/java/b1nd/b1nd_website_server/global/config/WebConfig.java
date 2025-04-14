package b1nd.b1nd_website_server.global.config;

import b1nd.b1nd_website_server.global.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final AuthInterceptor authInterceptor;

//
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedHeaders("*")
//                .allowedMethods("*")
//                .allowedOrigins("*");
//    }
//
//
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor((HandlerInterceptor) authInterceptor);
//    }
}
