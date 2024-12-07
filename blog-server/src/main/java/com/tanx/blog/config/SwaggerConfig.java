package com.tanx.blog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @description: swagger，api描述文档配置
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/27 8:49
 */

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        // 网站基本信息
        Info info = new Info().title("博客API接口文档").description("接口描述").version("1.0.0");
        // 服务端的测试
        List<Server> list = List.of(
                new Server().url("http://localhost:7070").description("本地端口测试"),
                new Server().url("https://8f62-120-241-117-223.ngrok-free.app").description("ngrok端口测试")
        );
        List<SecurityRequirement> security = List.of(
        );
        return new OpenAPI().info(info).servers(list);
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                //分组名
                .group("前台接口")
                //扫描路径，将路径下有swagger注解的接口解析到文档中
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                //分组名
                .group("后台接口")
                //扫描路径，将路径下有swagger注解的接口解析到文档中
                .pathsToMatch("/auth/**")
                .build();
    }
}
