package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.Ingredient;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.IngredientApiRequest;
import com.dudungtak.seproject.network.response.IngredientApiResponse;
import com.dudungtak.seproject.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class IngredientApiService {
    @Autowired
    IngredientRepository ingredientRepository;

    public Header<IngredientApiResponse> create(Header<IngredientApiRequest> request) {
        IngredientApiRequest body = request.getData();

        Ingredient ingredient = Ingredient.builder()
                .name(body.getName())
                .type(body.getType())
                .status(body.getStatus())
                .cost(body.getCost())
                .registeredAt(LocalDate.now())
                .build();

        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        return Optional.ofNullable(savedIngredient)
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("error on store"));
    }

    public Header<IngredientApiResponse> read(Long id) {
        return ingredientRepository.findById(id)
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("no data"));

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
                    if(body.getUnregisteredAt() != null)
                        ingredient.setUnregisteredAt(body.getUnregisteredAt());

                    Ingredient updatedIngredient = ingredientRepository.save(ingredient);

                    return updatedIngredient;
                })
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("no data"));
    }

    public Header delete(Long id) {
        return ingredientRepository.findById(id)
                .map(dish -> {
                    ingredientRepository.delete(dish);
                    return Header.OK("");
                })
                .orElseGet(() -> Header.ERROR("no data"));
    }

    public IngredientApiResponse response(Ingredient ingredient) {
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
