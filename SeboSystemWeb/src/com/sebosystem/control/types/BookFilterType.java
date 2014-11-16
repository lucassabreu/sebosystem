package com.sebosystem.control.types;

import com.sebosystem.dao.helper.EnumTypeKey;

public enum BookFilterType implements EnumTypeKey {
    BookTitle("title"), BookAuthor("author"), BookYear("year"), BookExcerpt("excerpt"), BookReview("review");

    private String key;

    BookFilterType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
