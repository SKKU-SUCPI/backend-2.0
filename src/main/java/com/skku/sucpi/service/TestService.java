package com.skku.sucpi.service;

import com.skku.sucpi.dto.test.TestRequestDto;
import com.skku.sucpi.dto.test.TestResponseDto;
import com.skku.sucpi.entity.TestEntity;
import com.skku.sucpi.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    @Transactional
    public TestResponseDto save(TestRequestDto requestDto) {
        TestEntity test = TestEntity.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        TestEntity result = testRepository.save(test);

        return TestResponseDto.builder()
                .id(result.getId())
                .title(result.getTitle())
                .content(result.getContent()).build();
    }
}
