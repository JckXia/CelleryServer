package com.cellery.api.backend.ui.model.request;

import java.util.List;

public class ProductsInRoutineRequestModel {
    private List<String> productIds;

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }
}
