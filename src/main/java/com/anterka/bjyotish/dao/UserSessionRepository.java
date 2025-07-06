package com.anterka.bjyotish.dao;

import com.anterka.bjyotish.entities.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByRefreshTokenAndIsActiveTrue(String refreshToken);

    Optional<UserSession> findBySessionTokenAndIsActiveTrue(String sessionToken);

    List<UserSession> findByBjyotishUserIdAndIsActiveTrue(Long bjyotishUserId);

    @Modifying
    @Transactional
    @Query("UPDATE UserSession us SET us.isActive = false WHERE us.bjyotishUserId = :userId")
    void deactivateAllSessionsForUser(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE UserSession us SET us.isActive = false WHERE us.refreshToken = :refreshToken")
    void deactivateSessionByRefreshToken(@Param("refreshToken") String refreshToken);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserSession us WHERE us.expiresAt < :expiredTime")
    void deleteExpiredSessions(@Param("expiredTime") Instant expiredTime);

    @Query("SELECT COUNT(us) FROM UserSession us WHERE us.bjyotishUserId = :userId AND us.isActive = true")
    long countActiveSessionsForUser(@Param("userId") Long userId);
}
