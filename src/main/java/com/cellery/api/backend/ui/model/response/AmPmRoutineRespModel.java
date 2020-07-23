package com.cellery.api.backend.ui.model.response;

import org.springframework.security.core.parameters.P;

import java.util.List;

public class AmPmRoutineRespModel {
    private RoutineRespModel am, pm;

    public AmPmRoutineRespModel(List<RoutineRespModel> routines) {
        for (RoutineRespModel routine : routines) {
            if (routine.getAm()) {
                am = routine;
            } else {
                pm = routine;
            }
        }
    }

    public RoutineRespModel getAm() {
        return am;
    }

    public void setAm(RoutineRespModel am) {
        this.am = am;
    }

    public RoutineRespModel getPm() {
        return pm;
    }

    public void setPm(RoutineRespModel pm) {
        this.pm = pm;
    }
}
