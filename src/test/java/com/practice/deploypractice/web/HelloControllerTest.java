package com.practice.deploypractice.web;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.practice.deploypractice.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

// 테스트를 진행할 때 JUnit에 내장된 실행자 외에 다른 실행자를 실행.(여기서는 SpringRunner 실행자를 실행)
// 스프링부트 테스트와 JUnit 사이에 연결자 역할을 한다.
@RunWith(SpringRunner.class)
// Web(Spring MVC)에 집중할 수 있는 어노테이션
// (선언할 경우 WebSecurityConfigurerAdapter, WebMvcConfigurer, @Controller, @ControllerAdvice등을 읽을 수 있다.
// 단, @Service, @Component, @Repository 등은 사용불가.) 여기서는 컨트롤러만 사용하기 때문에 사용.
// SecurityConfig를 읽더라도 내부의 CustomOAuth2UserService는 읽을 수 없기 때문에 스캔대상에서 SecurityConfig를 제거.
@WebMvcTest(controllers = HelloController.class,
    excludeFilters = {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
    }
)
public class HelloControllerTest {
    // !! @EnableJpaAuditing이 @SpringBootApplication과 함께 있을 경우 @WebMvcTest에서도 @EnableJpaAuditing을 읽게됨.
    // @EnableJpaAuditing은 최소 하나의 @Entity 클래스가 필요하지만 @WebMvcTest에서는 읽지를 못함.
    // 따라서 둘을 분리해줘야 함.

    // 웹 API를 테스트할 때 사용.
    // 스프링 MVC 테스트의 시작점.
    // 이 클래스를 통해 HTTP GET, POST 등에 대한 API 테스트 가능.
    @Autowired
    private MockMvc mvc;

    @WithMockUser(roles = "USER")
    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "CI/CD TEST";

        // MockMvc를 통해 /hello 주소로 HTTP GET 요청 수행.
        // 여러 검증을 이어서 선언 가능.
        mvc.perform(get("/hello"))
                // mvc.perform의 결과를 검증.
                // HTTP Header의 Status를 검증. (200, 400, 500 등의 상태를 검증.)
                .andExpect(status().isOk())
                // mvc.perform의 결과를 검증.
                // 응답 본문의 내용을 검증.
                // Controller에서 "hello"를 리턴하기 때문에 이 값이 맞는지 검증.
                .andExpect(content().string(hello));
    }

    @WithMockUser(roles = "USER")
    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                        // API 테스트할 때 사용될 요청 파라미터를 설정. (단, 값은 String만 허용한다.)
                        .param("name", name)
                        .param("amount", String.valueOf(amount))
        ).andExpect(status().isOk())
                // jsonPath: JSON 응답값을 필드별로 검증할 수 있는 메소드.
                // $를 기준으로 필드명을 명시.
        .andExpect(jsonPath("$.name", is(name)))
        .andExpect(jsonPath("$.amount", is(amount)));
    }
}