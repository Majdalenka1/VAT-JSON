package com.entego.vat_json;

import java.math.BigDecimal;

public class Country {
    private String abbreviation;
    private String name;
    private double fullVat;
    private double lowerVat;

    public Country(String abbreviation, String name, double fullVat, double lowerVat) {
        this.abbreviation = abbreviation;
        this.name = name;
        this.fullVat = fullVat;
        this.lowerVat = lowerVat;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getName() {
        return name;
    }

    public double getFullVat() {
        return fullVat;
    }

    public double getLowerVat() {
        return lowerVat;
    }

    @Override
    public String toString() {
        return "Country{" +
                "abbreviation='" + abbreviation + '\'' +
                ", name='" + name + '\'' +
                ", full_vat=" + fullVat +
                ", lower_vat=" + lowerVat +
                "}\n";
    }
}
