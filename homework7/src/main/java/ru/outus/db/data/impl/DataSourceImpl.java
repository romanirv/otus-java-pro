package ru.outus.db.data.impl;

import ru.outus.db.data.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class DataSourceImpl implements DataSource {
    private final HikariDataSource dataSource;

    public DataSourceImpl(String url) {
        HikariConfig configuration = new HikariConfig();
        configuration.setJdbcUrl(url);
        this.dataSource = new HikariDataSource(configuration);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}