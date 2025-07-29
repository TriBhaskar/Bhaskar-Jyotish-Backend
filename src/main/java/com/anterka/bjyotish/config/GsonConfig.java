package com.anterka.bjyotish.config;

import com.anterka.bjyotish.constants.enums.UserRoleEnum;
import com.anterka.bjyotish.dto.users.request.AstrologerRegistrationRequest;
import com.anterka.bjyotish.dto.users.request.ClientRegistrationRequest;
import com.anterka.bjyotish.dto.users.request.UserRegistrationRequest;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Configuration
public class GsonConfig {
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(UserRegistrationRequest.class, new UserRegistrationRequestAdapter())
                .create();
    }


    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        @Override
        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.toString()); // ISO-8601 format yyyy-MM-dd
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDate.parse(json.getAsString());
        }
    }

    private static class LocalTimeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
        @Override
        public JsonElement serialize(LocalTime time, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(time.toString()); // ISO-8601 format HH:mm:ss
        }

        @Override
        public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalTime.parse(json.getAsString());
        }
    }

    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime dateTime, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(dateTime.toString()); // ISO-8601 format
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDateTime.parse(json.getAsString());
        }
    }

    private static class UserRegistrationRequestAdapter implements JsonDeserializer<UserRegistrationRequest> {
        @Override
        public UserRegistrationRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            // 1. Check if we're dealing with a nested RegistrationData structure
            if (jsonObject.has("userRegistrationRequest") && jsonObject.has("userRole")) {
                // We're inside a RegistrationData object
                String userRole = jsonObject.get("userRole").getAsString();
                return deserializeByUserRole(userRole, jsonObject.get("userRegistrationRequest"), context);
            }

            // 2. Check for explicit userRole field
            if (jsonObject.has("userRole")) {
                String userRole = jsonObject.get("userRole").getAsString();
                return deserializeByUserRole(userRole, json, context);
            }

            // 3. Look for astrologer-specific fields
            if (jsonObject.has("displayName") || jsonObject.has("specializations") ||
                    jsonObject.has("consultationFee") || jsonObject.has("yearsOfExperience")) {
                return context.deserialize(json, AstrologerRegistrationRequest.class);
            }

            // 4. Look for client-specific fields
            if (jsonObject.has("birthDetails")) {
                return context.deserialize(json, ClientRegistrationRequest.class);
            }

            // 5. Default to ClientRegistrationRequest as fallback
            return context.deserialize(json, ClientRegistrationRequest.class);
        }

        private UserRegistrationRequest deserializeByUserRole(String userRole, JsonElement json,
                                                              JsonDeserializationContext context) {
            if (UserRoleEnum.CLIENT.toString().equals(userRole)) {
                return context.deserialize(json, ClientRegistrationRequest.class);
            } else if (UserRoleEnum.ASTROLOGER.toString().equals(userRole)) {
                return context.deserialize(json, AstrologerRegistrationRequest.class);
            }

            // Default to client if role doesn't match
            return context.deserialize(json, ClientRegistrationRequest.class);
        }
    }
}
