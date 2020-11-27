package com.dudungtak.seproject.network.response;

import com.dudungtak.seproject.enumpackage.UserType;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserApiResponse {
    private Long id;

    private String email;

    private String name;

    private String gender;

    private String address;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserType type;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;
}
