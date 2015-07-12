package com.mycompany.calculator;

import android.util.Log;
import android.widget.EditText;

import java.util.Arrays;

class Input{

    public static String getButtonStr(int button){
        Log.d("Input", "Button Press");
        String result = "";

        // Find out which button was pressed
        switch (button){
            
            // Operations
            /*TODO: Condense to addString(buttonText)*/
            case(R.id.Add):
                result = "+";
                break;
            case(R.id.Subtract):
                result = "-";
                break;
            case(R.id.Multiply):
                result = "*";
                break;
            case(R.id.Divide):
                result = "/";
                break;
            case(R.id.Percent):
                result = "%";
                break;
            case(R.id.Negative):
                result = "-";
                break;
            case(R.id.Decimal):
                result = ".";
                break;
            case(R.id.OpenParen):
                result = "(";
                break;
            case(R.id.CloseParen):
                result = ")";
                break;

            case(R.id.Exponent):
                result = "^";
                break;
            case(R.id.SquareRoot):
                result = "√(";
                break;
            case(R.id.Factorial):
                result = "!";
                break;
            case(R.id.Sine):
                result = "sin(";
                break;
            case(R.id.Cosine):
                result = "cos(";
                break;
            case(R.id.Tangent):
                result = "tan(";
                break;
            case(R.id.ScientificNotation):
                result = "*10^";
                break;
            case(R.id.Logarithm):
                result = "log(";
                break;
            case(R.id.NaturalLogarithm):
                result = "ln(";
                break;
            case(R.id.AbsoluteValue):
                result = "abs(";
                break;
            case(R.id.Pi):
                result = "π";
                break;
            case(R.id.E):
                result = "e";
                break;

            // Numbers
            // LOVE THE CONDITIONALS.
            case(R.id.Zero): result = "0"; break;
            case(R.id.One): result = "1"; break;
            case(R.id.Two): result = "2"; break;
            case(R.id.Three): result = "3"; break;
            case(R.id.Four): result = "4"; break;
            case(R.id.Five): result = "5"; break;
            case(R.id.Six): result = "6"; break;
            case(R.id.Seven): result = "7"; break;
            case(R.id.Eight): result = "8"; break;
            case(R.id.Nine): result = "9"; break;

            // x entered for Equals when in graphing mode b/c IDs cannot change
            case(R.id.Equals): result = "x"; break;
        }
        
        return result;
    }

    public static void addStr(EditText target, String s){
        final String[] OPERATIONS = {"+", "-", "*", "/", "^", "%"};
        final String[] NUMBERS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        boolean op = Arrays.asList(OPERATIONS).contains(s);
        String text = target.getText().toString();

        // Used for the cursor
        int start = Math.max(target.getSelectionStart(), 0);
        int end = Math.max(target.getSelectionEnd(), 0);
        // Use this for entering in the string:
        // target.getText().replace(Math.min(start, end), Math.max(start, end), s, 0, s.length());

        if (!op || s.equals("(") || s.equals(")")) {
            if (target.length() == 1 && text.charAt(0) == '0') {
                target.setText(s);
                target.requestFocus();
                target.setSelection(target.getText().length());
            } else {
                //target.append(s);
                target.getText().replace(Math.min(start, end), Math.max(start, end), s, 0, s.length());
            }
        }
        else {
            if (target.getText().length() > 0) {
                // Append numbers normally
                if (Arrays.asList(NUMBERS).contains(((Character) text.charAt(start - 1)).toString())) {
                    target.getText().replace(Math.min(start, end), Math.max(start, end), s, 0, s.length());
                } else if (text.charAt(start - 1) == ')') {
                    target.getText().replace(Math.min(start, end), Math.max(start, end), s, 0, s.length());
                }
                // Be selective with operations
                else {
                    // Don't let operations stack
                    if (text.charAt(start - 1) != '-' || (text.charAt(start - 1) == '-' && text.charAt(start - 2) == '-')) {
                        StringBuilder temp = new StringBuilder(text);

                        // Remove negative if changing operation
                        if (text.charAt(start - 2) == '-' && text.charAt(start - 1) == '-') {
                            temp.setCharAt(start - 2, s.charAt(0));
                            temp.deleteCharAt(start - 1);
                            target.setText(temp.toString());
                            target.requestFocus();
                            target.setSelection(start - 1);
                        }
                        else {
                            // otherwise just remove last operation
                            temp.setCharAt(start - 1, s.charAt(0));
                            target.setText(temp.toString());
                            target.requestFocus();
                            target.setSelection(start);
                        }
                    }
                    // Negatives are okay
                    else {
                        if (s.equals("-"))
                            //target.append(s);
                            target.getText().replace(Math.min(start, end), Math.max(start, end), s, 0, s.length());
                        else {
                            StringBuilder temp = new StringBuilder(text);
                            temp.setCharAt(start - 1, s.charAt(0));
                            target.setText(temp.toString());
                            target.requestFocus();
                            target.setSelection(start);
                        }
                    }
                }
            }
        }
    }

    public static void backspace(EditText equation){
        int start = equation.getSelectionStart();
        int end = equation.getSelectionEnd();

        if (equation.getText().length() == 1) {
            equation.setText("0");
            equation.requestFocus();
            equation.setSelection(equation.getText().length());
        }
        else if (start > 0){
            if (start == end) {
                StringBuilder temp = new StringBuilder(equation.getText());
                temp.deleteCharAt(start - 1);
                equation.setText(temp.toString());
                equation.requestFocus();
                equation.setSelection(start - 1);
            }
            else{
                StringBuilder temp = new StringBuilder(equation.getText());
                temp.delete(start, end);
                equation.setText(temp.toString());
                equation.requestFocus();
                equation.setSelection(start);
            }
        }
        else{
            if (start != end){
                StringBuilder temp = new StringBuilder(equation.getText());
                temp.delete(start, end);

                if (temp.length() == 0)
                    temp.append("0");

                equation.setText(temp.toString());
                equation.requestFocus();
                equation.setSelection(start);
            }
        }
    }
}