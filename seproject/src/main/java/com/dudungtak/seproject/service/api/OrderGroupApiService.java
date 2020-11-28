package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.*;
import com.dudungtak.seproject.enumpackage.AccessType;
import com.dudungtak.seproject.enumpackage.OrderStatus;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.Pagination;
import com.dudungtak.seproject.network.request.OrderGroupApiRequest;
import com.dudungtak.seproject.network.response.OrderElementApiResponse;
import com.dudungtak.seproject.network.response.OrderGroupApiResponse;
import com.dudungtak.seproject.order.OrderManager;
import com.dudungtak.seproject.repository.*;
import com.dudungtak.seproject.service.BaseCrudApiService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderGroupApiService {

    private final OrderGroupRepository orderGroupRepository;

    private final OrderElementRepository orderElementRepository;

    private final StaffRepository staffRepository;

    private final DishRepository dishRepository;

    private final UserRepository userRepository;

    private final StyleRepository styleRepository;

    private final OrderManager orderManager;

    @Autowired
    public OrderGroupApiService(OrderGroupRepository og, OrderElementRepository oe, StaffRepository sf, DishRepository d, UserRepository u, StyleRepository sl, OrderManager om) {
        this.orderGroupRepository = og;
        this.orderElementRepository = oe;
        this.staffRepository = sf;
        this.dishRepository = d;
        this.userRepository = u;
        this.styleRepository = sl;
        this.orderManager = om;
    }


    public Header<OrderGroupApiResponse> create(Authentication authentication, Header<OrderGroupApiRequest> request) {
        Claims claims = (Claims)authentication.getPrincipal();
        Long userId = claims.get("id", Long.class);

        OrderGroupApiRequest body = request.getData();

        // find user  &  find style
        User user = userRepository.getOne(userId);
        Style style = styleRepository.getOne(body.getStyle().getId());

        // build new OrderGroup
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

        // build new OrderElements
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

        // set total price of orderGroup  &  save orderGroup
        orderGroup.setTotalPrice(totalPrice);
        OrderGroup savedOrderGroup = orderGroupRepository.save(orderGroup);

        // set orderGroup field of orderElement  &  save orderElement
        List<OrderElement> orderElementApiResponseList = orderElementList.stream()
                .map(orderElement -> {
                    orderElement.setOrderGroup(savedOrderGroup);
                    OrderElement savedOrderElement = orderElementRepository.save(orderElement);

                    return savedOrderElement;
                })
                .collect(Collectors.toList());

        // register to OrderManager
        orderManager.register(savedOrderGroup);

        // build response  & return
        savedOrderGroup.setOrderElementList(orderElementApiResponseList);

        return Header.OK(response(savedOrderGroup));
    }

    public Header<List<OrderGroupApiResponse>> readAll(Authentication authentication, Pageable pageable) {
        Claims claims = (Claims)authentication.getPrincipal();
        Long userId = claims.get("id", Long.class);

        User user = userRepository.getOne(userId);

        Page<OrderGroup> orderGroups =  orderGroupRepository.findByUserOrderByCreatedAtDesc(user, pageable);

        List<OrderGroupApiResponse> orderGroupApiResponseList = orderGroups.stream()
                .map(OrderGroupApiService::response)
                .collect(Collectors.toList());

        Pagination pagination = BaseCrudApiService.pagination(orderGroups);

        return Header.OK(orderGroupApiResponseList, pagination);
    }

    public Header<List<OrderGroupApiResponse>> nextConfirm() {
        List<OrderGroup> orderGroupList = orderManager.getPendingConfirmOrder();

        List<OrderGroupApiResponse> orderGroupApiResponseList = orderGroupList.stream()
                .map(OrderGroupApiService::response)
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

    public Header<OrderGroupApiResponse> nextCook(Authentication authentication) {
        Claims claims = (Claims)authentication.getPrincipal();
        Long cookId = claims.get("id", Long.class);

        Optional<Staff> optionalCook = staffRepository.findById(cookId);

        return optionalCook
                .map(cook -> {
                    Optional<OrderGroup> optionalNextCook = orderManager.getNextCook(cook);

                    return optionalNextCook
                            .map(OrderGroupApiService::response)
                            .map(Header::OK)
                            .orElseGet(Header::OK);
                })
                .orElseGet(() -> Header.ERROR("invalid input"));
    }

    public Header finishCook(Authentication authentication) {
        Claims claims = (Claims)authentication.getPrincipal();
        Long cookId = claims.get("id", Long.class);

        Optional<Staff> optionalCook = staffRepository.findById(cookId);

        optionalCook
                .ifPresent(cook -> {orderManager.finishCooking(cook);});

        return optionalCook.map((cook) -> Header.OK())
                .orElseGet(() -> Header.ERROR("invalid input"));
    }

    public Header<OrderGroupApiResponse> nextDelivery(Authentication authentication) {
        Claims claims = (Claims)authentication.getPrincipal();
        Long deliveryManId = claims.get("id", Long.class);

        Optional<Staff> optionalDeliveryMan = staffRepository.findById(deliveryManId);

        return optionalDeliveryMan
                .map(deliveryMan -> {
                    Optional<OrderGroup> optionalNextDelivery = orderManager.getNextDelivery(deliveryMan);

                    return optionalNextDelivery
                            .map(OrderGroupApiService::response)
                            .map(Header::OK)
                            .orElseGet(Header::OK);
                })
                .orElseGet(() -> Header.ERROR("invalid input"));
    }

    public Header finishDelivery(Authentication authentication) {
        Claims claims = (Claims)authentication.getPrincipal();
        Long deliveryManId = claims.get("id", Long.class);

        Optional<Staff> optionalDeliveryMan = staffRepository.findById(deliveryManId);

        optionalDeliveryMan
                .ifPresent(deliveryMan -> {orderManager.finishDelivery(deliveryMan);});

        return optionalDeliveryMan.map((deliveryMan) -> Header.OK())
                .orElseGet(() -> Header.ERROR("invalid input"));
    }

    public Header cancel(Authentication authentication, Header<OrderGroupApiRequest> request) {
        Claims claims = (Claims)authentication.getPrincipal();
        Long userId = claims.get("id", Long.class);

        OrderGroupApiRequest body = request.getData();

        Optional<OrderGroup> optional = orderManager.findById(body.getId());
        if(!optional.isPresent()) return Header.ERROR("invalid OrderGroupID");
        Long orderGroupUserId = optional.get().getUser().getId();

        if(userId.equals(orderGroupUserId)) return Header.ERROR("permission denied");

        orderManager.cancel(body.getId());

        return Header.OK();
    }

    public static OrderGroupApiResponse response(OrderGroup orderGroup) {
        List<OrderElementApiResponse> elementResponseList =
                orderGroup.getOrderElementList().stream()
                        .map(OrderElementApiService::response)
                        .collect(Collectors.toList());

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
                .orderElementList(elementResponseList)
                .build();
    }
}
