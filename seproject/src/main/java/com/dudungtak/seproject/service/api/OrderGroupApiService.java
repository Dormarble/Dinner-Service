package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.*;
import com.dudungtak.seproject.enumpackage.OrderStatus;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.Pagination;
import com.dudungtak.seproject.network.request.OrderGroupApiRequest;
import com.dudungtak.seproject.network.response.OrderElementApiResponse;
import com.dudungtak.seproject.network.response.OrderGroupApiResponse;
import com.dudungtak.seproject.order.OrderManager;
import com.dudungtak.seproject.repository.*;
import com.dudungtak.seproject.service.BaseCrudApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Autowired
    OrderManager orderManager;

    public Header<OrderGroupApiResponse> create(Header<OrderGroupApiRequest> request) {
        OrderGroupApiRequest body = request.getData();

        User user = userRepository.getOne(body.getUserId());
        Style style = styleRepository.getOne(body.getStyle().getId());

        OrderGroup orderGroup = OrderGroup.builder()
                                    .orderAt(body.getOrderAt())
                                    .revAddress(body.getRevAddress())
                                    .paymentType(body.getPaymentType())
                                    .status(OrderStatus.RECEIVED)
                                    .comment(body.getComment())
                                    .revName(body.getRevName())
                                    .user(user)
                                    .style(style)
                                    .build();

        List<OrderElement> orderElementList = body.getOrderElementList().stream()
                .map(orderElementApiRequest -> {
                    Dish dish = dishRepository.getOne(orderElementApiRequest.getDishId());

                    OrderElement orderElement = OrderElement.builder()
                            .totalPrice(dish.getPrice().multiply(BigDecimal.valueOf(orderElementApiRequest.getQuantity())))
                            .quantity(orderElementApiRequest.getQuantity())
                            .dish(dish)
                            .build();

                    return orderElement;
                })
                .collect(Collectors.toList());

        BigDecimal totalPrice = orderElementList.stream()
                .map(orderElement -> orderElement.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orderGroup.setTotalPrice(totalPrice);
        OrderGroup savedOrderGroup = orderGroupRepository.save(orderGroup);

        List<OrderElementApiResponse> orderElementApiResponseList = orderElementList.stream()
                .map(orderElement -> {
                    orderElement.setOrderGroup(savedOrderGroup);
                    OrderElement savedOrderElement = orderElementRepository.save(orderElement);

                    return savedOrderElement;
                })
                .map(OrderElementApiService::response)
                .collect(Collectors.toList());

        orderManager.register(savedOrderGroup);

        OrderGroupApiResponse response = response(savedOrderGroup, orderElementApiResponseList);

        return Header.OK(response);
    }

    public Header<List<OrderGroupApiResponse>> readAll(Long userId, Pageable pageable) {
        User user = userRepository.getOne(userId);

        Page<OrderGroup> orderGroups =  orderGroupRepository.findByUserOrderByCreatedAtDesc(user, pageable);

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

    public Header<List<OrderGroupApiResponse>> nextConfirm() {
        List<OrderGroup> orderGroupList = orderManager.getPendingConfirmOrder();

        List<OrderGroupApiResponse> orderGroupApiResponseList = orderGroupList.stream()
                .map(orderGroup -> {
                    List<OrderElementApiResponse> orderElementApiResponseList = orderGroup.getOrderElementList().stream()
                            .map(OrderElementApiService::response)
                            .collect(Collectors.toList());

                    return OrderGroupApiService.response(orderGroup, orderElementApiResponseList);
                })
                .collect(Collectors.toList());

        return Header.OK(orderGroupApiResponseList);
    }

    public Header confirm(Header<List<OrderGroupApiRequest>> request) {
        List<OrderGroupApiRequest> body = request.getData();

        List<Long> confirmedList = body.stream()
                .map(orderGroupApiRequest -> orderGroupApiRequest.getId())
                .collect(Collectors.toList());

        orderManager.confirm(confirmedList);

        return Header.OK();
    }

    public static OrderGroupApiResponse response(OrderGroup orderGroup) {
        return null;
    }

    public static OrderGroupApiResponse response(
            OrderGroup orderGroup,
            List<OrderElementApiResponse> orderElementApiResponseList
    ) {
        return OrderGroupApiResponse.builder()
                .id(orderGroup.getId())
                .totalPrice(orderGroup.getTotalPrice())
                .orderAt(orderGroup.getOrderAt())
                .status(orderGroup.getStatus())
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
