package com.simbirsoft.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelUser {

    private ModelUserData data;

    public ModelUserData getData() {
        return data;
    }

    public void setData(ModelUserData data) {
        this.data = data;
    }
}
