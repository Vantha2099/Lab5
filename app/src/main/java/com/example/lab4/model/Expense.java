package com.example.lab4.model;

import com.google.gson.annotations.JsonAdapter;
import com.example.lab4.api.ISO8601DateAdapter;

public class Expense {
    private String id;  // Changed from int to String
    private String amount;
    private String currency;

    @JsonAdapter(ISO8601DateAdapter.class)
    private String createdDate;

    private String category;
    private String remark;
    private String createdBy;  // New field

    // Constructor for API responses
    public Expense(String id, String amount, String currency, String createdDate,
                   String category, String remark, String createdBy) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.createdDate = createdDate;
        this.category = category;
        this.remark = remark;
        this.createdBy = createdBy;
    }

    // Getters
    public String getId() { return id; }
    public String getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getCreatedDate() { return createdDate; }
    public String getCategory() { return category; }
    public String getRemark() { return remark; }
    public String getCreatedBy() { return createdBy; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setAmount(String amount) { this.amount = amount; }
    public void setCurrency(String currency) { this.currency = currency; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
    public void setCategory(String category) { this.category = category; }
    public void setRemark(String remark) { this.remark = remark; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}