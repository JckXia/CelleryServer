package com.cellery.api.backend.shared;

import java.io.Serializable;
import java.util.List;

public class RoutineDto implements Serializable {
    private static final long serialVersionUID = -3754051696982657160L;

    private String routineId;
    private List<ProductDto> products;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRoutineId() {
        return routineId;
    }

    public void setRoutineId(String routineId) {
        this.routineId = routineId;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}
