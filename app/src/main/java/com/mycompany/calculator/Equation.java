package com.mycompany.calculator;

public class Equation{
    
    String equation;
    
    Equation(String newEquation){
        changeEquation(newEquation);
    }
    
    void changeEquation(String newEquation){
        if (newEquation == null)
            equation = Core.spaceString("x");
        else
            equation = Core.spaceString(newEquation);
    }
    
    double getY(double x){
        String e = equation.replaceAll("x", Double.toString(x));
        e = Core.postfixConversion(e);
        return Core.solve(e);
    }
}