package com.dudungtak.seproject.enumpackage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IngredientType {
    VEGETABLE(0, "vegetable", "아채");

    private Integer id;
    private String title;
    private String description;
}
