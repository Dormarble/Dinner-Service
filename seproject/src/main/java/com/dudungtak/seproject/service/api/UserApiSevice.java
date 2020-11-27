package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.component.JwtUtil;
import com.dudungtak.seproject.entity.User;
import com.dudungtak.seproject.enumpackage.UserType;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.Pagination;
import com.dudungtak.seproject.network.request.UserApiRequest;
import com.dudungtak.seproject.network.response.UserApiResponse;
import com.dudungtak.seproject.repository.UserRepository;
import com.dudungtak.seproject.service.BaseCrudApiService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
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

    private final JwtUtil jwtUtil;

    @Autowired
    public UserApiSevice(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Header create(Authentication authentication, Header<UserApiRequest> request) {
        UserApiRequest body = request.getData();

        User user = User.builder()
                .name(body.getName())
                .email(body.getEmail())
                .password(body.getPassword())
                .gender(body.getGender())
                .address(body.getAddress())
                .phoneNumber(body.getPhoneNumber())
                .type(body.getType())
                .build();

        if(!UserApiSevice.isValidUser(user))
            return Header.ERROR("fill all required field(name, email, password, address, phone_number, type)");

        Optional<User> optionalUser = register(user);
        if(optionalUser.isPresent())
            return Header.OK();
        return Header.ERROR("existing user id");
    }

    public Header signIn(Authentication authentication, Header<UserApiRequest> request) {
        UserApiRequest body = request.getData();

        String email = body.getEmail();
        String password = body.getPassword();

        Optional<User> optionalUser = authenticate(email, password);
        return optionalUser
                .map(user -> {
                    String token = jwtUtil.createToken(user.getId(), user.getName(), user.getType());
                    return Header.OK(token);
                })
                .orElseGet(() -> Header.ERROR("fail to sign in"));
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

    public Header<UserApiResponse> read(Authentication authentication, Long neverUsed) {
        Claims claims = (Claims)authentication.getPrincipal();
        Long id = claims.get("id", Long.class);
        System.out.println(id);

        return userRepository.findById(id)
            .map(UserApiSevice::response)
            .map(Header::OK)
            .orElseGet(() -> Header.ERROR("no data"));
    }

    public Header<List<UserApiResponse>> readAll(Authentication authentication, Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        List<UserApiResponse> userApiResponseList = users.stream()
                .map(UserApiSevice::response)
                .collect(Collectors.toList());

        Pagination pagination = BaseCrudApiService.pagination(users);

        return Header.OK(userApiResponseList, pagination);
    }

    public Header<UserApiResponse> update(Authentication authentication, Header<UserApiRequest> request) {
        UserApiRequest body = request.getData();

        Claims claims = (Claims)authentication.getPrincipal();
        Long id = claims.get("id", Long.class);

        return userRepository.findById(id)
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
        UserType userType = user.getType();

        return !Stream.of(name, email, password, address, phoneNumber, userType)
                .anyMatch(Objects::isNull);
    }

    public static UserApiResponse response(User user) {
        return UserApiResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .gender(user.getGender())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .type(user.getType())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy())
                .build();
    }
}
