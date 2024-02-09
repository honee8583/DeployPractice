package com.practice.deploypractice.domain.posts;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest    // 내장 데이터베이스를 사용하고 JPA와 관련된 빈만 로드.
//@SpringBootTest // 별다른 설정이 없을 경우 H2 데이터베이스를 자동으로 수행.
public class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    // Junit에서 단위 테스트가 끝날때마다 수행되는 메소드를 지정.
    // 여러 테스트가 동시에 수행되면 H2 데이터베이스의 데이터가 그대로 남아 있어 다음 테스트 실행시 실패 가능성 있음.
    @After
    public void cleanUp() {
        postRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        // given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postRepository.save(Post.builder()
                .title(title)
                .content(content)
                .author("ekgns99@gmail.com")
                .build());

        // when
        List<Post> posts = postRepository.findAll();

        // then
        Post post = posts.get(0);
        assertEquals(post.getTitle(), title);
        assertEquals(post.getContent(), content);
    }

    @Test
    public void BaseTimeEntity_등록() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 2, 7, 0, 0, 0);
        postRepository.save(Post.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        // when
        List<Post> posts = postRepository.findAll();

        // then
        Post post = posts.get(0);

        System.out.println(">>>> creatDate=" + post.getCreatedDate() + ", modifiedDate=" + post.getModifiedDate());

        assertThat(post.getCreatedDate()).isAfter(now);
        assertThat(post.getModifiedDate()).isAfter(now);
    }

    @Test
    public void findAllDesc_테스트() {
        // given
        Post savedPost1 = postRepository.save(postRepository.save(Post.builder()
                .title("title1")
                .content("content1")
                .author("author1")
                .build()));

        Post savedPost2 = postRepository.save(postRepository.save(Post.builder()
                .title("title2")
                .content("content2")
                .author("author2")
                .build()));

        // when
        List<Post> posts = postRepository.findAllDesc();

        // then
        assertThat(posts).isNotEmpty();
        assertThat(posts.size()).isEqualTo(2);
        assertThat(posts).contains(savedPost1, savedPost2);
    }
}