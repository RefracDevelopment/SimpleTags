package me.refracdevelopment.simpletags.manager.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SelectCall {

    void call(ResultSet resultSet) throws SQLException;
}