package com.skku.sucpi.repository;

import com.skku.sucpi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByHakbun(String hakbun);
}
