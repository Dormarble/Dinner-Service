package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.OrderGroup;
import com.dudungtak.seproject.entity.User;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.Pagination;
import com.dudungtak.seproject.network.response.OrderElementApiResponse;
import com.dudungtak.seproject.network.response.OrderGroupApiResponse;
import com.dudungtak.seproject.repository.*;
import com.dudungtak.seproject.service.BaseCrudApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderGroupApiService {
    @Autowired
    OrderGroupRepository orderGroupRepository;

    @Autowired
    OrderElementRepository orderElementRepository;

    @Autowired
    DishRepository dishRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StyleRepository styleRepository;

    public Header<List<OrderGroupApiResponse>> readAll(Long userId, Pageable pageable) {
        User user = userRepository.getOne(userId);
        Page<OrderGroup> orderGroups =  orderGroupRepository.findAllByUserOrderByCreatedAtDesc(user, pageable);

        List<OrderGroupApiResponse> orderGroupApiResponseList = orderGroups.stream()
                .map(orderGroup -> {
                    List<OrderElementApiResponse> orderElementApiResponseList =
                            orderGroup.getOrderElementList().stream()
                                .map(OrderElementApiService::response)
                                .collect(Collectors.toList());

                    return response(orderGroup, orderElementApiResponseList);
                })
                .collect(Collectors.toList());

        Pagination pagination = BaseCrudApiService.pagination(orderGroups);

        return Header.OK(orderGroupApiResponseList, pagination);
    }

    public static OrderGroupApiResponse response(
            OrderGroup orderGroup,
            List<OrderElementApiResponse> orderElementApiResponseList
    ) {
        return OrderGroupApiResponse.builder()
                .id(orderGroup.getId())
                .totalPrice(orderGroup.getTotalPrice())
                .totalCost(orderGroup.getTotalCost())
                .orderAt(orderGroup.getOrderAt())
                .createdAt(orderGroup.getCreatedAt())
                .createdBy(orderGroup.getCreatedBy())
                .updatedAt(orderGroup.getUpdatedAt())
                .updatedBy(orderGroup.getUpdatedBy())
                .userId(orderGroup.getUser().getId())
                .revAddress(orderGroup.getRevAddress())
                .paymentType(orderGroup.getPaymentType())
                .comment(orderGroup.getComment())
                .revName(orderGroup.getRevAddress())
                .style(StyleApiService.response(orderGroup.getStyle()))
                .orderElementList(orderElementApiResponseList)
                .build();
    }
}
