package com.sebosystem.dao;

import com.sebosystem.dao.helper.EnumTypeKey;

public enum TransactionStatus implements EnumTypeKey {
    Open, InProgress, Closed;

    protected String key;

    private TransactionStatus() {
        this.key = this.name().toLowerCase();
    }

    public String getKey() {
        return this.key;
    };
}
