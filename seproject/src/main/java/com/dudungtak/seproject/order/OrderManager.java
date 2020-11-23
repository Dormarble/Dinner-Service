package com.dudungtak.seproject.order;

import com.dudungtak.seproject.entity.OrderGroup;
import com.dudungtak.seproject.enumpackage.OrderStatus;
import com.dudungtak.seproject.repository.OrderGroupRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderManager {
    @Autowired
    private OrderGroupRepository orderGroupRepository;

    private final Map<Long, OrderGroup> pendingConfirmMap;

    private final Queue<OrderGroup> standingByCookingQueue;

    private final Map<Long, OrderGroup> cookingMap;

    private final Queue<OrderGroup> standingByDeliveryQueue;

    private final Map<Long, OrderGroup> deliveryMap;

    private final Map<OrderGroup, OrderStatus> orderConditions;

    public OrderManager() {
        pendingConfirmMap = new HashMap<>();
        standingByCookingQueue = new LinkedList<>();
        cookingMap = new HashMap<>();
        standingByDeliveryQueue = new LinkedList<>();
        deliveryMap = new HashMap<>();
        orderConditions = new HashMap<>();
    }

    public void register(OrderGroup orderGroup) {
        pendingConfirmMap.put(orderGroup.getId(), orderGroup);
        changeStatus(orderGroup, OrderStatus.PENDINGCONFIRM);
    }

    public List<OrderGroup> getPendingConfirmOrder() {
        return pendingConfirmMap.keySet().stream()
                .map(pendingConfirmMap::get)
                .collect(Collectors.toList());
    }

    public void confirm(List<OrderGroup> confirmedList, List<OrderGroup> rejectedList) {
        confirmedList.stream()
                .forEach(orderGroup -> {
                    pendingConfirmMap.remove(orderGroup.getId());
                    changeStatus(orderGroup, OrderStatus.PENDINGCONFIRM);
                    standingByCookingQueue.add(orderGroup);
                });

        rejectedList.stream()
                .forEach(orderGroup -> {
                    pendingConfirmMap.remove(orderGroup.getId());
                    changeStatus(orderGroup, OrderStatus.REJECTED);
                });
    }

    public OrderGroup getNextCook() {
        OrderGroup orderGroup = standingByCookingQueue.poll();
        cookingMap.put(orderGroup.getId(), orderGroup);
        changeStatus(orderGroup, OrderStatus.COOKING);

        return orderGroup;
    }

    public void finishCooking(OrderGroup order) {
        Optional<OrderGroup> optionalOrderGroup = Optional.ofNullable(cookingMap.get(order.getId()));

        optionalOrderGroup.ifPresent(orderGroup -> {
            cookingMap.remove(order.getId());
            standingByDeliveryQueue.add(orderGroup);
            changeStatus(orderGroup, OrderStatus.STANDINGBYDELIVERY);
        });
    }

    public OrderGroup getNextDelivery() {
        OrderGroup orderGroup = standingByDeliveryQueue.poll();
        deliveryMap.put(orderGroup.getId(), orderGroup);
        changeStatus(orderGroup, OrderStatus.DELIVERY);

        return orderGroup;
    }

    public void finishDelivery(OrderGroup order) {
        Optional<OrderGroup> optionalOrderGroup = Optional.ofNullable(deliveryMap.get(order.getId()));

        optionalOrderGroup.ifPresent(orderGroup -> {
            deliveryMap.remove(order.getId());
            changeStatus(orderGroup, OrderStatus.DONE);
        });
    }

    private void changeStatus(OrderGroup orderGroup, OrderStatus status) {
        orderGroup.setStatus(status);
        orderConditions.put(orderGroup, status);

        new Thread(() -> orderGroupRepository.save(orderGroup)).run();
    }
}
