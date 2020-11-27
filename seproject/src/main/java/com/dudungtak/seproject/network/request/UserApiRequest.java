package com.dudungtak.seproject.network.request;

import com.dudungtak.seproject.enumpackage.UserType;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Builder
public class UserApiRequest {
    private Long id;

    private String email;

    private String password;

    private String name;

    private String gender;

    private String address;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserType type;
}
