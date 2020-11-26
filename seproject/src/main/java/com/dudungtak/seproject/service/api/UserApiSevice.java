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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserApiSevice {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserApiSevice(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Header create(Header<UserApiRequest> request) {
        UserApiRequest body = request.getData();

        User user = User.builder()
                .name(body.getName())
                .email(body.getEmail())
                .password(body.getPassword())
                .gender(body.getGender())
                .address(body.getAddress())
                .phoneNumber(body.getPhoneNumber())
                .build();

        if(!UserApiSevice.isValidUser(user))
            return Header.ERROR("fill all required field");

        Optional<User> optionalUser = register(user);
        if(optionalUser.isPresent())
            return Header.OK();
        return Header.ERROR("existing user id");
    }

    private Optional<User> register(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        if(optionalUser.isPresent()) {
            return Optional.empty();
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);

        return Optional.ofNullable(savedUser);
    }

    public Optional<User> authenticate(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            if(passwordEncoder.matches(password, user.getPassword()))
                return optionalUser;
        }

        return Optional.empty();
    }

    public Header<UserApiResponse> read(Long id) {
        return userRepository.findById(id)
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

    public static boolean isValidUser(User user) {
        String name = user.getName();
        String email = user.getEmail();
        String password = user.getPassword();
        String address = user.getAddress();
        String phoneNumber = user.getPhoneNumber();

        return !Stream.of(name, email, password, address, phoneNumber)
                .anyMatch(Objects::isNull);
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
