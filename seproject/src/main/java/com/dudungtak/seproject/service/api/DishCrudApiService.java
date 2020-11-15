package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.Dish;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.DishApiRequest;
import com.dudungtak.seproject.network.response.DishApiResponse;
import com.dudungtak.seproject.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DishCrudApiService {
    @Autowired
    DishRepository dishRepository;

    public Header<DishApiResponse> create(Header<DishApiRequest> request) {
        DishApiRequest body = request.getData();

        Dish dish = Dish.builder()
                .name(body.getName())
                .status(body.getStatus())
                .price(body.getPrice())
                .registeredAt(body.getRegisteredAt())
                .build();

        Dish savedDish = dishRepository.save(dish);

        return Optional.ofNullable(savedDish)
                .map(this::response)
                .map(response -> Header.OK(response, ""))
                .orElseGet(() -> Header.ERROR("error on store", ""));
    }

    public Header<DishApiResponse> read(Long id) {
        return dishRepository.findById(id)
                .map(this::response)
                .map(dishResponse -> Header.OK(dishResponse, ""))
                .orElseGet(() -> Header.ERROR("no data", ""));

    }

    public Header<DishApiResponse> update(Header<DishApiRequest> request) {
        DishApiRequest body = request.getData();

        return dishRepository.findById(body.getId())
                .map(dish -> {
                    dish
                        .setName(body.getName())
                        .setStatus(body.getStatus())
                        .setPrice(body.getPrice())
                        .setRegisteredAt(body.getRegisteredAt())
                        .setUnregisteredAt(body.getUnregisteredAt());

                    Dish updatedDish = dishRepository.save(dish);

                    return updatedDish;
                })
                .map(this::response)
                .map(response -> Header.OK(response, ""))
                .orElseGet(() -> Header.ERROR("no data", ""));
    }

    public Header delete(Long id) {
        return dishRepository.findById(id)
                .map(dish -> {
                    dishRepository.delete(dish);
                    return Header.OK("");
                })
                .orElseGet(() -> Header.ERROR("no data", ""));
    }

    public DishApiResponse response(Dish dish) {
        return DishApiResponse.builder()
                .id(dish.getId())
                .name(dish.getName())
                .status(dish.getStatus())
                .price(dish.getPrice())
                .registeredAt(dish.getRegisteredAt())
                .unregisteredAt(dish.getUnregisteredAt())
                .createdAt(dish.getCreatedAt())
                .createdBy(dish.getCreatedBy())
                .updatedAt(dish.getUpdatedAt())
                .updatedBy(dish.getUpdatedBy())
                .build();
    }
}
