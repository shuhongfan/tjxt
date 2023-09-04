package com.tianji.common.autoconfigure.swagger;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.tianji.common.domain.R;
import com.tianji.common.utils.CollUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseStatus;
import springfox.documentation.schema.property.ModelSpecificationFactory;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ResponseContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;

import java.util.Optional;

import static com.github.xiaoymin.knife4j.spring.util.TypeUtils.isVoid;

public class BaseSwaggerResponseBuilderPlugin implements OperationBuilderPlugin, Ordered {

    @Autowired
    private TypeResolver typeResolver;
    @Autowired
    private DocumentationPluginsManager documentationPlugins;
    @Autowired
    private ModelSpecificationFactory modelSpecifications;

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE+13;
    }

    @Override
    public void apply(OperationContext context) {
        // 1.处理返回值类型
        ResolvedType resolvedType = isVoid(context.getReturnType()) ?
                typeResolver.resolve(R.class) : typeResolver.resolve(R.class, context.getReturnType());
        ResolvedType returnType = context.alternateFor(resolvedType);
        // 2.处理状态码为ok
        int httpStatusCode = HttpStatus.OK.value();
        // 3.处理message
        String message = message(context);
        // 4.处理响应类型
        ModelContext modelContext = context.operationModelsBuilder().addReturn(returnType);
        // 5.响应结果
        ResponseContext responseContext = new ResponseContext(context.getDocumentationContext(), context);
        responseContext.responseBuilder()
                .representation(MediaType.APPLICATION_JSON)
                .apply(r -> r.model(m -> {
                    m.copyOf(modelSpecifications.create(modelContext, returnType));
                    m.name("R");
                    m.build();
                }))
                .description(message)
                .code(String.valueOf(httpStatusCode));
        context.operationBuilder()
                .responses(CollUtils.singletonList(documentationPlugins.response(responseContext)));
    }

    public static String message(OperationContext context) {
        Optional<ResponseStatus> responseStatus = context.findAnnotation(ResponseStatus.class);
        String reasonPhrase = HttpStatus.OK.getReasonPhrase();
        if (responseStatus.isPresent()) {
            reasonPhrase = responseStatus.get().reason();
            if (reasonPhrase.isEmpty()) {
                reasonPhrase = responseStatus.get().value().getReasonPhrase();
            }
        }
        return reasonPhrase;
    }
}

