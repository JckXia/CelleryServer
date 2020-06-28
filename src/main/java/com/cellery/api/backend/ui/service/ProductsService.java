package com.cellery.api.backend.ui.service;

import com.cellery.api.backend.shared.ProductDto;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.ui.data.ProductEntity;
import com.cellery.api.backend.ui.data.ProductsRepository;
import com.cellery.api.backend.ui.data.RoutineEntity;
import com.cellery.api.backend.ui.data.RoutinesRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
public class ProductsService {

    private ProductsRepository productsRepository;
    private RoutinesRepository routinesRepository;
    private MapperUtil mapper;

    @Autowired
    public ProductsService(ProductsRepository productsRepository, RoutinesRepository routinesRepository, MapperUtil mapper) {
        this.productsRepository = productsRepository;
        this.routinesRepository = routinesRepository;
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
    public void deleteProduct(String deleteProductId) throws FileNotFoundException {
        if (!productsRepository.existsByProductId(deleteProductId)) {
            throw new FileNotFoundException("Product does not exist");
        }

        ProductEntity productEntity = productsRepository.findByProductId(deleteProductId);

        // The product will be deleted regardless if it is in a routine or not. On the frontend, the user
        // will be prompted about the effects of deleting the product if it is in a routine (it will disappear from
        // routines it is in)

        // check if the routine(s) that have this product have only 1 product, so deleting the product
        // will delete that routine too
        List<RoutineEntity> inRoutines = productEntity.getRoutines();
        for (RoutineEntity routine: inRoutines) {
            if (routine.getProducts().size() == 1) {
                routinesRepository.delete(routine); // goodbye
            } else {
                // remove it from the routine
                routine.removeProductFromRoutine(productEntity); // if we do not, the product still exists in db even after line 92 since routine is the parent
                routinesRepository.save(routine);
            }
        }

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
            } catch (FileNotFoundException e) {
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
