package com.mycompany.calculator;

public class Equation{
    
    String equation;
    
    Equation(String newEquation){
        changeEquation(newEquation);
    }
    
    void changeEquation(String newEquation){
        if (newEquation == null || newEquation.equals(""))
            equation = Core.spaceString("x");
        else {
            equation = Core.spaceString(newEquation);
            equation = Core.postfixConversion(equation);
        }
    }
    
    double getY(double x){
        String e = equation.replaceAll("x", Double.toString(x));

        return Core.solve(e);
    }
}