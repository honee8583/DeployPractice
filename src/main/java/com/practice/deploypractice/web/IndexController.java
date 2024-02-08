package com.practice.deploypractice.web;

import com.practice.deploypractice.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final PostService postService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postService.findAllDesc());

        // 머스테치로 인해 문자열을 반환할 때 앞의 경로와 뒤의 확장자는 자동으로 지정.
        // 앞의 경로는 src/main/resources/templates
        // 뒤의 확장자는 .mustache
        // ViewResolver가 처리.

        return "index";
    }

    @GetMapping("/post/save")
    public String postSave() {
        return "post-save";
    }
}
