package com.dudungtak.seproject.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Header<T> {
    private LocalDateTime transactionTime;

    private String resultCode;

    private String description;

    private String connectionId;

    private T data;

    public static <T> Header<T> OK(String connectionId) {
        return (Header<T>)Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("200")
                .description("OK")
                .connectionId(connectionId)
                .build();
    }

    public static <T> Header<T> OK(T data, String connectionId) {
        return (Header<T>)Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("200")
                .description("OK")
                .connectionId(connectionId)
                .data(data)
                .build();
    }

    public static <T> Header<T> ERROR(String message, String connectionId) {
        return (Header<T>)Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("200")
                .description(message)
                .connectionId(connectionId)
                .build();
    }
}
