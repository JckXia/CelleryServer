package com.cellery.api.backend.ui.data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Products")
public class ProductEntity implements Serializable {

    private static final long serialVersionUID = -353092878535428416L;

    @Id
    @GeneratedValue
    private long Id;

    @Column
    private String productId;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_description")
    private String description;

    @ManyToMany
    @JoinTable(name = "routine_products",
            joinColumns = @JoinColumn(name = "id_routine"),
            inverseJoinColumns = @JoinColumn(name =  "id_product"))
    private List<RoutineEntity> routines = new ArrayList<>();

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RoutineEntity> getRoutines() {
        return routines;
    }

    public void setRoutines(List<RoutineEntity> routines) {
        this.routines = routines;
    }
}
