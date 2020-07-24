package com.cellery.api.backend.ui.data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routines")
public class RoutineEntity implements Serializable {

    private static final long serialVersionUID = 3462085515126113658L;

    @Id
    @GeneratedValue
    @Column
    private long id;

    @Column
    private String routineId;

    @ManyToOne(fetch = FetchType.LAZY) // owning side of relationship
    @JoinColumn(name = "user_fk")
    private UserEntity user;

    @Column(nullable = false)
    private Boolean isAm;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "routine_products",
            joinColumns = @JoinColumn(name =  "id_routine", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_product", referencedColumnName = "id"))
    private List<ProductEntity> products = new ArrayList<>();

    /* LogEntity should have a private array of RoutineEntities similar to UserEntity and similar
    annotations
    @ManyToOne
    @JoinColumn(name = "log_fk")
    private LogEntity logFk; */

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getRoutineId() {
        return routineId;
    }

    public void setRoutineId(String routineId) {
        this.routineId = routineId;
    }

    public void removeProductFromRoutine(ProductEntity product) {
        this.products.remove(product);
    }

    public void addProductToRoutine(ProductEntity product) {
        this.products.add(product);
    }

    public boolean getIsAm() {
        return isAm;
    }

    public void setIsAm(Boolean am) {
        isAm = am;
    }
}
