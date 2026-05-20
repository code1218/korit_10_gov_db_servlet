package com.korit.databaseservlet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korit.databaseservlet.entity.Restaurant;
import com.korit.databaseservlet.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/restaurants")
public class RestaurantController extends HttpServlet {
    // CRUD

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader br = req.getReader();
        StringBuilder requestBody = new StringBuilder();

        while (true) {
            String line = br.readLine();
            if (line == null) {
                break;
            }
            requestBody.append(line);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Restaurant restaurant = objectMapper.readValue(requestBody.toString(), Restaurant.class);

        try {
            Connection connection = DBUtil.getConnection();
            String sql = """
                    insert into restaurants values 
                        (null, ?, ?, ?, ?, default, default)""";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, restaurant.getRestaurantName());
            pstmt.setString(2, restaurant.getCategory());
            pstmt.setString(3, restaurant.getAddress());
            pstmt.setDouble(4, restaurant.getRating());
            int successCount = pstmt.executeUpdate();

            Map<String, String> responseBody = Map.of(
                    "message", "success: " + successCount + "건"
            );
            resp.setContentType("application/json");
            resp.getWriter().println(objectMapper.writeValueAsString(responseBody));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Restaurant> restaurants = new ArrayList<>();

        try {
            Connection connection = DBUtil.getConnection();
            String sql = """
                select 
                    * 
                from 
                    restaurants""";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String restaurantName = rs.getString("name");
                String category = rs.getString("category");
                String address = rs.getString("address");
                double rating = rs.getDouble("rating");
                Timestamp timestamp = rs.getTimestamp("created_at");
                int minOrderAmount = rs.getInt("min_order_amount");
                Restaurant restaurant = Restaurant.builder()
                        .id(id)
                        .restaurantName(restaurantName)
                        .category(category)
                        .address(address)
                        .rating(rating)
                        .createAt(timestamp.toLocalDateTime().toString())
                        .minOrderAmount(minOrderAmount)
                        .build();
                restaurants.add(restaurant);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = objectMapper.writeValueAsString(restaurants);
        resp.setContentType("application/json");
        resp.getWriter().println(responseJson);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
