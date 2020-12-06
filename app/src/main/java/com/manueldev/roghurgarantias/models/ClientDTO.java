package com.manueldev.roghurgarantias.models;

import java.util.List;

public class ClientDTO {

    private long clientId;
    private String dni;
    private String names;
    private String lastnames;
    private String mobilePhone;
    private List<Object> activateAssurances;
    private Object createdAt;
    private Object lastUpdated;

    public ClientDTO(){}

    public long getClientId() { return clientId; }
    public ClientDTO setClientId(long value) { this.clientId = value; return this;}

    public String getDni() { return dni; }
    public ClientDTO setDni(String value) { this.dni = value; return this;}

    public String getNames() { return names; }
    public ClientDTO setNames(String value) { this.names = value;return this; }

    public String getLastnames() { return lastnames; }
    public ClientDTO setLastnames(String value) { this.lastnames = value;return this; }

    public List<Object> getActivateAssurances() { return activateAssurances; }
    public ClientDTO setActivateAssurances(List<Object> value) { this.activateAssurances = value;return this; }

    public Object getCreatedAt() { return createdAt; }
    public ClientDTO setCreatedAt(Object value) { this.createdAt = value; return this;}

    public Object getLastUpdated() { return lastUpdated; }
    public ClientDTO setLastUpdated(Object value) { this.lastUpdated = value; return this;}

    public String getFullName(){
        return names + " "+lastnames;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public ClientDTO setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
        return this;
    }
}
