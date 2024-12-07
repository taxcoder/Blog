package com.tanx.blog.config;

import com.tanx.blog.filter.JwtFilter;
import com.tanx.blog.handler.UserDetailHandler;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author zhong
 */

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Resource
    private UserDetailHandler userDetailHandler;
    @Resource
    private JwtFilter jwtFilter;
    @Resource
    private UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //使用自定义的用户校验
        authProvider.setUserDetailsService(userDetailHandler);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //禁用csrf保护，前后端分离不需要
                .csrf(AbstractHttpConfigurer::disable)
                //以下是验证请求拦截和放行配置
                .authorizeHttpRequests(auth -> {
                    // 放行swagger
                    auth.requestMatchers("/api/**").permitAll();
                    auth.requestMatchers(HttpMethod.OPTIONS).permitAll();
                    auth.anyRequest().authenticated();
                })
                //取消默认登出页面的使用
                .logout(AbstractHttpConfigurer::disable)
                //取消默认登录页面的使用
                .formLogin(AbstractHttpConfigurer::disable)
                //禁用session，因为我们已经使用了JWT
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //禁用httpBasic，因为我们传输数据用的是post，而且请求体是JSON
                .httpBasic(AbstractHttpConfigurer::disable)
                // 使用自定义的权限查询校验方法
                .authenticationProvider(authenticationProvider())
                // 配置请求拦截
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                // 配置自定义cors
                .cors(configurer -> configurer.configurationSource(urlBasedCorsConfigurationSource));
        return http.build();
    }
}

