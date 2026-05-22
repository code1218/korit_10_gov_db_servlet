package com.korit.databaseservlet.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korit.databaseservlet.entity.CustomerAddress;
import com.korit.databaseservlet.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/customers/addresses")
public class CustomerAddressController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CustomerAddress> customerAddresses = objectMapper.readValue(req.getReader(), new TypeReference<List<CustomerAddress>>() {});

        try {
            Connection connection = DBUtil.getConnection();
            StringBuilder stringBuilder = new StringBuilder("insert into customer_addresses values ");
            for (int i = 0; i < customerAddresses.size(); i++) {
                stringBuilder.append("(null, ?, ?)");
                if (i < customerAddresses.size() - 1) {
                    stringBuilder.append(", ");
                }
            }
            String sql = stringBuilder.toString();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            for (int i = 0; i < customerAddresses.size(); i++) {
                pstmt.setInt(((i + 1) * 2) -1, customerAddresses.get(i).getCustomerId());
                pstmt.setString((i + 1) * 2, customerAddresses.get(i).getAddress());
            }
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

    }
}
