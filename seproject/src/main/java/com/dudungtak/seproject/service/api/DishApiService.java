package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.Dish;
import com.dudungtak.seproject.entity.DishElement;
import com.dudungtak.seproject.entity.Ingredient;
import com.dudungtak.seproject.exception.BadInputException;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.Pagination;
import com.dudungtak.seproject.network.request.DishApiRequest;
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

    private final DishRepository dishRepository;

    private final DishElementRepository dishElementRepository;

    private final IngredientRepository ingredientRepository;

    @Autowired
    public DishApiService(DishRepository dishRepository, DishElementRepository dishElementRepository, IngredientRepository ingredientRepository) {
        this.dishRepository = dishRepository;
        this.dishElementRepository = dishElementRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Transactional
    public Header<DishApiResponse> create(Header<DishApiRequest> request) {
        DishApiRequest body = request.getData();

        // build dish without price
        Dish dish = Dish.builder()
                .name(body.getName())
                .price(body.getPrice())             // <------- add (do not use ingredient)
                .status(body.getStatus())
                .imgUrl(body.getImgUrl())
                .image(body.getImage())
                .registeredAt(body.getRegisteredAt())
                .build();

        Dish savedDish = dishRepository.save(dish);

        DishApiResponse dishApiResponse = response(savedDish);

// ============================================= unused  =============================================
//        // create dishElements without dish
//        List<DishElement> dishElementList = body.getDishElementList().stream()
//                .map(dishElementApiRequest -> {
//                    Ingredient ingredient = ingredientRepository.getOne(dishElementApiRequest.getIngredientId());
//
//                    DishElement dishElement = DishElement.builder()
//                            .totalPrice(ingredient.getCost().multiply(BigDecimal.valueOf(dishElementApiRequest.getQuantity())))
//                            .quantity(dishElementApiRequest.getQuantity())
//                            .ingredient(ingredient)
//                            .build();
//
//                    return dishElement;
//                })
//                .collect(Collectors.toList());
//
//        // calculate total dish price
//        BigDecimal totalPrice = dishElementList.stream()
//                .map(DishElement::getTotalPrice)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        // estimate total price of dish  &  save dish
//        dish.setPrice(totalPrice);
//        Dish savedDish = dishRepository.save(dish);
//
//        // set dish id to dishElements  & save dishElements
//        List<DishElement> savedDishElementList = dishElementList.stream()
//                .map(dishElement -> {
//                    dishElement.setDish(savedDish);
//                    DishElement savedDishElement = dishElementRepository.save(dishElement);
//
//                    return savedDishElement;
//                })
//                .collect(Collectors.toList());
//        savedDish.setDishElementList(savedDishElementList);     //  set dishElements for response()
//
//        DishApiResponse dishApiResponse = response(savedDish);

        return Header.OK(dishApiResponse);
    }

    public Header<DishApiResponse> read(Long id) {
        Dish dish = dishRepository.getOne(id);

        return Header.OK(DishApiService.response(dish));
    }

    public Header<List<DishApiResponse>> readAll(Pageable pageable) {
        Page<Dish> dishPage = dishRepository.findAll(pageable);

        List<DishApiResponse> dishApiResponseList = dishPage.stream()
                    .map(DishApiService::response)
                    .collect(Collectors.toList());

        Pagination pagination = BaseCrudApiService.pagination(dishPage);

        return Header.OK(dishApiResponseList, pagination);
    }

    public Header<DishApiResponse> update(Header<DishApiRequest> request) {
        DishApiRequest body = request.getData();

        Optional<Dish> optionalDish = dishRepository.findById(body.getId());

        return optionalDish
                .map(dish -> {
                    // update dish
                    dish
                        .setName(body.getName())
                        .setStatus(body.getStatus())
                        .setPrice(body.getPrice())              // <---------- add (do not use ingredient)
                        .setImgUrl(body.getImgUrl())
                        .setImage(body.getImage())
                        .setRegisteredAt(body.getRegisteredAt())
                        .setUnregisteredAt(body.getUnregisteredAt());

                    Dish savedDish = dishRepository.save(dish);
                    DishApiResponse dishApiResponse = DishApiService.response(savedDish);
// ============================================= unused  =============================================
//                    // delete old dishElements
//                    dishElementRepository.deleteAllByDish(dish);
//
//                    // create updated dishElements  &  save dishElements
//                    List<DishElement> dishElementList = body.getDishElementList().stream()
//                            .map(dishElementApiRequest -> {
//                                Ingredient ingredient = ingredientRepository.getOne(dishElementApiRequest.getIngredientId());
//
//                                DishElement dishElement = DishElement.builder()
//                                        .totalPrice(ingredient.getCost().multiply(BigDecimal.valueOf(dishElementApiRequest.getQuantity())))
//                                        .quantity(dishElementApiRequest.getQuantity())
//                                        .dish(dish)
//                                        .ingredient(ingredient)
//                                        .build();
//
//                                DishElement savedDishElement = dishElementRepository.save(dishElement);
//
//                                return savedDishElement;
//                            })
//                            .collect(Collectors.toList());
//
//                    // saved updated dish
//                    BigDecimal price = dishElementList.stream()
//                            .map(DishElement::getTotalPrice)
//                            .reduce(BigDecimal.ZERO, BigDecimal::add);
//                    dish.setPrice(price);
//                    Dish savedDish = dishRepository.save(dish);
//
//                    DishApiResponse dishApiResponse = DishApiService.response(savedDish);

                    return Header.OK(dishApiResponse);
                })
                .orElseThrow(BadInputException::new);
    }

    @Transactional
    public Header delete(Long id) {
        Optional<Dish> optionalDish = dishRepository.findById(id);
        return optionalDish
                .map(dish -> {
                    List<DishElement> dishElementList = dish.getDishElementList();
                    dishElementList.forEach(dishElement -> dishElementRepository.delete(dishElement));

                    return dish;
                })
                .map(dish -> {
                    dishRepository.delete(dish);

                    return Header.OK();
                })
                .orElseThrow(BadInputException::new);
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
//    public static DishApiResponse response(Dish dish) {
//        List<DishElementApiResponse> dishElementApiResponseList = dish.getDishElementList().stream()
//                .map(DishElementApiService::response)
//                .collect(Collectors.toList());
//
//        return DishApiResponse.builder()
//                .id(dish.getId())
//                .name(dish.getName())
//                .status(dish.getStatus())
//                .price(dish.getPrice())
//                .dishElementList(dishElementApiResponseList)
//                .imgUrl(dish.getImgUrl())
//                .image(dish.getImage())
//                .registeredAt(dish.getRegisteredAt())
//                .unregisteredAt(dish.getUnregisteredAt())
//                .createdAt(dish.getCreatedAt())
//                .createdBy(dish.getCreatedBy())
//                .updatedAt(dish.getUpdatedAt())
//                .updatedBy(dish.getUpdatedBy())
//                .build();
//    }
}
