package edu.csust.dao;

import java.sql.SQLException;
import java.util.List;

public interface BaseDao<T> {
    int insert(T t) throws SQLException;
    int update(T t) throws SQLException;
    int delete(int id) throws SQLException;
    T findById(int id) throws SQLException;
    List<T> findAll() throws SQLException;
}
