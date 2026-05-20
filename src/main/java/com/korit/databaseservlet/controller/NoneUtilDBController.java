package com.korit.databaseservlet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korit.databaseservlet.entity.Menu;
import com.korit.databaseservlet.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/db/none/util")
public class NoneUtilDBController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Menu> menus = new ArrayList<>();
            Connection connection = DBUtil.getConnection();
            String sql = """
                select
                    *
                from
                    menus;
            """;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                Menu menu = Menu.builder()
                        .id(resultSet.getInt("id"))
                        .restaurantId(resultSet.getInt("restaurant_id"))
                        .menuName(resultSet.getString("menu_name"))
                        .price(resultSet.getInt("price"))
                        .description(resultSet.getString("description"))
                        .isAvailable(resultSet.getBoolean("is_available"))
                        .build();
                menus.add(menu);
                System.out.println(menu);
            }
            resp.setContentType("application/json");
            resp.setStatus(200);
            ObjectMapper objectMapper = new ObjectMapper();
            resp.getWriter().println(objectMapper.writeValueAsString(menus));
        } catch (Exception e) {

        }
    }

}














