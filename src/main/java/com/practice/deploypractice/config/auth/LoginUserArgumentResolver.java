package com.practice.deploypractice.config.auth;

import com.practice.deploypractice.config.auth.dto.SessionUser;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    // 조건에 맞는 경우 메소드가 있다면 HandlerMethodArgumentResolver의 구현체가
    // 지정한 값으로 해당 메소드의 파리미터로 넘길 수 있다.

    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 컨트롤러 메소드의 특정 파라미터를 지원하는지 판단.
        // 파라미터에 @LoginUser 어노테이션이 붙어 있고,
        // 파라미터 클래스 타입이 SessionUser.class인 경우 true를 반환.
        boolean isLoginUserAnnotation  = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 파라미터에 전달할 객체를 생성.
        // 세션에서 객체를 가져옴.
        return httpSession.getAttribute("user");
    }
}
