package ru.check_api.order;

import java.util.ArrayList;
import java.util.List;

public class CreateOrder {
    private List<String> ingredients;

    public CreateOrder(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public CreateOrder() {
        ingredients = new ArrayList<>();
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
