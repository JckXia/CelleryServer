package com.cellery.api.backend.ui.model.request;

import java.util.List;

public class ProductsInRoutineRequestModel {
    private String routineId;
    private List<String> productIds;

    public String getRoutineId() {
        return routineId;
    }

    public void setRoutineId(String routineId) {
        this.routineId = routineId;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }
}
