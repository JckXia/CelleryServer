package com.cellery.api.backend.ui.data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -217932131231325124L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable=false,length=255,unique = true)
    private String userName;

    @Column(nullable = false, length = 120, unique = true)
    private String email;

    @Column(nullable = false, length = 120, unique = true)
    private String userId;

    @Column(nullable = false, unique = true)
    private String encryptedPassword;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RoutineEntity> routines = new ArrayList<>();

    @OneToMany(mappedBy = "productUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductEntity> userProducts = new ArrayList<>();

    public List<RoutineEntity> getRoutines() {
        return routines;
    }

    public void setRoutines(List<RoutineEntity> routines) {
        this.routines = routines;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ProductEntity> getUserProducts() {
        return userProducts;
    }

    public void setUserProducts(List<ProductEntity> userProducts) {
        this.userProducts = userProducts;
    }

    public void removeProductFromUser(ProductEntity product){
        this.userProducts.remove(product);
    }

    public void removeRoutineFromUser(RoutineEntity routine) {
        this.routines.remove(routine);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
