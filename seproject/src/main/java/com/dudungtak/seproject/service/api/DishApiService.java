package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.Dish;
import com.dudungtak.seproject.entity.DishElement;
import com.dudungtak.seproject.entity.Ingredient;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.Pagination;
import com.dudungtak.seproject.network.request.DishApiRequest;
import com.dudungtak.seproject.network.request.DishElementApiRequest;
import com.dudungtak.seproject.network.response.DishApiResponse;
import com.dudungtak.seproject.network.response.DishElementApiResponse;
import com.dudungtak.seproject.repository.DishElementRepository;
import com.dudungtak.seproject.repository.DishRepository;
import com.dudungtak.seproject.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DishApiService {
    @Autowired
    DishRepository dishRepository;

    @Autowired
    DishElementRepository dishElementRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    public Header<DishApiResponse> create(Header<DishApiRequest> request) {
        DishApiRequest body = request.getData();

        // create dish
        Dish dish = Dish.builder()
                .name(body.getName())
                .status(body.getStatus())
                .price(body.getPrice())
                .registeredAt(body.getRegisteredAt())
                .build();

        // create DishElement
        List<DishElementApiResponse> dishElementApiResponseList = body.getDishElementList().stream()
                .map(dishElementApiRequest -> {
                    Ingredient ingredient = ingredientRepository.getOne(dishElementApiRequest.getIngredientId());

                    DishElement dishElement = DishElement.builder()
                            // totalCost = ingredientCost * ingredientQuantity
                            .totalCost(ingredient.getCost().multiply(BigDecimal.valueOf(dishElementApiRequest.getQuantity())))
                            .quantity(dishElementApiRequest.getQuantity())
                            .dish(dish)
                            .ingredient(ingredient)
                            .build();

                    DishElement newDishElement = dishElementRepository.save(dishElement);

                    return DishElementApiService.response(newDishElement);
                })
                .collect(Collectors.toList());

        Dish savedDish = dishRepository.save(dish);

        return Header.OK(response(savedDish, dishElementApiResponseList));
    }

    public Header<DishApiResponse> read(Long id) {
        Dish dish = dishRepository.getOne(id);

        List<DishElementApiResponse> dishElementApiResponseList =
                dish.getDishElementList().stream()
                    .map(DishElementApiService::response)
                    .collect(Collectors.toList());

        DishApiResponse dishApiResponse = response(dish, dishElementApiResponseList);

        return Header.OK(dishApiResponse);
    }

    public Header<List<DishApiResponse>> readAll(Pageable pageable) {
        Page<Dish> dishPage = dishRepository.findAll(pageable);

        List<DishApiResponse> dishApiResponseList = dishPage.stream()
                .map(DishApiService::response)
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(dishPage.getTotalPages())
                .totalElements(dishPage.getTotalElements())
                .currentPage(dishPage.getNumber())
                .currentElements(dishPage.getNumberOfElements())
                .build();

        return Header.OK(dishApiResponseList, pagination);
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

                    // update DishElement
                    List<DishElementApiRequest> dishElementApiRequestList = body.getDishElementList();


                    List<Long> dishElementIdList = updatedDish.getDishElementList().stream().
                            map(dishElement -> dishElement.getId())
                            .collect(Collectors.toList());

                    for(DishElementApiRequest dishElementApiRequest : dishElementApiRequestList) {
                        Long id = dishElementApiRequest.getId();

                        // 기존 재료 변경한 경우
                        if(dishElementIdList.contains(id)) {
                            Integer quantity = dishElementApiRequest.getQuantity();
                            // 기존 재료의 수량을 변경한 경우
                            if(quantity != 0) {
                                DishElement dishElement = dishElementRepository.getOne(id)
                                        .setQuantity(dishElementApiRequest.getQuantity());
                                dishElementRepository.save(dishElement);
                            } else {        // 기존 재료를 삭제한 경우
                                DishElement dishElement = dishElementRepository.getOne(id);
                                dishElementRepository.delete(dishElement);
                            }
                        } else {        // 새로운 재료를 추가한 경우
                            Ingredient ingredient = ingredientRepository.getOne(dishElementApiRequest.getIngredientId());

                            DishElement dishElement = DishElement.builder()
                                    .totalCost(ingredient.getCost().multiply(BigDecimal.valueOf(dishElementApiRequest.getQuantity())))
                                    .quantity(dishElementApiRequest.getQuantity())
                                    .dish(updatedDish)
                                    .ingredient(ingredient)
                                    .build();

                            dishElementRepository.save(dishElement);
                        }
                    }

                    return updatedDish;
                })
                .map(DishApiService::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("no data"));
    }

    public Header delete(Long id) {
        Dish dish = dishRepository.getOne(id);

        if(dish == null) return Header.ERROR("no data");

        dish.getDishElementList().forEach(dishElement -> {
                dishElementRepository.delete(dishElement);
            });

        dishRepository.delete(dish);

        return Header.OK();
    }

    public static DishApiResponse response(Dish dish) {
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

    public static DishApiResponse response(Dish dish, List<DishElementApiResponse> dishElementApiResponseList) {
        return DishApiResponse.builder()
                .id(dish.getId())
                .name(dish.getName())
                .status(dish.getStatus())
                .price(dish.getPrice())
                .dishElementList(dishElementApiResponseList)
                .registeredAt(dish.getRegisteredAt())
                .unregisteredAt(dish.getUnregisteredAt())
                .createdAt(dish.getCreatedAt())
                .createdBy(dish.getCreatedBy())
                .updatedAt(dish.getUpdatedAt())
                .updatedBy(dish.getUpdatedBy())
                .build();
    }
}
