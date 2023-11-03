package com.testPayPalIntegeration.app.paypalTestApp.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    private double price;
    private String currency;
    private String description;
}
