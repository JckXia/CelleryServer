package com.cellery.api.backend.ui.model.response;

import java.util.List;

public class ProductRespModel {
    private String productId;
    private String name;
    private String description;
    private List<RoutineRespModel> routines;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RoutineRespModel> getRoutines() {
        return routines;
    }

    public void setRoutines(List<RoutineRespModel> routines) {
        this.routines = routines;
    }
}
