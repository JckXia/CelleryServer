package com.cellery.api.backend.ui.controllers;

import com.cellery.api.backend.shared.ProductDto;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.ui.model.request.CreateProductRequestModel;
import com.cellery.api.backend.ui.model.request.DeleteProductRequestModel;
import com.cellery.api.backend.ui.model.request.EditProductRequestModel;
import com.cellery.api.backend.ui.model.request.ProductRequestModel;
import com.cellery.api.backend.ui.model.response.ProductRespModel;
import com.cellery.api.backend.ui.service.ProductsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductsService ps;
    private MapperUtil modelMapper;

    @Autowired
    ProductController(ProductsService ps, MapperUtil mapper) {
        this.ps = ps;
        this.modelMapper = mapper;
    }

    @GetMapping
    public String test() {
        return "You have poked the /products endpoint";
    }

    @GetMapping(path="/get")
    public ResponseEntity<ProductRespModel> getProduct(@Valid @RequestBody ProductRequestModel productReq) {

        try {
            ProductDto returnDto = ps.getProduct(productReq.getProductId());

            ProductRespModel returnVal = modelMapper.strictMapper().map(returnDto, ProductRespModel.class);

            return ResponseEntity.status(HttpStatus.OK).body(returnVal);

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(path="/create")
    public ResponseEntity<ProductRespModel> createProduct(@Valid @RequestBody CreateProductRequestModel product) {

        ModelMapper mapper = modelMapper.strictMapper();

        ProductDto productDto = mapper.map(product, ProductDto.class);
        ProductDto returnDto = ps.createProduct(productDto);

        ProductRespModel returnVal = mapper.map(returnDto, ProductRespModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnVal);
    }

    @DeleteMapping(path="/delete")
    public ResponseEntity<Boolean> deleteProductById(@Valid @RequestBody DeleteProductRequestModel productId) {

        try {
            String id = productId.getProductId();
            Boolean returnBool = ps.deleteProduct(id);

            return ResponseEntity.status(HttpStatus.OK).body(returnBool);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping(path = "/batch-delete")
    public ResponseEntity<Integer> deleteProductsById(@Valid @RequestBody ArrayList<DeleteProductRequestModel> productsToDelete) {
        // map to a list of productIds
        List<String> productIds = productsToDelete.stream().map(product -> product.getProductId()).collect(Collectors.toList());

        Integer numDeleted = ps.deleteProducts(productIds);

        return ResponseEntity.status(HttpStatus.OK).body(numDeleted);
    }

    @PatchMapping(path="/edit")
    public ResponseEntity<ProductRespModel> editProductInfo(@Valid @RequestBody EditProductRequestModel productInfo) {
        try {
            ModelMapper mapper = modelMapper.strictMapper();

            ProductDto productDto = mapper.map(productInfo, ProductDto.class);
            ProductDto returnDto = ps.editProduct(productDto);

            ProductRespModel returnVal = mapper.map(returnDto, ProductRespModel.class);

            return ResponseEntity.status(HttpStatus.OK).body(returnVal);

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
