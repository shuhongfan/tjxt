package com.tianji.gateway.swagger;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.List;

@RestController
@RequestMapping("/swagger-resources")
@RequiredArgsConstructor
public class SwaggerResourceController {

    private final GatewaySwaggerResourceProvider gatewaySwaggerResourceProvider;

    @RequestMapping
    public ResponseEntity<List<SwaggerResource>> swaggerResources() {
        return new ResponseEntity<>(gatewaySwaggerResourceProvider.get(), HttpStatus.OK);
    }
}