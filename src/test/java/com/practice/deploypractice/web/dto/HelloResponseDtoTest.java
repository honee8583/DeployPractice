package com.practice.deploypractice.web.dto;

import static org.junit.Assert.*;

import org.junit.Test;

public class HelloResponseDtoTest {

    @Test
    public void 롬복_기능_테스트() {
        // given
        String name = "test";
        int amount = 1000;

        // when
        HelloResponseDto dto = new HelloResponseDto(name, amount);

        // then
        // assertJ라는 테스트 검증 라이브러리의 검증 메소드.
        assertEquals(dto.getName(), name);
        assertEquals(dto.getAmount(), amount);
    }
}