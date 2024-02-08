package com.practice.deploypractice.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        // 머스테치로 인해 문자열을 반환할 때 앞의 경로와 뒤의 확장자는 자동으로 지정.
        // 앞의 경로는 src/main/resources/templates
        // 뒤의 확장자는 .mustache
        // ViewResolver가 처리.
        return "index";
    }
}
