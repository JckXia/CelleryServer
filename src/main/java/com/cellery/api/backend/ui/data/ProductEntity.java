package com.cellery.api.backend.ui.data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class ProductEntity implements Serializable {

    private static final long serialVersionUID = -353092878535428416L;

    @Id
    @GeneratedValue
    @Column
    private long Id;

    @Column
    private String productId;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_description")
    private String description;

    @ManyToMany(mappedBy = "products")
    private List<RoutineEntity> routines = new ArrayList<>();

    @ManyToOne // owning side of relationship
    @JoinColumn(name = "user_fk")
    private UserEntity productUser;

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

    public void addRoutine(RoutineEntity routine) {
        this.routines.add(routine);
    }

    public UserEntity getProductUser() {
        return productUser;
    }

    public void setProductUser(UserEntity productUser) {
        this.productUser = productUser;
    }
}
