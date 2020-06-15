package com.cellery.api.backend.ui.data;

import org.springframework.data.repository.CrudRepository;

public interface ProductsRepository extends CrudRepository<ProductEntity, Long> {
}
