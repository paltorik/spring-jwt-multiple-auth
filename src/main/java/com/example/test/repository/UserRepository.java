package com.example.test.repository;

import com.example.test.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
    public Optional <User> findById(Long $Id);

    public Optional <User> findByPhone(String $phone);

    public Optional <User> findByEmail(String $email);

    public Optional<User> findByUsername(String username);

    public boolean existsByEmail(String email);
}