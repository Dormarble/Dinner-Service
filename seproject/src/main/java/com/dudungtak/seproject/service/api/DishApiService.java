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
import com.dudungtak.seproject.service.BaseCrudApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Transactional
    public Header<DishApiResponse> create(Header<DishApiRequest> request) {
        DishApiRequest body = request.getData();

        // build dish without price
        Dish dish = Dish.builder()
                .name(body.getName())
                .status(body.getStatus())
                .imgUrl(body.getImgUrl())
                .image(body.getImage())
                .registeredAt(body.getRegisteredAt())
                .build();

        // create dishElements without dish
        List<DishElement> dishElementList = body.getDishElementList().stream()
                .map(dishElementApiRequest -> {
                    Ingredient ingredient = ingredientRepository.getOne(dishElementApiRequest.getIngredientId());

                    DishElement dishElement = DishElement.builder()
                            .totalPrice(ingredient.getCost().multiply(BigDecimal.valueOf(dishElementApiRequest.getQuantity())))
                            .quantity(dishElementApiRequest.getQuantity())
                            .ingredient(ingredient)
                            .build();


                    return dishElement;
                })
                .collect(Collectors.toList());

        // calculate total dish price
        BigDecimal totalPrice = dishElementList.stream()
                .map(DishElement::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // set price & save
        dish.setPrice(totalPrice);
        Dish savedDish = dishRepository.save(dish);

        // set dish & save
        List<DishElement> savedDishElementList = dishElementList.stream()
                .map(dishElement -> {
                    dishElement.setDish(savedDish);
                    DishElement savedDishElement = dishElementRepository.save(dishElement);

                    return savedDishElement;
                })
                .collect(Collectors.toList());

        // set dish id to dishElements  & save dishElements
        List<DishElementApiResponse> dishElementApiResponseList = savedDishElementList.stream()
                .map(DishElementApiService::response)
                .collect(Collectors.toList());

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

        List<DishApiResponse> dishApiResponseList =
                dishPage.stream()
                    .map(dish -> {
                        List<DishElementApiResponse> dishElementApiResponseList = dish.getDishElementList().stream()
                                .map(DishElementApiService::response)
                                .collect(Collectors.toList());

                        return DishApiService.response(dish, dishElementApiResponseList);
                    })
                    .collect(Collectors.toList());

        Pagination pagination = BaseCrudApiService.pagination(dishPage);

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
                        .setImgUrl(body.getImgUrl())
                        .setImage(body.getImage())
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
                                    .totalPrice(ingredient.getCost().multiply(BigDecimal.valueOf(dishElementApiRequest.getQuantity())))
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
        Optional<Dish> optionalDish = dishRepository.findById(id);

         optionalDish.ifPresent(dish -> {
             dish.getDishElementList().forEach(dishElement -> {
                 dishElementRepository.delete(dishElement);
             });

             dishRepository.delete(dish);
         });

        return optionalDish
                .map(dish -> {
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("no data"));
    }

    public static DishApiResponse response(Dish dish) {
        return DishApiResponse.builder()
                .id(dish.getId())
                .name(dish.getName())
                .status(dish.getStatus())
                .price(dish.getPrice())
                .imgUrl(dish.getImgUrl())
                .image(dish.getImage())
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
                .imgUrl(dish.getImgUrl())
                .image(dish.getImage())
                .registeredAt(dish.getRegisteredAt())
                .unregisteredAt(dish.getUnregisteredAt())
                .createdAt(dish.getCreatedAt())
                .createdBy(dish.getCreatedBy())
                .updatedAt(dish.getUpdatedAt())
                .updatedBy(dish.getUpdatedBy())
                .build();
    }
}
