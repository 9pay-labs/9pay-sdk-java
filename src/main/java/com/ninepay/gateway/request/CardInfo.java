package com.ninepay.gateway.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardInfo {
    @JsonProperty("card_number")
    private String cardNumber;

    @JsonProperty("hold_name")
    private String cardHolderName;

    @JsonProperty("exp_month")
    private String expirationMonth;

    @JsonProperty("exp_year")
    private String expirationYear;

    @JsonProperty("cvv")
    private String cvv;
}
