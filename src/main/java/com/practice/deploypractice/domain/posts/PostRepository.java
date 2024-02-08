package com.practice.deploypractice.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    // Entity 클래스와 기본 Entity Repository는 함께 위치해야 한다.
}
