package com.cellery.api.backend.ui.service;

import com.cellery.api.backend.shared.ProductDto;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.ui.data.ProductEntity;
import com.cellery.api.backend.ui.data.ProductsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
public class ProductsService {

    private ProductsRepository productsRepository;
    private MapperUtil mapper;

    @Autowired
    public ProductsService(ProductsRepository pr, MapperUtil mapper) {
        this.productsRepository = pr;
        this.mapper = mapper;
    }

    /*private ProductDto convertEntityToDto(ProductEntity src) {
        Converter<List<RoutineEntity>, Integer> convert = arr -> arr.getSource() == null ? 0 : arr.getSource().size();

        ModelMapper lilMap = this.mapper.strictMapper();
        lilMap.createTypeMap(ProductEntity.class, ProductDto.class).addMappings(mapper ->
                mapper.using(convert).map(ProductEntity::getRoutines, ProductDto::setRoutines));

        return lilMap.map(src, ProductDto.class);
    }*/

    public ProductDto getProduct(String wantedProductId) throws FileNotFoundException {
        if (!productsRepository.existsByProductId(wantedProductId)) {
            throw new FileNotFoundException("Product does not exist");
        }

        ProductEntity productEntity = productsRepository.findByProductId(wantedProductId);

        ProductDto returnDto = mapper.strictMapper().map(productEntity, ProductDto.class);
        return returnDto;
    }

    public ProductDto createProduct(ProductDto newProduct) {
        newProduct.setProductId((UUID.randomUUID().toString()));

        ModelMapper modelMapper = mapper.strictMapper();
        ProductEntity productEntity = modelMapper.map(newProduct, ProductEntity.class);

        productsRepository.save(productEntity); // save to database

        ProductDto returnDto = mapper.strictMapper().map(productEntity, ProductDto.class);
        return returnDto;
    }

    // Deletes a product by the productId
    public void deleteProduct(String deleteProductId) throws FileNotFoundException, UnsupportedOperationException {
        if (!productsRepository.existsByProductId(deleteProductId)) {
            throw new FileNotFoundException("Product does not exist");
        }

        ProductEntity productEntity = productsRepository.findByProductId(deleteProductId);

        // The product will be deleted regardless if it is in a routine or not. On the frontend, the user
        // will be prompted about the effects of deleting the product if it is in a routine (it will disappear from
        // routines it is in)
        productsRepository.delete(productEntity);
    }

    /* In a list of products to delete, if one product does not exist, we will
        continue iterating over the remaining products. An exception does not stop the loop.
        Returns the number of products deleted
     */
    public Integer deleteProducts(List<String> ids) {
        Integer numDeleted = 0;

        for (String id: ids) {
            try {
                deleteProduct(id);
                ++numDeleted;
            } catch (FileNotFoundException | UnsupportedOperationException e) {
                /* I know! This is strange and definitely not the cleanest way to deal with our case */
            }
        }
        return numDeleted;
    }

    public ProductDto editProduct(ProductDto product) throws FileNotFoundException {
        if (!productsRepository.existsByProductId(product.getProductId())) {
            throw new FileNotFoundException("Product does not exist");
        }

        // get ref db object
        ProductEntity productEdit = productsRepository.getOneByProductId(product.getProductId());

        if (!product.getName().isEmpty() && product.getName() != null && !product.getName().equals(productEdit.getName())) {
            productEdit.setName(product.getName());
        }

        // product desc is allowed to be empty
        if (!product.getDescription().equals(productEdit.getDescription())) {
            productEdit.setDescription(product.getDescription());
        }

        ProductDto returnDto = mapper.strictMapper().map(productEdit, ProductDto.class);

        // save object changes to db
        productsRepository.save(productEdit);

        return returnDto;
    }
}
