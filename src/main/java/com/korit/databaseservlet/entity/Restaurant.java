package com.korit.databaseservlet.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    private int id;
    private String restaurantName;
    private String category;
    private String address;
    private double rating;
    private String createAt;
    private int minOrderAmount;
}
