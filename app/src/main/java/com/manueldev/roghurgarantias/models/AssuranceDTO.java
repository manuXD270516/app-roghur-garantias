package com.manueldev.roghurgarantias.models;

public class AssuranceDTO {

    private long assuranceId;
    private long productId;
    private Object product;
    private long clientId;
    private Object client;
    private long usersCommerceActivateId;
    private Object usersCommerceActivate;
    private Object activationDate;
    private long expirationDays;
    private Object usersCommerceClaimId;
    private Object usersCommerceClaim;

    private Object reason;
    private Object claimDate;
    private Object createdAt;
    private Object lastUpdated;

    public AssuranceDTO(){}

    public long getAssuranceId() { return assuranceId; }
    public AssuranceDTO setAssuranceId(long value) { this.assuranceId = value; return this;}

    public long getProductId() { return productId; }
    public AssuranceDTO setProductId(long value) { this.productId = value; return this;}

    public Object getProduct() { return product; }
    public AssuranceDTO setProduct(Object value) { this.product = value; return this;}

    public long getClientId() { return clientId; }
    public AssuranceDTO setClientId(long value) { this.clientId = value; return this;}

    public Object getClient() { return client; }
    public AssuranceDTO setClient(Object value) { this.client = value; return this;}

    public long getUsersCommerceActivateId() { return usersCommerceActivateId; }
    public AssuranceDTO setUsersCommerceActivateId(long value) { this.usersCommerceActivateId = value; return this;}

    public Object getUsersCommerceActivate() { return usersCommerceActivate; }
    public AssuranceDTO setUsersCommerceActivate(Object value) { this.usersCommerceActivate = value; return this;}

    public Object getActivationDate() { return activationDate; }
    public AssuranceDTO setActivationDate(Object value) { this.activationDate = value; return this;}

    public long getExpirationDays() { return expirationDays; }
    public AssuranceDTO setExpirationDays(long value) { this.expirationDays = value; return this;}

    public Object getUsersCommerceClaimId() { return usersCommerceClaimId; }
    public AssuranceDTO setUsersCommerceClaimId(Object value) { this.usersCommerceClaimId = value; return this;}

    public Object getUsersCommerceClaim() { return usersCommerceClaim; }
    public AssuranceDTO setUsersCommerceClaim(Object value) { this.usersCommerceClaim = value; return this;}

    public Object getReason() { return reason; }
    public AssuranceDTO setReason(Object value) { this.reason = value;return this; }

    public Object getClaimDate() { return claimDate; }
    public AssuranceDTO setClaimDate(Object value) { this.claimDate = value; return this;}

    public Object getCreatedAt() { return createdAt; }
    public AssuranceDTO setCreatedAt(Object value) { this.createdAt = value; return this; }

    public Object getLastUpdated() { return lastUpdated; }
    public AssuranceDTO setLastUpdated(Object value) { this.lastUpdated = value;  return this;}
}
