package com.cellery.api.backend.ui.controllers;

import com.cellery.api.backend.shared.ProductDto;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.ui.model.request.CreateProductRequestModel;
import com.cellery.api.backend.ui.model.request.DeleteProductRequestModel;
import com.cellery.api.backend.ui.model.request.EditProductRequestModel;
import com.cellery.api.backend.ui.model.response.ProductRespModel;
import com.cellery.api.backend.ui.service.ProductsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private Environment env;
    private ProductsService productsService;
    private MapperUtil modelMapper;

    @Autowired
    ProductController(Environment env, ProductsService ps, MapperUtil mapper) {
        this.env = env;
        this.productsService = ps;
        this.modelMapper = mapper;
    }

    // get all products IN PROGRESS
    @GetMapping
    public ResponseEntity<String> getProducts(@RequestHeader(value = "${authentication.authorization}") String auth) {
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    // get product by id
    @GetMapping(path="/{id}")
    public ResponseEntity<ProductRespModel> getProduct(@PathVariable String productId) {

        try {
            ProductDto returnDto = productsService.getProduct(productId);

            ProductRespModel returnVal = new ModelMapper().map(returnDto, ProductRespModel.class);
            return ResponseEntity.status(HttpStatus.OK).body(returnVal);

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // create product
    @PostMapping
    public ResponseEntity<ProductRespModel> createProduct(@Valid @RequestBody CreateProductRequestModel product) {

        ModelMapper mapper = modelMapper.strictMapper();

        ProductDto productDto = mapper.map(product, ProductDto.class);
        ProductDto returnDto = productsService.createProduct(productDto);

        ProductRespModel returnVal = mapper.map(returnDto, ProductRespModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnVal);
    }

    // delete product by id
    @DeleteMapping(path="/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable String productId) {
        try {
            productsService.deleteProduct(productId);

            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted product.");

        } catch (FileNotFoundException e) { // product does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // delete a list of products
    @DeleteMapping(path = "/batch-delete")
    public ResponseEntity<Integer> deleteProductsById(@Valid @RequestBody List<DeleteProductRequestModel> productsToDelete) {
        // map to a list of productIds
        List<String> productIds = productsToDelete.stream().map(product -> product.getProductId()).collect(Collectors.toList());

        Integer numDeleted = productsService.deleteProducts(productIds);

        /* The frontend can check if the list size request is equal to the number deleted
            If not (and the number deleted will be less than the list size request), products either do not
            exist or they are still part of (a) routine(s).
         */
        return ResponseEntity.status(HttpStatus.OK).body(numDeleted);
    }

    @PatchMapping(path="/edit")
    public ResponseEntity<ProductRespModel> editProductInfo(@Valid @RequestBody EditProductRequestModel productInfo) {
        try {
            ModelMapper mapper = modelMapper.strictMapper();

            ProductDto productDto = mapper.map(productInfo, ProductDto.class);
            ProductDto returnDto = productsService.editProduct(productDto);

            ProductRespModel returnVal = mapper.map(returnDto, ProductRespModel.class);

            return ResponseEntity.status(HttpStatus.OK).body(returnVal);

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
