package com.cellery.api.backend.ui.model.request;

import java.util.List;

public class CreateRoutineRequestModel {
    private List<String> products; // list of productIds

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }
}
