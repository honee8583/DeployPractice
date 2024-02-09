package com.practice.deploypractice.config.auth;

import com.practice.deploypractice.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity  // Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // h2-console 화면을 사용하기 위해 해당 옵션 disable.
        http.csrf().disable()
                .headers().frameOptions().disable()
            .and()
                // URL별 권한 관리를 설정하는 옵션의 시작점.
                // authorizeRequests()가 선언되어야 antMatchers 옵션 사용 가능.
                .authorizeRequests()
                // 권한 관리 대상을 지정하는 옵션.
                // URL, HTTP 메소드 별로 관리 가능.
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**")
                .permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                // 설정된 값 외의 나머지 URL들.
                .anyRequest().authenticated()
            .and()
                // 로그아웃 성공시 "/" 주소로 이동.
                .logout()
                    .logoutSuccessUrl("/")
            .and()
                // OAuth2 로그인 기능에 대한 여러 설정의 진입점.
                .oauth2Login()
                    // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당.
                    .userInfoEndpoint()
                        // 소셜 로그인 성공시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록.
                        // 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시 가능.
                        .userService(customOAuth2UserService);
    }
}
