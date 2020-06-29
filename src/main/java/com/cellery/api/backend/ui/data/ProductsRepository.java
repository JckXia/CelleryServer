package com.cellery.api.backend.ui.data;

import org.springframework.data.repository.CrudRepository;

public interface ProductsRepository extends CrudRepository<ProductEntity, Long> {
    Boolean existsByProductId(String productId);
    ProductEntity findByProductId(String productId);
    ProductEntity getOneByProductId(String productId); // returns ref to db object
}
