package com.msaproject.doctor.model;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.msaproject.doctor.Constants;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PatientDoctorModel implements Serializable {

    public static final String PATIENT_ID = "patientId";
    public static final String DOCTOR_ID = "doctorId";

    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("patientId")
    private String patientId;
    @Expose
    @SerializedName("patientModel")
    private UserModel patientModel;
    @Expose
    @SerializedName("doctorId")
    private String doctorId;
    @Expose
    @SerializedName("doctorModel")
    private UserModel doctorModel;
    @Expose
    @SerializedName("lastAppointment")
    @ServerTimestamp
    private Date lastAppointment;
    @Expose
    @SerializedName("nextAppointment")
    private Date nextAppointment;
    @Expose
    @SerializedName(Constants.MAP_KEY_CREATED_AT)
    @ServerTimestamp
    private Date createdAt;
    @Expose
    @SerializedName(Constants.MAP_KEY_UPDATED_AT)
    @ServerTimestamp
    private Date updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public UserModel getPatientModel() {
        return patientModel;
    }

    public void setPatientModel(UserModel patientModel) {
        this.patientModel = patientModel;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public UserModel getDoctorModel() {
        return doctorModel;
    }

    public void setDoctorModel(UserModel doctorModel) {
        this.doctorModel = doctorModel;
    }

    public Date getLastAppointment() {
        return lastAppointment;
    }

    public void setLastAppointment(Date lastAppointment) {
        this.lastAppointment = lastAppointment;
    }

    public Date getNextAppointment() {
        return nextAppointment;
    }

    public void setNextAppointment(Date nextAppointment) {
        this.nextAppointment = nextAppointment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Map<String, Object> getModelMap() {
        HashMap<String, Object> modelMap = new HashMap<>();
        modelMap.put("id", id);
        modelMap.put(PATIENT_ID, patientId);
        modelMap.put(DOCTOR_ID, doctorId);
        modelMap.put("lastAppointment", lastAppointment);
        modelMap.put("nextAppointment", nextAppointment);
        modelMap.put(Constants.MAP_KEY_CREATED_AT, createdAt);
        modelMap.put(Constants.MAP_KEY_UPDATED_AT, updatedAt);
        return modelMap;
    }
}
