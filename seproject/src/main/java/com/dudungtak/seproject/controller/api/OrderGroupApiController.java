package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.controller.Permission;
import com.dudungtak.seproject.enumpackage.AccessType;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.OrderGroupApiRequest;
import com.dudungtak.seproject.network.response.OrderGroupApiResponse;
import com.dudungtak.seproject.service.api.OrderGroupApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderGroupApiController {
    @Autowired
    OrderGroupApiService orderGroupApiService;

    @PostMapping("order")
    public Header<OrderGroupApiResponse> create(Authentication authentication, @RequestBody Header<OrderGroupApiRequest> request) {
        Permission.isValidAccess(authentication, AccessType.CUSTOMER);

        return orderGroupApiService.create(authentication, request);
    }

    @GetMapping("user/orders")
    public Header<List<OrderGroupApiResponse>> readAll(Authentication authentication, Pageable pageable) {
        Permission.isValidAccess(authentication, AccessType.LOGINEDALL);

        return orderGroupApiService.readAll(authentication, pageable);
    }

    @GetMapping("/order/confirm")
    public Header<List<OrderGroupApiResponse>> nextConfirm(Authentication authentication) {
        Permission.isValidAccess(authentication, AccessType.MANAGER);

        return orderGroupApiService.nextConfirm();
    }

    @PostMapping("/order/confirm")
    public Header confirm(Authentication authentication, @RequestBody Header<List<OrderGroupApiRequest>> request) {
        Permission.isValidAccess(authentication, AccessType.MANAGER);

        return orderGroupApiService.confirm(request);
    }

    @GetMapping("order/cook")
    public Header<OrderGroupApiResponse> nextCook(Authentication authentication) {
        Permission.isValidAccess(authentication, AccessType.COOK);

        return orderGroupApiService.nextCook(authentication);
    }

    @PostMapping("order/cook/finish")
    public Header finishCook(Authentication authentication) {
        Permission.isValidAccess(authentication, AccessType.COOK);

        return orderGroupApiService.finishCook(authentication);
    }

    @GetMapping("order/delivery")
    public Header<OrderGroupApiResponse> nextDelivery(Authentication authentication) {
        Permission.isValidAccess(authentication, AccessType.DELIVERYMAN);

        return orderGroupApiService.nextDelivery(authentication);
    }

    @PostMapping("order/delivery/finish")
    public Header finishDelivery(Authentication authentication) {
        Permission.isValidAccess(authentication, AccessType.DELIVERYMAN);

        return orderGroupApiService.finishDelivery(authentication);
    }

    @PutMapping("order/cancel")
    public Header cancel(Authentication authentication, @RequestBody Header<OrderGroupApiRequest> request) {
        Permission.isValidAccess(authentication, AccessType.CUSTOMER);

        return orderGroupApiService.cancel(authentication, request);
    }
}
