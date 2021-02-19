package com.udacity.pricing.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceList {
    private List<Price> prices;

    public PriceList() { prices = new ArrayList<>(); }
}
