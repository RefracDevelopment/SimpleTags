package me.refracdevelopment.simpletags.manager.data;

import java.sql.ResultSet;

public interface SelectCall {

    void call(ResultSet resultSet);
}