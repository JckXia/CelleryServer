package com.cellery.api.backend.ui.model.request;

public class UpdateLogRequestModel {
    private String amRoutine;
    private String pmRoutine;
    private String notes;
    private Integer rating;
    private Boolean isTimeOfMonth;
    private Long  logUpdateTimeStamp;

    public Long getLogUpdateTimeStamp() {
        return logUpdateTimeStamp;
    }

    public void setLogUpdateTimeStamp(Long logUpdateTimeStamp) {
        this.logUpdateTimeStamp = logUpdateTimeStamp;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsTimeOfMonth() {
        return isTimeOfMonth;
    }

    public void setIsTimeOfMonth(Boolean timeOfMonth) {
        isTimeOfMonth = timeOfMonth;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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
}
