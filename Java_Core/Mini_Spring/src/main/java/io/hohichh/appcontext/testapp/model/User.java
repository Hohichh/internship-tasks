package io.hohichh.appcontext.testapp.model;


import lombok.Data;

import java.util.UUID;

@Data
public class User {
    private UUID id;
    private String name;
    private String email;
}
