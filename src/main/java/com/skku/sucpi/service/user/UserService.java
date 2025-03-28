package com.skku.sucpi.service.user;

import com.skku.sucpi.dto.user.SSOUserDto;
import com.skku.sucpi.entity.User;
import com.skku.sucpi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getOrCreateUser(SSOUserDto ssoUserDto) {
        return userRepository.findByHakbun(ssoUserDto.getHakbun())
                .orElseGet(() -> {
                    User newUser = new User(ssoUserDto);
                    return userRepository.save(newUser);
        });
    }
}
