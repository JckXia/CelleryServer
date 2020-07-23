package com.cellery.api.backend.ui.model.response;

import java.util.List;

public class AmPmRoutineRespModel {
    private RoutineRespModel am, pm;

    public AmPmRoutineRespModel(List<RoutineRespModel> routines) {
        if (!routines.isEmpty()) {
            if (routines.get(0).getAm()) {
                am = routines.get(0);
                pm = routines.get(1);
            } else {
                am = routines.get(1);
                pm = routines.get(0);
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
