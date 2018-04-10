package com.francony.romain.outerspacemanager.model;

public class FleetAmount {
    private Integer amount;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "FleetAmount{" +
                "amount=" + amount +
                '}';
    }
}
