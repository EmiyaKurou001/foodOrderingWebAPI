package com.foodordering.repository;

import com.foodordering.model.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    
    Optional<Account> findByUsername(String username);
    
    Optional<Account> findByEmail(String email);
    
    List<Account> findByActiveTrue();
    
    List<Account> findByRolesContaining(String role);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}

