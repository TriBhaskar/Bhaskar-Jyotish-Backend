package com.anterka.bjyotish.dao;

import com.anterka.bjyotish.entities.UserSessionRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserSessionJdbcRepository {
    private final JdbcClient jdbcClient;

    private static final String SELECT_BY_SESSION_TOKEN = """
        SELECT id, bjyotish_user_id, session_token, refresh_token, expires_at, 
               is_active, ip_address, user_agent, created_at, updated_at
        FROM user_sessions 
        WHERE session_token = :sessionToken AND is_active = true
        """;

    private static final String SELECT_BY_REFRESH_TOKEN = """
        SELECT id, bjyotish_user_id, session_token, refresh_token, expires_at, 
               is_active, ip_address, user_agent, created_at, updated_at
        FROM user_sessions 
        WHERE refresh_token = :refreshToken AND is_active = true
        """;

    private static final String SELECT_BY_USER_ID = """
        SELECT id, bjyotish_user_id, session_token, refresh_token, expires_at, 
               is_active, ip_address, user_agent, created_at, updated_at
        FROM user_sessions 
        WHERE bjyotish_user_id = :userId AND is_active = true
        """;

    public Optional<UserSessionRecord> findByRefreshTokenAndIsActiveTrue(String refreshToken) {
        return jdbcClient.sql(SELECT_BY_REFRESH_TOKEN)
                .param("refreshToken", refreshToken)
                .query(UserSessionRecord.class)
                .optional();
    }

    public Optional<UserSessionRecord> findBySessionTokenAndIsActiveTrue(String sessionToken) {
        return jdbcClient.sql(SELECT_BY_SESSION_TOKEN)
                .param("sessionToken", sessionToken)
                .query(UserSessionRecord.class)
                .optional();
    }

    public List<UserSessionRecord> findByBjyotishUserIdAndIsActiveTrue(Long bjyotishUserId) {
        return jdbcClient.sql(SELECT_BY_USER_ID)
                .param("userId", bjyotishUserId)
                .query(UserSessionRecord.class)
                .list();
    }

    public void deactivateAllSessionsForUser(Long userId) {
        jdbcClient.sql("UPDATE user_sessions SET is_active = false WHERE bjyotish_user_id = :userId")
                .param("userId", userId)
                .update();
    }

    public void deactivateSessionByRefreshToken(String refreshToken) {
        jdbcClient.sql("UPDATE user_sessions SET is_active = false WHERE refresh_token = :refreshToken")
                .param("refreshToken", refreshToken)
                .update();
    }

    public void deleteExpiredSessions(Instant expiredTime) {
        jdbcClient.sql("DELETE FROM user_sessions WHERE expires_at < :expiredTime")
                .param("expiredTime", expiredTime)
                .update();
    }

    public long countActiveSessionsForUser(Long userId) {
        return jdbcClient.sql("SELECT COUNT(*) FROM user_sessions WHERE bjyotish_user_id = :userId AND is_active = true")
                .param("userId", userId)
                .query(Long.class)
                .single();
    }

    public UserSessionRecord save(UserSessionRecord session) {
        if (session.id() == null) {
            return insert(session);
        } else {
            return update(session);
        }
    }

    private UserSessionRecord insert(UserSessionRecord session) {
        Long id = jdbcClient.sql("""
            INSERT INTO user_sessions(bjyotish_user_id, session_token, refresh_token, expires_at, 
                                     is_active, ip_address, user_agent, created_at, updated_at)
            VALUES(:bjyotishUserId, :sessionToken, :refreshToken, :expiresAt, 
                   :isActive, :ipAddress, :userAgent, :createdAt, :updatedAt)
            RETURNING id
            """)
                .param("bjyotishUserId", session.bjyotishUserId())
                .param("sessionToken", session.sessionToken())
                .param("refreshToken", session.refreshToken())
                .param("expiresAt", java.sql.Timestamp.from(Instant.now()))
                .param("isActive", session.isActive())
                .param("ipAddress", session.ipAddress(),Types.OTHER)
                .param("userAgent", session.userAgent())
                .param("createdAt", java.sql.Timestamp.from(Instant.now()))
                .param("updatedAt", java.sql.Timestamp.from(Instant.now()))
                .query(Long.class)
                .single();

        return new UserSessionRecord(
                id,
                session.bjyotishUserId(),
                session.sessionToken(),
                session.refreshToken(),
                session.expiresAt(),
                session.isActive(),
                session.ipAddress(),
                session.userAgent(),
                session.createdAt() != null ? session.createdAt() : Instant.now(),
                session.updatedAt() != null ? session.updatedAt() : Instant.now()
        );
    }

    private UserSessionRecord update(UserSessionRecord session) {
        jdbcClient.sql("""
            UPDATE user_sessions
            SET bjyotish_user_id = :bjyotishUserId, 
                session_token = :sessionToken,
                refresh_token = :refreshToken,
                expires_at = :expiresAt,
                is_active = :isActive,
                ip_address = :ipAddress,
                user_agent = :userAgent,
                updated_at = :updatedAt
            WHERE id = :id
            """)
                .param("id", session.id())
                .param("bjyotishUserId", session.bjyotishUserId())
                .param("sessionToken", session.sessionToken())
                .param("refreshToken", session.refreshToken())
                .param("expiresAt", java.sql.Timestamp.from(session.expiresAt()))
                .param("isActive", session.isActive())
                .param("ipAddress", session.ipAddress(),Types.OTHER)
                .param("userAgent", session.userAgent())
                .param("updatedAt", java.sql.Timestamp.from(Instant.now()))
                .update();

        return new UserSessionRecord(
                session.id(),
                session.bjyotishUserId(),
                session.sessionToken(),
                session.refreshToken(),
                session.expiresAt(),
                session.isActive(),
                session.ipAddress(),
                session.userAgent(),
                session.createdAt(),
                Instant.now()
        );
    }

    public void deleteById(Long id) {
        jdbcClient.sql("DELETE FROM user_sessions WHERE id = :id")
                .param("id", id)
                .update();
    }
}
