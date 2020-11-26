package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.Ingredient;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.IngredientApiRequest;
import com.dudungtak.seproject.network.response.IngredientApiResponse;
import com.dudungtak.seproject.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class IngredientApiService {
    @Autowired
    IngredientRepository ingredientRepository;

    public Header<IngredientApiResponse> create(Authentication authentication, Header<IngredientApiRequest> request) {
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

        return Optional.ofNullable(savedIngredient)
                .map(IngredientApiService::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("error on store"));
    }

    public Header<IngredientApiResponse> read(Authentication authentication, Long id) {
        return ingredientRepository.findById(id)
                .map(IngredientApiService::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("no data"));

    }

    public Header<IngredientApiResponse> update(Authentication authentication, Header<IngredientApiRequest> request) {
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
                .orElseGet(() -> Header.ERROR("no data"));
    }

    public Header delete(Authentication authentication, Long id) {
        return ingredientRepository.findById(id)
                .map(dish -> {
                    ingredientRepository.delete(dish);
                    return Header.OK("");
                })
                .orElseGet(() -> Header.ERROR("no data"));
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
