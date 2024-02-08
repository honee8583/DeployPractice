package com.practice.deploypractice.domain.posts;

import com.practice.deploypractice.domain.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity // 기본값으로 클래스의 카멜케이스 이름을 언더스코어 네이밍(_)으로 테이블 이름을 매칭한다. (ex: SalesManager -> sales_manager)
public class Post extends BaseTimeEntity {
    // Entity 클래스에는 절대 Setter 메소드를 만들지 않는다.
    // 해당 필드의 값 변경이 필요하다면 명확히 그 목적과 의도를 나타낼 수 있는 메소드를 추가해야 한다.
    // 기본적인 구조는 생성자를 통해 최종값을 채운 후 DB에 삽입하는 것이며, 값 변경이 필요한 경우 해당 이벤트에 맞는
    // public 메소드를 호출하여 변경하는 것을 전제로 한다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)    // 데이터베이스의 자료구조를 TEXT로 지정.
    private String content;

    private String author;

    // 생성자 상단에 선언시 생성자에 포함된 필드만 빌더에 포함.
    @Builder
    public Post(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
