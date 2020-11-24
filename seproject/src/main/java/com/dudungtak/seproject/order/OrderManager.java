package com.dudungtak.seproject.order;

import com.dudungtak.seproject.entity.OrderGroup;
import com.dudungtak.seproject.enumpackage.OrderStatus;
import com.dudungtak.seproject.repository.OrderGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderManager {
    private final OrderGroupRepository orderGroupRepository;

    private final Map<Long, OrderGroup> pendingConfirmMap;

    private final Map<Long, OrderGroup> confirmingMap;

    private final Queue<OrderGroup> standingByCookingQueue;

    private final Map<Long, OrderGroup> cookingMap;

    private final Queue<OrderGroup> standingByDeliveryQueue;

    private final Map<Long, OrderGroup> deliveryMap;

    private final Map<OrderGroup, OrderStatus> orderConditions;

    @Autowired
    public OrderManager(OrderGroupRepository orderGroupRepository) {
        this.orderGroupRepository = orderGroupRepository;
        orderConditions = new HashMap<>();

        pendingConfirmMap = loadMap(OrderStatus.PENDINGCONFIRM);
        confirmingMap = loadMap(OrderStatus.CONFIRMING);
        standingByCookingQueue = loadQueue(OrderStatus.STANDINGBYCOOKING);
        cookingMap = loadMap(OrderStatus.COOKING);
        standingByDeliveryQueue = loadQueue(OrderStatus.STANDINGBYDELIVERY);
        deliveryMap = loadMap(OrderStatus.DELIVERY);
    }

    public void register(OrderGroup orderGroup) {
        pendingConfirmMap.put(orderGroup.getId(), orderGroup);
        changeStatus(orderGroup, OrderStatus.PENDINGCONFIRM);
    }

    public List<OrderGroup> getPendingConfirmOrder() {
        List<OrderGroup> orderGroupList = pendingConfirmMap.keySet().stream()
                .map(orderGroupId -> {
                    OrderGroup orderGroup = pendingConfirmMap.get(orderGroupId);
                    changeStatus(orderGroup, OrderStatus.CONFIRMING);
                    confirmingMap.put(orderGroupId, orderGroup);

                    return orderGroup;
                })
                .collect(Collectors.toList());

        pendingConfirmMap.clear();

        return orderGroupList;
    }

    public List<OrderGroup> confirm(List<Long> confirmedList) {
        // confirmed
        List<OrderGroup> orderGroupList = confirmedList.stream()
                .filter(orderGroupId -> {
                    return Optional.ofNullable(confirmingMap.get(orderGroupId)).isPresent();
                })
                .map(orderGroupId -> {
                    OrderGroup orderGroup = confirmingMap.get(orderGroupId);

                    confirmingMap.remove(orderGroupId);
                    changeStatus(orderGroup, OrderStatus.STANDINGBYCOOKING);
                    standingByCookingQueue.add(orderGroup);

                    return orderGroup;
                })
                .collect(Collectors.toList());
        // rejected
        confirmingMap.keySet().stream()
                .forEach(rejectedId -> {
                    OrderGroup orderGroup = confirmingMap.get(rejectedId);
                    confirmingMap.remove(rejectedId);
                    changeStatus(orderGroup, OrderStatus.REJECTED);
                });

        return orderGroupList;
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

    private Map<Long, OrderGroup> loadMap(OrderStatus status) {
        Map<Long, OrderGroup> hashMap = new HashMap<>();
        System.out.println(orderGroupRepository);
        List<OrderGroup> orderGroupList = orderGroupRepository.findByStatusOrderByCreatedAtAsc(status);

        orderGroupList
                .forEach(orderGroup -> {
                    hashMap.put(orderGroup.getId(), orderGroup);
                    orderConditions.put(orderGroup, status);
                });

        return hashMap;
    }

    public  Queue<OrderGroup> loadQueue(OrderStatus status) {
        Queue<OrderGroup> orderGroupQueue = new LinkedList<>();
        List<OrderGroup> orderGroupList = orderGroupRepository.findByStatusOrderByCreatedAtAsc(status);

        orderGroupList
                .forEach(orderGroup -> {
                    orderGroupQueue.add(orderGroup);
                    orderConditions.put(orderGroup, status);
                });

        return orderGroupQueue;
    }
}
