package com.example.scanapp.Model;

import java.text.DecimalFormat;

public class Functions {

    public double setFraction(double number){

        DecimalFormat df = new DecimalFormat("##.##");

        String dx = df.format(number).replace(",",".");
        number = Double.parseDouble(dx);



        return number;
    }
}
