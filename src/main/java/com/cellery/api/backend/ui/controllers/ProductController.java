package com.cellery.api.backend.ui.controllers;

import com.cellery.api.backend.shared.ProductDto;
import com.cellery.api.backend.shared.UserDto;
import com.cellery.api.backend.shared.Util.JwtUtil;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.shared.Util.Utils;
import com.cellery.api.backend.ui.model.request.CreateProductRequestModel;
import com.cellery.api.backend.ui.model.request.EditProductRequestModel;
import com.cellery.api.backend.ui.model.request.ProductRequestModel;
import com.cellery.api.backend.ui.model.response.ProductRespModel;
import com.cellery.api.backend.ui.service.ProductsService;
import com.cellery.api.backend.ui.service.UsersService;
import com.googlecode.gentyref.TypeToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private Environment env;
    private ProductsService productsService;
    private UsersService usersService;
    private MapperUtil modelMapper;
    private JwtUtil jwtUtil;

    @Autowired
    ProductController(Environment env, ProductsService ps, UsersService usersService, MapperUtil mapper, JwtUtil jwtUtil) {
        this.env = env;
        this.productsService = ps;
        this.usersService = usersService;
        this.modelMapper = mapper;
        this.jwtUtil = jwtUtil;
    }

    private Type productRespModelListType() {
        return new TypeToken<List<ProductRespModel>>(){}.getType();
    }

    private UserDto getUserDto(String auth) throws UsernameNotFoundException {
        String email = jwtUtil.getEmailFromToken(auth);
        UserDto userDto = usersService.getUserDetailsByEmail(email);
        return userDto;
    }

    // get all products
    @GetMapping
    public ResponseEntity<List<ProductRespModel>> getProducts(@RequestHeader(value = "${authentication.authorization}") String auth) {
        try {
            auth = auth.replace(env.getProperty("authentication.bearer"), "");
            UserDto userDto = getUserDto(auth);

            if (!jwtUtil.validateToken(auth, userDto)) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(null);
            }

            List<ProductDto> productsList = productsService.getProducts(jwtUtil.getEmailFromToken(auth));

            List<ProductRespModel> returnVal = modelMapper.strictMapper().map(productsList, productRespModelListType());
            return ResponseEntity.status(HttpStatus.OK).body(returnVal);

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } // jwt related exceptions are caught by the web filter
    }

    // get product by id
    @GetMapping(path="/{id}")
    public ResponseEntity<ProductRespModel> getProduct(@PathVariable String id) {
        try {
            ProductDto returnDto = productsService.getProduct(id);

            ProductRespModel returnVal = new ModelMapper().map(returnDto, ProductRespModel.class);
            return ResponseEntity.status(HttpStatus.OK).body(returnVal);

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // create product
    @PostMapping
    public ResponseEntity<ProductRespModel> createProduct(@RequestHeader(value = "${authentication.authorization}") String auth,
                                                          @Valid @RequestBody CreateProductRequestModel product) {
        try {
            auth = auth.replace(env.getProperty("authentication.bearer"), "");
            UserDto userDto = getUserDto(auth);

            if (!jwtUtil.validateToken(auth, userDto)) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(null);
            }

            ModelMapper mapper = modelMapper.strictMapper();

            ProductDto productDto = mapper.map(product, ProductDto.class);
            ProductDto returnDto = productsService.createProduct(jwtUtil.getEmailFromToken(auth), productDto); // create product and add to user

            ProductRespModel returnVal = mapper.map(returnDto, ProductRespModel.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(returnVal);

        } catch (RuntimeException e) {
            // user has valid jwt but email does not match the valid user
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // delete product by id
    @DeleteMapping(path="/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable String id) {
        try {
            productsService.deleteProduct(id);

            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted product.");

        } catch (FileNotFoundException e) { // product does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // delete a list of products
    @DeleteMapping(path = "/batch-delete")
    public ResponseEntity<Integer> deleteProductsById(@Valid @RequestBody List<ProductRequestModel> productsToDelete) {
        // map to a list of productIds
        List<String> productIds = productsToDelete.stream().map(product -> product.getProductId()).collect(Collectors.toList());

        Integer numDeleted = productsService.deleteProducts(productIds);

        /* The frontend can check if the list size request is equal to the number deleted
            If not (and the number deleted will be less than the list size request), products either do not
            exist or they are still part of (a) routine(s).
         */
        return ResponseEntity.status(HttpStatus.OK).body(numDeleted);
    }

    @PatchMapping(path="/{id}")
    public ResponseEntity<ProductRespModel> editProductInfo(@PathVariable String id, @Valid @RequestBody EditProductRequestModel productInfo) {
        try {
            ProductDto productDto = modelMapper.strictMapper().map(productInfo, ProductDto.class);
            productDto.setProductId(id);

            ProductDto returnDto = productsService.editProduct(productDto); // edit product details

            ProductRespModel returnVal = modelMapper.strictMapper().map(returnDto, ProductRespModel.class);
            return ResponseEntity.status(HttpStatus.OK).body(returnVal);

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
