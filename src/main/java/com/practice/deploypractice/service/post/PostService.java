package com.practice.deploypractice.service.post;

import com.practice.deploypractice.domain.posts.Post;
import com.practice.deploypractice.domain.posts.PostRepository;
import com.practice.deploypractice.web.dto.PostListResponseDto;
import com.practice.deploypractice.web.dto.PostResponseDto;
import com.practice.deploypractice.web.dto.PostSaveRequestDto;
import com.practice.deploypractice.web.dto.PostUpdateRequestDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public Long save(PostSaveRequestDto requestDto) {
        return postRepository.save(requestDto.toEntity()).getId();
    }

    public PostResponseDto findById(Long id) {
        Post entity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostResponseDto(entity);
    }

    @Transactional
    public Long update(Long id, PostUpdateRequestDto requestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        post.update(requestDto.getTitle(), requestDto.getContent());    // 더티 체킹(Dirty Checking)

        return id;
    }

    // readOnly=true : 트랜잭션 범위는 유지하되, 조회 기능만 남겨두오 조회속도가 개선됨.
    // 등록, 수정, 삭제 기능이 전혀 없는 서비스 메소드에서 사용 권장.
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAllDesc() {
        return postRepository.findAllDesc().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        postRepository.delete(post);
    }
}
