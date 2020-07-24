package com.cellery.api.backend.ui.model.request;

import java.util.List;

public class ProductsInRoutineRequestModel {
    private List<String> productIds;
    private Boolean isAm;

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public Boolean getIsAm() {
        return isAm;
    }

    public void setIsAm(Boolean am) {
        isAm = am;
    }
}
