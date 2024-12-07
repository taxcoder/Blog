package com.tanx.blog.config;

import com.tanx.blog.interceptor.RepeatRequestInterceptor;
import com.tanx.blog.interceptor.UpdateLoginTimeInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 跨域处理
 *
 * @author tanxiang
 * @Date 2021/5/26 12:59
 */

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Resource
    private RepeatRequestInterceptor interceptor;
    @Resource
    private UpdateLoginTimeInterceptor updateLoginTimeInterceptor;

    private static final List<String> list = new ArrayList<>();

    @Resource
    private Environment environment;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**");
        registry.addInterceptor(updateLoginTimeInterceptor).addPathPatterns("/auth/**");
    }

    public WebMvcConfiguration(Environment environment) {
        this.environment = environment;
        if ("dev".equals(this.environment.getProperty("spring.profiles.active"))) {
            list.add("*");
        } else {
            list.add("*");
        }
    }

    private org.springframework.web.cors.CorsConfiguration buildConfig() {
        org.springframework.web.cors.CorsConfiguration corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
        // 设置所有的域名
        corsConfiguration.setAllowedOriginPatterns(list);
        // 设置所有的请求头
        corsConfiguration.addAllowedHeader("*");
        // 设置所有的方法
        corsConfiguration.setAllowedMethods(List.of("POST", "GET", "DELETE", "PUT", "OPTIONS", "HEAD"));
        // 设置cookie可以传递
        corsConfiguration.setAllowCredentials(true);
        // 设置最大时间
        corsConfiguration.setMaxAge(3600L);
        return corsConfiguration;
    }

    @Bean
    public UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return source;
    }
}
