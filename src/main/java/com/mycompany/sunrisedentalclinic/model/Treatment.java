package com.mycompany.sunrisedentalclinic.model;

import java.math.BigDecimal;

public class Treatment {

    private int treatmentId;
    private String treatmentName;
    private String description;
    private BigDecimal treatmentCost;
    private String status;

    public Treatment() {
    }

    public Treatment(int treatmentId, String treatmentName,
                     String description, BigDecimal treatmentCost,
                     String status) {

        this.treatmentId = treatmentId;
        this.treatmentName = treatmentName;
        this.description = description;
        this.treatmentCost = treatmentCost;
        this.status = status;
    }

    public Treatment(String treatmentName,
                     String description,
                     BigDecimal treatmentCost,
                     String status) {

        this.treatmentName = treatmentName;
        this.description = description;
        this.treatmentCost = treatmentCost;
        this.status = status;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(int treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTreatmentCost() {
        return treatmentCost;
    }

    public void setTreatmentCost(BigDecimal treatmentCost) {
        this.treatmentCost = treatmentCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
