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

    public Boolean getAm() {
        return isAm;
    }

    public void setAm(Boolean am) {
        isAm = am;
    }
}
