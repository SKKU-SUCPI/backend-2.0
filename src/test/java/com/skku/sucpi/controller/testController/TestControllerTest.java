package com.skku.sucpi.controller.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skku.sucpi.dto.test.TestRequestDto;
import com.skku.sucpi.dto.test.TestResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // get test
    @Test
    void test() throws Exception{
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }

    // post test
    @Test
    void saveTest() throws Exception{
        // given
        String title = "title";
        String content = "content";

        TestRequestDto requestDto = new TestRequestDto();
        requestDto.setTitle(title);
        requestDto.setContent(content);

        String json = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions result = mockMvc.perform(post("/test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // then
        String contentAsString = result.andReturn().getResponse().getContentAsString();
        TestResponseDto responseDto = objectMapper.readValue(contentAsString, TestResponseDto.class);

        assertThat(responseDto.getId()).isGreaterThan(0L);
        assertThat(responseDto.getTitle()).isEqualTo(title);
        assertThat(responseDto.getContent()).isEqualTo(content);
    }

}
