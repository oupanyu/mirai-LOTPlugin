package top.oupanyu.database.impl;

import java.sql.ResultSet;

public interface DatabaseImpl {
    ResultSet execute(String sql);
}
