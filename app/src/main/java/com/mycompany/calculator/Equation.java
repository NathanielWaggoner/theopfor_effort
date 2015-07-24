package com.mycompany.calculator;

public class Equation{
    
    String equation;
    
    Equation(String newEquation){
        changeEquation(newEquation);
    }
    
    void changeEquation(String newEquation){
        equation = Core.spaceString(newEquation);
    }
    
    double getY(double x){
        String e = equation.replaceAll("x", Double.toString(x));
        e = Core.postfixConversion(e);
        double answer = Core.solve(e);
        return answer;
    }
}