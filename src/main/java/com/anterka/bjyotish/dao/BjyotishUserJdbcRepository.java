package com.anterka.bjyotish.dao;

import com.anterka.bjyotish.entities.BjyotishUserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BjyotishUserJdbcRepository {
    private final JdbcClient jdbcClient;

    private static final String SELECT_BY_EMAIL = """
        SELECT id, email, phone, password_hash, first_name, last_name, 
               date_of_birth, gender, role, status, profile_image_url, 
               email_verified, phone_verified, last_login_at, created_at, updated_at 
        FROM bjyotish_users WHERE email = :email
        """;

    public Optional<BjyotishUserRecord> findById(Long id) {
        return jdbcClient.sql("SELECT * FROM bjyotish_users WHERE id = :id")
                .param("id", id)
                .query(BjyotishUserRecord.class)
                .optional();
    }
    public Optional<BjyotishUserRecord> findByEmail(String email) {
        return jdbcClient.sql(SELECT_BY_EMAIL)
                .param("email", email)
                .query(BjyotishUserRecord.class)
                .optional();
    }

    public Optional<BjyotishUserRecord> findByPhone(String phone) {
        return jdbcClient.sql("SELECT * FROM bjyotish_users WHERE phone = :phone")
                .param("phone", phone)
                .query(BjyotishUserRecord.class)
                .optional();
    }

    public Optional<BjyotishUserRecord> findByEmailOrPhone(String email, String phone) {
        return jdbcClient.sql("SELECT * FROM bjyotish_users WHERE email = :email OR phone = :phone")
                .param("email", email)
                .param("phone", phone)
                .query(BjyotishUserRecord.class)
                .optional();
    }
}
