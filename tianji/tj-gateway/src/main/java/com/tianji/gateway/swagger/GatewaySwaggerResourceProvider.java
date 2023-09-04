package com.tianji.gateway.swagger;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Primary
@RequiredArgsConstructor
public class GatewaySwaggerResourceProvider implements SwaggerResourcesProvider {

    /**
     * swagger2默认的url后缀
     */
    private static final String SWAGGER2_URL = "/v2/api-docs";

    /**
     * 路由定位器
     */
    private final RouteLocator routeLocator;

    /**
     * 网关应用名称
     */
    @Value("${spring.application.name}")
    private String gatewayName;

    /**
     * 获取 Swagger 资源
     */
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        Map<String, String> servers = new HashMap<>();
        // 1.获取路由 Uri中的 Host 作为服务名，把路由id作为请求路径，这里要确保路由id与路由path前缀一致
        routeLocator.getRoutes()
                .filter(route -> route.getUri().getHost() != null)
                .filter(route -> !gatewayName.equals(route.getUri().getHost()))
                .subscribe( r -> servers.put(r.getUri().getHost(), r.getId()));
        // 2.创建自定义资源
        servers.forEach((name, path) -> {
            // 创建Swagger 资源
            SwaggerResource swaggerResource = new SwaggerResource();
            // 设置访问地址
            swaggerResource.setUrl("/" + path + SWAGGER2_URL);
            // 设置名称
            swaggerResource.setName(name);
            swaggerResource.setSwaggerVersion("3.0.0");
            resources.add(swaggerResource);
        });
        return resources;
    }
}