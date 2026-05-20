package com.korit.databaseservlet.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Menu {
    private int id;
    private int restaurantId;
    private String menuName;
    private int price;
    private String description;
    private boolean isAvailable;
}
