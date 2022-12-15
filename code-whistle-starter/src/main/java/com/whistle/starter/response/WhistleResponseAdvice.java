package com.whistle.starter.response;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Gentvel
 */
@Slf4j
@RestControllerAdvice
public class WhistleResponseAdvice implements ResponseBodyAdvice<Object> {

    private static final int DEFAULT_MAX_PAYLOAD_LENGTH = 10000;
    private static final String REQUEST_MESSAGE_PREFIX = "Request [";
    private static final String REQUEST_MESSAGE_SUFFIX = "]";
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public boolean supports(@NotNull MethodParameter returnType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, @NotNull MethodParameter returnType, @NotNull MediaType selectedContentType, @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response) {
        ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;

        String requestURI = servletServerHttpRequest.getServletRequest().getRequestURI();
        if(requestURI.contains("swagger-resources")
                ||requestURI.contains("api-docs")
                 ||requestURI.contains("static")){
            return body;
        }
        Object object;
        if (body == null) {
            object = (Collections.emptyMap());
        } else if (Result.class.isAssignableFrom(body.getClass())) {
            object = body;
        } else if (checkPrimitive(body)) {
            return Result.ok(Collections.singletonMap("result", body));
        } else {
            object = body;
        }
        log.info("{} Response [{}]",createRequestMessage(servletServerHttpRequest.getServletRequest()),objectMapper.writeValueAsString(object));
        return object;
    }


    private boolean checkPrimitive(Object body) {
        Class<?> clazz = body.getClass();
        return clazz.isPrimitive()
                || clazz.isArray()
                || Collection.class.isAssignableFrom(clazz)
                || body instanceof Number
                || body instanceof Boolean
                || body instanceof Character
                || body instanceof String;
    }


    protected String createRequestMessage(HttpServletRequest request) {
        StringBuilder msg = new StringBuilder();
        msg.append(WhistleResponseAdvice.REQUEST_MESSAGE_PREFIX);
        msg.append(request.getMethod()).append(" ");
        msg.append(request.getRequestURI());
        String queryString = request.getQueryString();
        if (StrUtil.isNotBlank(queryString)) {
            msg.append('?').append(queryString);
        }
        String client = getIpAddr(request);
        if (StringUtils.hasLength(client)) {
            msg.append(", client=").append(client);
        }
        HttpHeaders headers = new ServletServerHttpRequest(request).getHeaders();
        msg.append(", token=").append(headers.get("token"));
        String payload = getMessagePayload(request);
        if (StrUtil.isNotBlank(payload)) {
            msg.append(", payload=").append(payload);
        }
        msg.append(WhistleResponseAdvice.REQUEST_MESSAGE_SUFFIX);
        return msg.toString();
    }

    protected String getMessagePayload(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper =
                WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        String payload="";
        if(ContentType.MULTIPART.getValue().equals(request.getContentType())){
            return payload;
        }
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, DEFAULT_MAX_PAYLOAD_LENGTH);

                try {
                    payload=new String(buf, 0, length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    payload= "[unknown]";
                }
                return payload.replaceAll("\\n", "");
            }
        }
        return "";
    }


    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(ip.lastIndexOf(",") + 1).trim();
        }
        return ip;
    }
}
