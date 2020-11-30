package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.exception.BadInputException;
import com.dudungtak.seproject.exception.ExistedUserException;
import com.dudungtak.seproject.exception.LoginFailedException;
import com.dudungtak.seproject.util.JwtUtil;
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
public class UserApiService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserApiService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Header signUp(Header<UserApiRequest> request) {
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

        if(!UserApiService.isValidUser(user))
            throw new BadInputException();

        Optional<User> optionalUser = register(user);
        if(optionalUser.isPresent())
            return Header.OK();
        else throw new ExistedUserException();
    }

    public Header signIn(Header<UserApiRequest> request) {
        UserApiRequest body = request.getData();

        String email = body.getEmail();
        String password = body.getPassword();

        Optional<User> optionalUser = authenticate(email, password);

        return optionalUser
                .map(user -> {
                    String token = jwtUtil.createToken(user.getId(), user.getName(), user.getType());

                    return Header.OK(token);
                })
                .orElseThrow(LoginFailedException::new);
    }

    private Optional<User> register(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        if(optionalUser.isPresent()) {
            return Optional.empty();
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);

        return Optional.of(savedUser);
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

    public Header<UserApiResponse> read(Authentication authentication) {
        Claims claims = (Claims)authentication.getPrincipal();
        Long id = claims.get("id", Long.class);

        return userRepository.findById(id)
            .map(UserApiService::response)
            .map(Header::OK)
            .orElseThrow(BadInputException::new);
    }

    public Header<List<UserApiResponse>> readAllCustomer(Pageable pageable) {
        Page<User> customerUsers = userRepository.findByTypeEquals(UserType.CUSTOMER, pageable);

        List<UserApiResponse> userApiResponseList = customerUsers.stream()
                .map(UserApiService::response)
                .collect(Collectors.toList());

        Pagination pagination = BaseCrudApiService.pagination(customerUsers);

        return Header.OK(userApiResponseList, pagination);
    }

    public Header<UserApiResponse> readStaff(Long id) {
        Optional<User> optionalStaff = userRepository.findById(id);

        optionalStaff
                .map(staff -> {
                    if(staff.getType() == UserType.CUSTOMER)
                        return null;

                    return staff;
                })
                .map(UserApiService::response)
                .map(Header::OK)
                .orElseThrow(BadInputException::new);

        return userRepository.findById(id)
                .map(UserApiService::response)
                .map(Header::OK)
                .orElseThrow(BadInputException::new);
    }

    public Header<List<UserApiResponse>> readAllStaff(Pageable pageable) {
        Page<User> staffUsers = userRepository.findByTypeNot(UserType.CUSTOMER, pageable);

        List<UserApiResponse> userApiResponseList = staffUsers.stream()
                .map(UserApiService::response)
                .collect(Collectors.toList());

        Pagination pagination = BaseCrudApiService.pagination(staffUsers);

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
                        .setPassword(passwordEncoder.encode(body.getPassword()))
                        .setName(body.getName())
                        .setGender(body.getGender())
                        .setAddress(body.getAddress())
                        .setPhoneNumber(body.getPhoneNumber());

                    User savedUser = userRepository.save(user);

                    return savedUser;
                })
                .map(UserApiService::response)
                .map(Header::OK)
                .orElseThrow(BadInputException::new);
    }

    public Header delete(Authentication authentication) {
        Claims claims = (Claims)authentication.getPrincipal();
        Long id = claims.get("id", Long.class);

        userRepository.deleteById(id);

        return Header.OK();
    }

    public Header deleteStaff(Long id) {
        Optional<User> optionalStaff = userRepository.findById(id);
        boolean isDeleted = optionalStaff
                .map(staff -> {
                    boolean result = false;

                    if(!staff.getType().equals(UserType.CUSTOMER)) {
                        userRepository.deleteById(id);
                        result = true;
                    }
                    return result;
                })
                .orElse(false);

        if(isDeleted) {
            return Header.OK();
        }
        throw new BadInputException();
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
