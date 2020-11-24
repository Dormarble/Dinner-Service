package com.dudungtak.seproject.network.request;

import com.dudungtak.seproject.enumpackage.StaffJob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffApiRequest {
    private Long id;

    private String name;

    private String email;

    private String password;

    private String gender;

    private String address;

    private String phoneNumber;

    private StaffJob job;
}
