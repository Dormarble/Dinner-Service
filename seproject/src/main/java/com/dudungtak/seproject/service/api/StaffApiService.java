package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.Staff;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.Pagination;
import com.dudungtak.seproject.network.request.StaffApiRequest;
import com.dudungtak.seproject.network.response.StaffApiResponse;
import com.dudungtak.seproject.repository.StaffRepository;
import com.dudungtak.seproject.service.BaseCrudApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffApiService {
    @Autowired
    private StaffRepository staffRepository;

    public Header<StaffApiResponse> create(Header<StaffApiRequest> request) {
        StaffApiRequest body = request.getData();

        Staff staff = Staff.builder()
                .id(body.getId())
                .name(body.getName())
                .email(body.getEmail())
                .password(body.getPassword())
                .gender(body.getGender())
                .address(body.getAddress())
                .phoneNumber(body.getPhoneNumber())
                .job(body.getJob())
                .build();

        Staff savedStaff = staffRepository.save(staff);

        return Optional.ofNullable(savedStaff)
                .map(StaffApiService::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("invalid new staff"));
    }

    public Header<StaffApiResponse> read(Long id) {
        return staffRepository.findById(id)
                .map(StaffApiService::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("no data"));
    }

    public Header<List<StaffApiResponse>> readAll(Pageable pageable) {
        Page<Staff> pageStaff = staffRepository.findAll(pageable);

        List<StaffApiResponse> staffList = pageStaff.stream()
                .map(StaffApiService::response)
                .collect(Collectors.toList());

        Pagination pagination = BaseCrudApiService.pagination(pageStaff);

        return Header.OK(staffList, pagination);
    }

    public Header<StaffApiResponse> update(Header<StaffApiRequest> request) {
        StaffApiRequest body = request.getData();

        return staffRepository.findById(body.getId())
                .map(staff -> {
                    staff
                        .setId(body.getId())
                        .setName(body.getName())
                        .setEmail(body.getEmail())
                        .setPassword(body.getPassword())
                        .setGender(body.getGender())
                        .setAddress(body.getAddress())
                        .setPhoneNumber(body.getPhoneNumber())
                        .setJob(body.getJob());

                    return staff;
                })
                .map(StaffApiService::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("no data"));
    }

    public Header delete(Long id) {
        return staffRepository.findById(id)
                .map(dish -> {
                    staffRepository.delete(dish);
                    return Header.OK("");
                })
                .orElseGet(() -> Header.ERROR("no data"));
    }


    public static StaffApiResponse response(Staff staff) {
        return StaffApiResponse.builder()
                .id(staff.getId())
                .name(staff.getName())
                .email(staff.getEmail())
                .password(staff.getPassword())
                .gender(staff.getGender())
                .address(staff.getAddress())
                .phoneNumber(staff.getPhoneNumber())
                .job(staff.getJob())
                .createdAt(staff.getCreatedAt())
                .createdBy(staff.getCreatedBy())
                .updatedAt(staff.getUpdatedAt())
                .updatedBy(staff.getUpdatedBy())
                .build();
    }
}
