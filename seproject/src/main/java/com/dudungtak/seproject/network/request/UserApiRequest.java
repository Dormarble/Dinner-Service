package com.dudungtak.seproject.network.request;

import lombok.Builder;
import lombok.Getter;

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
}
