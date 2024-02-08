package com.practice.deploypractice.domain.posts;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    // Entity 클래스와 기본 Entity Repository는 함께 위치해야 한다.

    @Query("SELECT p FROM Post p ORDER BY p.id DESC")
    List<Post> findAllDesc();
}
