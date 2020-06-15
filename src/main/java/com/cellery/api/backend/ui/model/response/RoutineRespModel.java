package com.cellery.api.backend.ui.model.response;

import java.util.List;

public class RoutineRespModel {
    private String routineId;
    private List<ProductRespModel> products;

    public String getRoutineId() {
        return routineId;
    }

    public void setRoutineId(String routineId) {
        this.routineId = routineId;
    }

    public List<ProductRespModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRespModel> products) {
        this.products = products;
    }
}
