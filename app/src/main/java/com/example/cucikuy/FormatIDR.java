package com.example.cucikuy;

import java.text.NumberFormat;
import java.util.Locale;
public class FormatIDR {
    public static String FormatIDR(double angka) {
        return NumberFormat.getCurrencyInstance(new Locale("in", "ID"))
                .format(angka)
                .replaceAll("[Rp\\s]", "")
                .replace(",00", "");
    }
}
