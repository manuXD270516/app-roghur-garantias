package com.manueldev.roghurgarantias.models;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class ActiveAssuranceDTO {

    private int assuranceId;

    private String clientDni;

    private String clientFullName;

    // Campos utilizados para el reclamo de garantia
    private String productCode;

    private String productName;

    private String assuranceState;

    private String assuranceDateState;

    private int expirationDays;


    public ActiveAssuranceDTO(int assuranceId, String clientDni, String clientFullName, String productCode, String productName, String assuranceState, String assuranceDateState, int expirationDays) {
        this.assuranceId = assuranceId;
        this.clientDni = clientDni;
        this.clientFullName = clientFullName;
        this.productCode = productCode;
        this.productName = productName;
        this.assuranceState = assuranceState;
        this.assuranceDateState = assuranceDateState;
        this.expirationDays = expirationDays;
    }

    public ActiveAssuranceDTO() {
    }


    public int getAssuranceId() {
        return assuranceId;
    }

    public void setAssuranceId(int assuranceId) {
        this.assuranceId = assuranceId;
    }

    public String getClientDni() {
        return clientDni;
    }

    public void setClientDni(String clientDni) {
        this.clientDni = clientDni;
    }

    public String getClientFullName() {
        return clientFullName;
    }

    public void setClientFullName(String clientFullName) {
        this.clientFullName = clientFullName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAssuranceState() {
        return assuranceState;
    }

    public void setAssuranceState(String assuranceState) {
        this.assuranceState = assuranceState;
    }

    public String getAssuranceDateState() {
        return assuranceDateState;
    }

    public void setAssuranceDateState(String assuranceDateState) {
        this.assuranceDateState = assuranceDateState;
    }

    public int getExpirationDays() {
        return expirationDays;
    }

    public void setExpirationDays(int expirationDays) {
        this.expirationDays = expirationDays;
    }

    public boolean assuranceExpired() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");
                LocalDateTime dateExpired = LocalDate.parse(this.assuranceDateState, formatter).atStartOfDay();
                dateExpired = dateExpired.plusDays(this.expirationDays);
                return dateExpired.isBefore(LocalDateTime.now());
            } else {
                SimpleDateFormat formatterOld = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                try {
                    Date dateExpired = formatterOld.parse(this.assuranceDateState);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateExpired);
                    cal.add(Calendar.DAY_OF_YEAR, this.expirationDays);
                    dateExpired = cal.getTime();
                    return dateExpired.before(new Date());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } catch (Exception e){
            String error = e.getMessage();
            Log.i(TAG, "assuranceExpired error: "+ e.getMessage());
            return false;
        }

    }

    @Override
    public String toString() {
        return "ActiveAssuranceDTO{" +
                "assuranceId=" + assuranceId +
                ", clientDni='" + clientDni + '\'' +
                ", clientFullName='" + clientFullName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", assuranceState='" + assuranceState + '\'' +
                ", assuranceDateState='" + assuranceDateState + '\'' +
                '}';
    }
}
