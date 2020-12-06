package com.manueldev.roghurgarantias.models;

// Analizar la refactorizacion del DTO

public class ClaimAssuranceDTO {

    private long assuranceId;
    private long usersCommerceClaimId;
    private String reason;

    public ClaimAssuranceDTO(){}

    public long getAssuranceId() {
        return assuranceId;
    }

    public ClaimAssuranceDTO setAssuranceId(long assuranceId) {
        this.assuranceId = assuranceId;
        return this;
    }

    public long getUsersCommerceClaimId() {
        return usersCommerceClaimId;
    }

    public ClaimAssuranceDTO setUsersCommerceClaimId(long usersCommerceClaimId) {
        this.usersCommerceClaimId = usersCommerceClaimId;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public ClaimAssuranceDTO setReason(String reason) {
        this.reason = reason;
        return this;
    }
}
