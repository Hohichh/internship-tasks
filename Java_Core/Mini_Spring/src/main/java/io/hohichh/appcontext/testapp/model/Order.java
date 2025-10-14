package io.hohichh.appcontext.testapp.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Order {
    private UUID id;
    private String product;
    private String description;
    private UUID userId;
}
