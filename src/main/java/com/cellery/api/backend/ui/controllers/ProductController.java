package com.cellery.api.backend.ui.controllers;

import com.cellery.api.backend.ui.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductsService ps;


    // TODO: Create product (post), delete product by id (delete), update product by id (patch), bulk delete (delete), get product by id
}
