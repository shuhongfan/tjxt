package com.tianji.common.autoconfigure.mvc.converter;

import com.tianji.common.utils.WebUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.List;

public class WrapperResponseMessageConverter implements HttpMessageConverter<Object> {

    private final MappingJackson2HttpMessageConverter delegate;

    public WrapperResponseMessageConverter(
            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.delegate = mappingJackson2HttpMessageConverter;
    }

    @Override
    public boolean canRead(@NonNull Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(@NonNull Class<?> clazz, MediaType mediaType) {
        return WebUtils.isGatewayRequest() && delegate.canWrite(clazz, mediaType);
    }

    @Override
    @NonNull
    public List<MediaType> getSupportedMediaTypes() {
        return delegate.getSupportedMediaTypes();
    }

    @Override
    @NonNull
    public Object read(@NonNull Class<?> clazz,@NonNull HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return delegate.read(clazz, inputMessage);
    }

    @Override
    public void write(@NonNull Object o, MediaType contentType,@NonNull HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        delegate.write(o, contentType, outputMessage);
    }
}
