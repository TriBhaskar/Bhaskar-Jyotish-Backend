package com.anterka.bjyotish.dao;

import com.anterka.bjyotish.entities.BjyotishUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BjyotishUserRepository extends JpaRepository<BjyotishUser, Long> {
    Optional<BjyotishUser> findByEmail(String email);
    Optional<BjyotishUser> findByPhone(String phone);

}
