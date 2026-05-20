package com.korit.databaseservlet.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) {
                throw new RuntimeException("db.properties 찾지 못함");
            }
            PROPS.load(is);
            Class.forName("com.mysql.cj.jdbc.Driver");  // mysql 라이브러리 드라이버 객체 생성
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = PROPS.getProperty("db.url");
        String username = PROPS.getProperty("db.username");
        String password = PROPS.getProperty("db.password");
        return DriverManager.getConnection(url, username, password);
    }
}
