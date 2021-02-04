package com.msaproject.doctor.model;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.msaproject.doctor.Constants;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppointmentModel implements Serializable {

    public static final String PATIENT_ID = "patientId";
    public static final String DOCTOR_ID = "doctorId";

    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName(PATIENT_ID)
    private String patientId;
    @Expose
    @SerializedName(DOCTOR_ID)
    private String doctorId;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("doctorNotes")
    private String doctorNotes;
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

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
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
        modelMap.put("title", title);
        modelMap.put("doctorNotes", doctorNotes);
        modelMap.put(Constants.MAP_KEY_CREATED_AT, createdAt);
        modelMap.put(Constants.MAP_KEY_UPDATED_AT, updatedAt);
        return modelMap;
    }
}
