package com.anterka.bjyotish.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomApiResponse {
    private String message;
    private ResponseStatusEnum status;
    private LocalDateTime timestamp;
}
