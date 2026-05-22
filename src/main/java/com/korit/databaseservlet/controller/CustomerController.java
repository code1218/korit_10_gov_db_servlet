package com.korit.databaseservlet.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.korit.databaseservlet.entity.Customer;
import com.korit.databaseservlet.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/customers")
public class CustomerController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customer = objectMapper.readValue(req.getReader(), Customer.class);

        try {
            Connection connection = DBUtil.getConnection();
            String sql = """
                    insert into customers values (null, ?, ?)""";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getPhone());
            int successCount = pstmt.executeUpdate();

            Map<String, String> response = Map.of(
                    "message", "successCount: " + successCount
            );
            resp.setContentType("application/json");
            resp.getWriter().println(objectMapper.writeValueAsString(response));
        } catch (SQLException e) {
            e.printStackTrace();
            Map<String, String> response = Map.of(
                    "message", e.getMessage()
            );
            resp.setStatus(400);
            resp.setContentType("application/json");
            resp.getWriter().println(objectMapper.writeValueAsString(response));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("query");
        List<Customer> customers = new ArrayList<>();

        try {
            Connection connection = DBUtil.getConnection();
            String sql = """
                select
                    *
                from
                    customers
                where
                    name like concat('%', ?, '%')
                    or phone like concat('%', ?, '%')""";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, query);
            pstmt.setString(2, query);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                customers.add(Customer.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .phone(rs.getString("phone"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        resp.setContentType("application/json");
        resp.getWriter().println(objectMapper.writeValueAsString(customers));
    }
}











