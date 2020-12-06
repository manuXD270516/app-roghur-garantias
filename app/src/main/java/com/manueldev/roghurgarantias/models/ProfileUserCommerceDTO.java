package com.manueldev.roghurgarantias.models;

public class ProfileUserCommerceDTO {

    private long profileUserCommerceId;
    private String username;
    private String commmerceWork;
    private String commerceAddress;
    private long countAssurancesActivated;
    private long countAssurancesClaim;
    private long countClientsRegister;

    public ProfileUserCommerceDTO(){}

    public long getProfileUserCommerceId() { return profileUserCommerceId; }
    public void setProfileUserCommerceId(long value) { this.profileUserCommerceId = value; }

    public String getUsername() { return username; }
    public void setUsername(String value) { this.username = value; }

    public String getCommmerceWork() { return commmerceWork; }
    public void setCommmerceWork(String value) { this.commmerceWork = value; }

    public String getCommerceAddress() { return commerceAddress; }
    public void setCommerceAddress(String value) { this.commerceAddress = value; }

    public long getCountAssurancesActivated() { return countAssurancesActivated; }
    public void setCountAssurancesActivated(long value) { this.countAssurancesActivated = value; }

    public long getCountAssurancesClaim() { return countAssurancesClaim; }
    public void setCountAssurancesClaim(long value) { this.countAssurancesClaim = value; }

    public long getCountClientsRegister() { return countClientsRegister; }
    public void setCountClientsRegister(long value) { this.countClientsRegister = value; }
}
