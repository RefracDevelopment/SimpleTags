package me.refracdevelopment.simpletags.database;

import java.sql.ResultSet;

public interface SelectCall {

    void call(ResultSet resultSet);
}