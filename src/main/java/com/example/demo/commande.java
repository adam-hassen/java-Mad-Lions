package com.example.demo;

import java.sql.Date;

public class commande {
    private Integer id;
    private Integer customerID;
    private Double total;
    private Date date;

    public commande(Integer id, Integer customerID, Double total, Date date) {
        this.id = id;
        this.customerID = customerID;
        this.total = total;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
