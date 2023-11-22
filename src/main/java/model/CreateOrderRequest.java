package model;

import java.util.List;

public class CreateOrderRequest {
    private List<String> ingredients;

    public CreateOrderRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public CreateOrderRequest(){
    }

    public List<String> getIngredients() {
        return ingredients;
    }

}
