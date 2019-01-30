package com.test.restaurant.test.utils;

import java.sql.ResultSet;

public interface ResultSetHandler<T> {
    public <T> T handle(ResultSet rs);
}
