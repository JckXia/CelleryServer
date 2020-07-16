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
    private String log_id;

    @Column
    private String notes;

    @Column
    private int rating;

    @Column
    private boolean is_time_of_month;

    @Column
    private int year_value;

    @Column
    private int month_value;

    @OneToMany(mappedBy = "logObject",cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<ImageEntity> logImages = new ArrayList<>();

    @Column
    private int day_value;

    @Column
    private String created_at;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getDay_value() {
        return day_value;
    }

    public void setDay_value(int day_value) {
        this.day_value = day_value;
    }

    public int getMonth_value() {
        return month_value;
    }

    public void setMonth_value(int month_value) {
        this.month_value = month_value;
    }

    public List<ImageEntity> getLogImages() {
        return logImages;
    }

    public void setLogImages(List<ImageEntity> logImages) {
        this.logImages = logImages;
    }

    public int getYear_value() {
        return year_value;
    }

    public void setYear_value(int year_value) {
        this.year_value = year_value;
    }

    public boolean isIs_time_of_month() {
        return is_time_of_month;
    }

    public void setIs_time_of_month(boolean is_time_of_month) {
        this.is_time_of_month = is_time_of_month;
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
}
