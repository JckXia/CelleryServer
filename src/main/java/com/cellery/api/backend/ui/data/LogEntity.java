package com.cellery.api.backend.ui.data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  Log entity has one to many relationship with images
 *  (Many images ( limited to 2) can belong to one log)
 *  Log entity has many to many relationship with medication
 *  Log entity has many to many relationship with routines (integrate with existing backend)
 */

@Entity
@Table(name = "logs")
public class LogEntity implements Serializable {
    private static final long serialVersionUID = -3530928785354284162L;

    @Id
    @GeneratedValue
    @Column
    private long id;

    @Column
    private String logId;

    @Column
    private String notes;

    @Column
    private int rating;

    @Column
    private Boolean isTimeOfMonth;



    @OneToMany(mappedBy = "logObject",cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<ImageEntity> logImages = new ArrayList<>();



    @Column
    private Long createdTimeStamp;



    @Column
    private String amRoutine;

    @Column
    private String pmRoutine;


    public Long getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(Long createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    @ManyToOne(fetch = FetchType.LAZY) // owning side of relationship
    @JoinColumn(name = "user_fk")
    private UserEntity logUser;

    public UserEntity getLogUser() {
        return logUser;
    }


    public void setLogUser(UserEntity logUser) {
        this.logUser = logUser;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public List<ImageEntity> getLogImages() {
        return logImages;
    }

    public void setLogImages(List<ImageEntity> logImages) {
        this.logImages = logImages;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getPmRoutine() {
        return pmRoutine;
    }

    public void setPmRoutine(String pmRoutine) {
        this.pmRoutine = pmRoutine;
    }

    public String getAmRoutine() {
        return amRoutine;
    }

    public void setAmRoutine(String amRoutine) {
        this.amRoutine = amRoutine;
    }

    public Boolean getIsTimeOfMonth() {
        return isTimeOfMonth;
    }

    public void setIsTimeOfMonth(Boolean timeOfMonth) {
        isTimeOfMonth = timeOfMonth;
    }
}