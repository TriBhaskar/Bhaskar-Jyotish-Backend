package com.anterka.bjyotish.service;

import com.anterka.bjyotish.dto.users.UserRegistrationRequest;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RegistrationCacheService {

    private final JedisPooled client;
    private final Gson gson;

    private static final String REGISTRATION_PREFIX = "registration_";
    private static final long REGISTRATION_VALIDITY_SECONDS = TimeUnit.HOURS.toSeconds(2);

    public void saveRegistration(String email, UserRegistrationRequest registrationRequest) {
        String key = REGISTRATION_PREFIX + email;
        client.jsonSet(key, gson.toJson(registrationRequest));
        client.expire(key, REGISTRATION_VALIDITY_SECONDS);
    }

    public UserRegistrationRequest getRegistration(String email) {
        String key = REGISTRATION_PREFIX + email;
        return client.jsonGet(key, UserRegistrationRequest.class);
    }

    public void deleteRegistration(String email) {
        String key = REGISTRATION_PREFIX + email;
        client.del(key);
    }
}
