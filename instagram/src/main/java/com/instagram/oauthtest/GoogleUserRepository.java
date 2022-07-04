package com.instagram.oauthtest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface GoogleUserRepository extends JpaRepository<GoogleUser, Long> {

    Optional<GoogleUser> findByEmail(String email);
}
