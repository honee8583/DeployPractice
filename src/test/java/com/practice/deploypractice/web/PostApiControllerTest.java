package com.practice.deploypractice.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.practice.deploypractice.domain.posts.Post;
import com.practice.deploypractice.domain.posts.PostRepository;
import com.practice.deploypractice.web.dto.PostResponseDto;
import com.practice.deploypractice.web.dto.PostSaveRequestDto;
import com.practice.deploypractice.web.dto.PostUpdateRequestDto;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMethod;

@RunWith(SpringRunner.class)
// @WebMvcTest의 경우 JPA기능이 작동하지 않고 Controller와 ControllerAdvice 등 외부 연동과 관련된 부분만 활성화되므로 여기서는 사용 x.
// 지금같이 JPA 기능까지 한번에 테스트할 때는 @SpringBootTest와 TestRestTemplate을 사용.
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PostApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostRepository postRepository;

    @After
    public void tearDown() throws Exception {
        postRepository.deleteAll();
    }

    @Test
    public void Post_등록된다() {
        // given
        String title = "title";
        String content = "content";
        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/post";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Post> posts = postRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo(title);
        assertThat(posts.get(0).getContent()).isEqualTo(content);
    }

    @Test
    public void Post_수정된다() {
        // given
        Post savedPost = postRepository.save(Post.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long updateId = savedPost.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostUpdateRequestDto requestDto = PostUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/post/" + updateId;

        // HTTP 요청을 보낼 때 요청 바디에 데이터를 담기 위해 사용되는 HttpEntity 객체를 생성
        HttpEntity<PostUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
        // postForEntity()와 유사하지만 요청과 응답에 대한 헤더 및 상태 코드를 세세하게 제어할 수 있다.
        // url(요청을 보낼 url), method(HTTP요청 메소드), requestEntity(요청 헤더와 바디를 포함하는 HttpEntity객체), responseType(응답을 받을 데이터 타입)
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Post> posts = postRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(posts.get(0).getContent()).isEqualTo(expectedContent);
    }

    @Test
    public void Post_조회한다() {
        // given
        String title = "title";
        String content = "content";
        String author = "author";
        Post savedPost = postRepository.save(Post.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());

        Long id = savedPost.getId();

        String url = "http://localhost:" + port + "/api/v1/post/" + id;

        // when
        ResponseEntity<PostResponseDto> responseEntity = restTemplate.getForEntity(url, PostResponseDto.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody().getTitle()).isEqualTo(title);
        assertThat(responseEntity.getBody().getContent()).isEqualTo(content);
        assertThat(responseEntity.getBody().getAuthor()).isEqualTo(author);
    }

    @Test
    public void Post_삭제된다() {
        // given
        Post savedPost = postRepository.save(Post.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long deleteId = savedPost.getId();

        String url = "http://localhost:" + port + "/api/v1/post/" + deleteId;

        HttpEntity<Long> requestEntity = new HttpEntity<>(deleteId);

        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(deleteId);
    }
}