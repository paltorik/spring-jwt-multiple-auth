package com.example.test.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.test.model.AccessToken;

@Repository
public interface AccessTokenRepository extends JpaRepository <AccessToken,Long> {
    Optional<AccessToken> findById(Long idLong);
}
