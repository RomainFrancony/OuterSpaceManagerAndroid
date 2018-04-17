package com.francony.romain.outerspacemanager.response;


public class ActionResponse {
    private String code;
    private long attackTime;

    public ActionResponse(String code, long attackTime) {
        this.code = code;
        this.attackTime = attackTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getAttackTime() {
        return attackTime;
    }

    public void setAttackTime(long attackTime) {
        this.attackTime = attackTime;
    }
}
