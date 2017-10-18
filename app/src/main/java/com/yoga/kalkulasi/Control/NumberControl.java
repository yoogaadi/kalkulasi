package com.yoga.kalkulasi.Control;
import java.text.*;

public class NumberControl
{
    public String getNumberFormat(double nominal ){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        
        return kursIndonesia.format(nominal);
    }
}
