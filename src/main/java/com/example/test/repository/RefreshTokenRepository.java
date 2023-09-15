package com.example.test.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.test.model.RefreshToken;

import jakarta.transaction.Transactional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    public Optional<RefreshToken> findById(Long iUuid);

    public boolean existsById(Long iUuid);

    @Modifying(clearAutomatically=true, flushAutomatically = true)
    @Transactional
    @Query("update RefreshToken rt set revoke = true where accessTokenId= ?1")
    void updateRevokeByAccessToken(Long idAccess);

}
