package com.practice.deploypractice.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.deploypractice.domain.posts.Post;
import com.practice.deploypractice.domain.posts.PostRepository;
import com.practice.deploypractice.web.dto.PostSaveRequestDto;
import com.practice.deploypractice.web.dto.PostUpdateRequestDto;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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

    // @WithMockUser가 MockMvc에서만 작동하기 때문에 MockMvc를 사용해서 테스트해야 함. (RestTemplate 사용 x)
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() {
        // 매번 테스트가 시작되건에 MockMvc 인스턴스를 생성.
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles="USER") // 스프링 시큐리티를 적용하게 될경우 임의로 인증된 사용자를 추가하여 API만 테스트해야 함. (가짜 사용자를 만들어서 사용)
    public void Post_등록된다() throws Exception {
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
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                // 본문 영역은 문자열로 표현하기 위해 ObjectMapper를 통해 문자열 JSON으로 변환.
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Post> posts = postRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo(title);
        assertThat(posts.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles="USER")
    public void Post_수정된다() throws Exception {
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
//        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Post> posts = postRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(posts.get(0).getContent()).isEqualTo(expectedContent);
    }

    @Test
    @WithMockUser(roles="USER")
    public void Post_조회한다() throws Exception {
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
//        ResponseEntity<PostResponseDto> responseEntity = restTemplate.getForEntity(url, PostResponseDto.class);
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.author").value("author"))
                .andExpect(status().isOk());

        // then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Post post = postRepository.findById(id).get();
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getAuthor()).isEqualTo(author);
    }

    @Test
    @WithMockUser(roles="USER")
    public void Post_삭제된다() throws Exception {
        // given
        Post savedPost = postRepository.save(Post.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long deleteId = savedPost.getId();

        String url = "http://localhost:" + port + "/api/v1/post/" + deleteId;

//        HttpEntity<Long> requestEntity = new HttpEntity<>(deleteId);

        // when
//        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Long.class);
        mvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(deleteId)))
                .andExpect(status().isOk());

        // then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isEqualTo(deleteId);

        Optional<Post> post = postRepository.findById(deleteId);
        assertThat(post).isNotPresent();
    }
}