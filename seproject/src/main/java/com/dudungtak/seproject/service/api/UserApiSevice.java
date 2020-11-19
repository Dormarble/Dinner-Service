package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.User;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.Pagination;
import com.dudungtak.seproject.network.request.UserApiRequest;
import com.dudungtak.seproject.network.response.UserApiResponse;
import com.dudungtak.seproject.repository.UserRepository;
import com.dudungtak.seproject.service.BaseCrudApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserApiSevice {
    @Autowired
    private UserRepository userRepository;

    public Header<UserApiResponse> read(Long id) {
        return Optional.ofNullable(userRepository.getOne(id))
            .map(UserApiSevice::response)
            .map(Header::OK)
            .orElseGet(() -> Header.ERROR("no data"));
    }

    public Header<List<UserApiResponse>> readAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        List<UserApiResponse> userApiResponseList = users.stream()
                .map(UserApiSevice::response)
                .collect(Collectors.toList());

        Pagination pagination = BaseCrudApiService.pagination(users);

        return Header.OK(userApiResponseList, pagination);
    }

    public Header<UserApiResponse> update(Header<UserApiRequest> request) {
        UserApiRequest body = request.getData();

        return userRepository.findById(body.getId())
                .map(user -> {
                    user
                        .setEmail(body.getEmail())
                        .setPassword(body.getPassword())
                        .setName(body.getName())
                        .setGender(body.getGender())
                        .setAddress(body.getAddress())
                        .setPhoneNumber(body.getPhoneNumber());

                    return user;
                })
                .map(UserApiSevice::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("no user"));
    }

    public static UserApiResponse response(User user) {
        return UserApiResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .gender(user.getGender())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy())
                .build();
    }
}
