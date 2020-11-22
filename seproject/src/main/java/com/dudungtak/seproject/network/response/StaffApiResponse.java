package com.dudungtak.seproject.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffApiResponse {
    private Long id;

    private String name;

    private String email;

    private String password;

    private String gender;

    private String address;

    private String phoneNumber;

    private String job;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;
}
