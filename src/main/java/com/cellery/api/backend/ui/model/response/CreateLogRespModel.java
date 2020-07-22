package com.cellery.api.backend.ui.model.response;

public class CreateLogRespModel {
    private String log_id;
    private String note;
    private String rating;
    private String am_routine;
    private String pm_routine;
    private String created_at;


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPm_routine() {
        return pm_routine;
    }

    public void setPm_routine(String pm_routine) {
        this.pm_routine = pm_routine;
    }

    public String getAm_routine() {
        return am_routine;
    }

    public void setAm_routine(String am_routine) {
        this.am_routine = am_routine;
    }

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

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }
}
