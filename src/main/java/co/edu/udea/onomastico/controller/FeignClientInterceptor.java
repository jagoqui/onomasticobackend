package co.edu.udea.onomastico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import co.edu.udea.onomastico.security.JwtTokenProvider;
import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

  private static final String AUTHORIZATION_HEADER = "Authorization";

  public static String getBearerTokenHeader() {
    return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization").substring(7);
  }

  @Override
  public void apply(RequestTemplate requestTemplate) {
      requestTemplate.header(AUTHORIZATION_HEADER, getBearerTokenHeader());
  }
}