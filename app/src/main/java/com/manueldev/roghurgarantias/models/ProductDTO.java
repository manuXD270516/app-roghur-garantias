package com.manueldev.roghurgarantias.models;

import java.util.List;

public class ProductDTO {

    private long productId;
    private String serialCode;
    private String codeArticle;
    private String name;
    private String mark;
    private long expirationDays;
    private List<Object> activateAssurances;
    private Object createdAt;
    private Object lastUpdated;

    public ProductDTO(){}

    public long getProductId() { return productId; }
    public ProductDTO setProductId(long value) { this.productId = value; return this; }

    public String getSerialCode() { return serialCode; }
    public ProductDTO setSerialCode(String value) { this.serialCode = value;return this; }

    public String getCodeArticle() { return codeArticle; }
    public ProductDTO setCodeArticle(String value) { this.codeArticle = value; return this;}

    public String getName() { return name; }
    public ProductDTO setName(String value) { this.name = value; return this;}

    public String getMark() { return mark; }
    public ProductDTO setMark(String value) { this.mark = value; return this;}

    public long getExpirationDays() { return expirationDays; }
    public ProductDTO setExpirationDays(long value) { this.expirationDays = value;return this; }

    public List<Object> getActivateAssurances() { return activateAssurances; }
    public ProductDTO setActivateAssurances(List<Object> value) { this.activateAssurances = value; return this;}

    public Object getCreatedAt() { return createdAt; }
    public ProductDTO setCreatedAt(Object value) { this.createdAt = value; return this;}

    public Object getLastUpdated() { return lastUpdated; }
    public ProductDTO setLastUpdated(Object value) { this.lastUpdated = value; return this;}
}
