package com.sebastian_daschner.scalable_coffee_shop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;

public class JsonNodeResolver implements HandlerMethodArgumentResolver {

	private static final String JSONBODYATTRIBUTE = "JSON_REQUEST_BODY";

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {

		return parameter.getParameterType().equals(JsonNode.class);
	}

	@Override
	public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
	                              final WebDataBinderFactory binderFactory) throws Exception {

		if (parameter.getParameterType().equals(JsonNode.class)) {
			return objectMapper.readTree(getRequestBody(webRequest));
		}

		return null;
	}

	private String getRequestBody(NativeWebRequest webRequest) {

		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		String jsonBody = (String) servletRequest.getAttribute(JSONBODYATTRIBUTE);
		if (jsonBody == null) {
			try {
				String body = IOUtils.toString(servletRequest.getInputStream(), Charset.forName("UTF-8"));
				servletRequest.setAttribute(JSONBODYATTRIBUTE, body);
				return body;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return "";
	}
}