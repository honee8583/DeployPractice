package com.practice.deploypractice.config.auth.dto;

import com.practice.deploypractice.domain.user.User;
import java.io.Serializable;
import lombok.Getter;

// User엔티티 클래스를 그대로 사용했다면 User 클래스에 직렬화를 구현하지 않았다는 의미의 에러 발생.
// User 클래스는 엔티티이기 때문에 직렬화 코드를 넣으면 안됌.
// 엔티티 클래스가 연관관계를 가지게 된다면 직렬화 대상에 자식엔티티까지 포함되므로 성능 이슈, 부수 효과가 발생.
// 직렬화 기능을 가진 세션 Dto를 하나 추가로 만드는 것이 좋음.
@Getter
public class SessionUser implements Serializable {
    // 인증된 사용자 정보만 필요.
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
