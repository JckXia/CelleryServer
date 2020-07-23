package com.cellery.api.backend.ui.model.request;

import java.util.List;

public class ProductsInRoutineRequestModel {
    private List<String> productIds;
    private boolean isAm;

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public boolean getAm() {
        return isAm;
    }

    public void setAm(boolean am) {
        isAm = am;
    }
}
