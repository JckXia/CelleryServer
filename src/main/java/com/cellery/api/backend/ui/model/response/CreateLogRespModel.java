package com.cellery.api.backend.ui.model.response;

public class CreateLogRespModel {
    private String logId;
    private String note;
    private String rating;
    private String amRoutine;
    private String pmRoutine;
    private String createdAt;


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getAmRoutine() {
        return amRoutine;
    }

    public void setAmRoutine(String amRoutine) {
        this.amRoutine = amRoutine;
    }

    public String getPmRoutine() {
        return pmRoutine;
    }

    public void setPmRoutine(String pmRoutine) {
        this.pmRoutine = pmRoutine;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
