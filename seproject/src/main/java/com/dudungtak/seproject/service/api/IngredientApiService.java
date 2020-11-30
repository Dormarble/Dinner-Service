package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.Ingredient;
import com.dudungtak.seproject.exception.BadInputException;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.Pagination;
import com.dudungtak.seproject.network.request.IngredientApiRequest;
import com.dudungtak.seproject.network.response.IngredientApiResponse;
import com.dudungtak.seproject.repository.IngredientRepository;
import com.dudungtak.seproject.service.BaseCrudApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientApiService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientApiService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Header<IngredientApiResponse> create(Header<IngredientApiRequest> request) {
        IngredientApiRequest body = request.getData();

        Ingredient ingredient = Ingredient.builder()
                .name(body.getName())
                .type(body.getType())
                .status(body.getStatus())
                .cost(body.getCost())
                .stock(body.getStock())
                .registeredAt(LocalDate.now())
                .build();

        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        IngredientApiResponse ingredientApiResponse = response(savedIngredient);

        return Header.OK(ingredientApiResponse);
    }

    public Header<IngredientApiResponse> read(Long id) {
        return ingredientRepository.findById(id)
                .map(IngredientApiService::response)
                .map(Header::OK)
                .orElseThrow(BadInputException::new);
    }

    public Header<List<IngredientApiResponse>> readAll(Pageable pageable) {
        Page<Ingredient> pageIngredient = ingredientRepository.findAll(pageable);

        List<IngredientApiResponse> ingredientApiResponseList = pageIngredient.stream()
                .map(IngredientApiService::response)
                .collect(Collectors.toList());

        Pagination pagination = BaseCrudApiService.pagination(pageIngredient);

        return Header.OK(ingredientApiResponseList, pagination);
    }

    public Header<IngredientApiResponse> update(Header<IngredientApiRequest> request) {
        IngredientApiRequest body = request.getData();

        return ingredientRepository.findById(body.getId())
                .map(ingredient -> {
                    ingredient
                            .setName(body.getName())
                            .setType(body.getType())
                            .setStatus(body.getStatus())
                            .setCost(body.getCost());

                    if(body.getRegisteredAt() != null)
                        ingredient.setRegisteredAt(body.getRegisteredAt());

                    Ingredient updatedIngredient = ingredientRepository.save(ingredient);

                    return updatedIngredient;
                })
                .map(IngredientApiService::response)
                .map(Header::OK)
                .orElseThrow(BadInputException::new);
    }

    public Header delete(Long id) {
        return ingredientRepository.findById(id)
                .map(dish -> {
                    ingredientRepository.delete(dish);
                    return Header.OK("");
                })
                .orElseThrow(BadInputException::new);
    }

    public static IngredientApiResponse response(Ingredient ingredient) {
        return IngredientApiResponse.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .type(ingredient.getType())
                .status(ingredient.getStatus())
                .cost(ingredient.getCost())
                .stock(ingredient.getStock())
                .registeredAt(ingredient.getRegisteredAt())
                .unregisteredAt(ingredient.getUnregisteredAt())
                .createdAt(ingredient.getCreatedAt())
                .createdBy(ingredient.getCreatedBy())
                .updatedAt(ingredient.getUpdatedAt())
                .updatedBy(ingredient.getUpdatedBy())
                .build();
    }
}
