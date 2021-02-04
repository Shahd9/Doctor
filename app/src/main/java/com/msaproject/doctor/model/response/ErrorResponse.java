package com.msaproject.doctor.model.response;

import com.google.gson.annotations.SerializedName;
import com.msaproject.doctor.model.ErrorModel;

import java.util.List;

public class ErrorResponse {

    @SerializedName("errors")
    private List<ErrorModel> errors;

    public List<ErrorModel> getErrors() {
        return errors;
    }

    public ErrorModel getFirstError(){
        if (errors == null || errors.isEmpty())
            return null;

        return errors.get(0);
    }
}
