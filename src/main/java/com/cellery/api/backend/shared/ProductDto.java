package com.cellery.api.backend.shared;

import java.io.Serializable;
import java.util.List;

public class ProductDto implements Serializable {
    private static final long serialVersionUID = 3447096282640163255L;

    private String productId;
    private String name;
    private String description;
    private List<RoutineDto> routines;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public List<RoutineDto> getRoutines() {
        return routines;
    }

    public void setRoutines(List<RoutineDto> routines) {
        this.routines = routines;
    }
}
