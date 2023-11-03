package com.testPayPalIntegeration.app.paypalTestApp.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayOutOrder {

    private Double totalAmount;
    private String currency;
    private String receiver;
}
