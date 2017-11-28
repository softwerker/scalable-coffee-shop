package com.sebastian_daschner.scalable_coffee_shop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class JsonNodeResolver implements HandlerMethodArgumentResolver {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {

		return parameter.getParameterType().equals(JsonNode.class);
	}

	@Override
	public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
	                              final WebDataBinderFactory binderFactory) throws Exception {

		if (parameter.getParameterType().equals(JsonNode.class)) {
			mavContainer.getModel();
			return objectMapper.readTree("");
		}
		return null;
	}

}
