package com.dudungtak.seproject.model.network.request;

import com.dudungtak.seproject.model.enumpackage.UserType;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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
