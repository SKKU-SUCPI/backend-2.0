package com.skku.sucpi.service.submit;

import com.skku.sucpi.dto.submit.SubmitStateDto;
import com.skku.sucpi.entity.Submit;
import com.skku.sucpi.repository.SubmitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class SubmitService {

    private final SubmitRepository submitRepository;

    public SubmitStateDto.Response updateSubmitState(SubmitStateDto.Request request) {
        Optional<Submit> optionalSubmit = submitRepository.findById(request.getId());

        if (optionalSubmit.isEmpty()) {
            throw new IllegalArgumentException("No submit id : " + request.getId());
        } else {
            Submit submit = optionalSubmit.get();
            submit.updateState(request.getState());

            return SubmitStateDto.Response.builder()
                    .id(submit.getId())
                    .state(submit.getState())
                    .build();
        }
    }
}
