package com.sebosystem.dao;

import com.sebosystem.dao.helper.EnumTypeKey;

public enum RequestType implements EnumTypeKey {
    BookDuplicated("book_duplicated"),
    AuthorDuplicated("author_duplicated"),
    BookRevision("book_revision"),
    AuthorRevision("author_revision"),
    ReviewReport("review_report"),
    ExcerptReport("excerpt_report");

    protected String key;

    RequestType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
