package com.cellery.api.backend.shared;

import java.io.Serializable;

public class LogDto implements Serializable {
    private static final long serialVersionUID = 34470962826401635L;

    private String logId;
    private String note;
    private Integer rating;
    private String amRoutine;
    private String pmRoutine;
    private String createdAt;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
