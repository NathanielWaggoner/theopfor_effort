package com.mycompany.calculator;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.util.Arrays;


public class CalculatorScreen extends ActionBarActivity {
    public static final String TAG = "Calculator";
    public static final String[] OPERATIONS = {"+", "-", "*", "/", "^", "%"};
    public static final String[] NUMBERS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    /*
     * Probably a better way to do this but...
     * here are a bunch of conditionals because guess
     * who hates stupid crashes? Me.
     */

    // Controls whether or not a decimal can be placed
    public static boolean canDecimal = true;
    // Make sure empty parentheses aren't a thing...
    public static boolean canCloseParen = false;
    // Count for parentheses to see if ) can be entered
    public static int parenCount = 0;
    // Make sure equal is actually possible, none of this "1+" then crash crap...
    public static boolean canEqual = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_screen);

        final EditText equation = (EditText) findViewById(R.id.Equation);
        equation.requestFocus();
        equation.setSelection(equation.getText().length());

        equation.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });

        Button delete = (Button) findViewById(R.id.Delete);
        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                equation.setText("0");
                equation.requestFocus();
                equation.setSelection(equation.getText().length());
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculator_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addString(EditText target, String s){

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

    public void answerEquation(View view){
        EditText equation = (EditText) findViewById(R.id.Equation);
        String result = equation.getText().toString();
        Log.d(TAG, result);
        result = Core.spaceString(result);
        result = Core.postfixConversion(result);

        // Format decimal to remove trailing zeros on whole numbers
        DecimalFormat format = new DecimalFormat();
        format.setGroupingSize(0);
        format.setDecimalSeparatorAlwaysShown(false);

        equation.setText(format.format(Core.solve(result)));
        equation.requestFocus();
        equation.setSelection(equation.getText().length());
    }

    public void buttonPress(View view){
        EditText equation = (EditText) findViewById(R.id.Equation);
        int start = equation.getSelectionStart();
        int end = equation.getSelectionEnd();

        // Find out which button was pressed
        switch (view.getId()){

            // Buttons with purpose
            case(R.id.Delete):
                //int start = equation.getSelectionStart();
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
                break;
            case(R.id.History): break;
            case(R.id.Equals):
                //if (canEqual && parenCount == 0)
                    answerEquation(view);
                break;

            // Operations
            case(R.id.Add):
                addString(equation, "+");
                canDecimal = true;
                canCloseParen = false;
                canEqual = false;
                break;
            case(R.id.Subtract):
                addString(equation, "-");
                canDecimal = true;
                canCloseParen = false;
                canEqual = false;
                break;
            case(R.id.Multiply):
                addString(equation, "*");
                canDecimal = true;
                canCloseParen = false;
                canEqual = false;
                break;
            case(R.id.Divide):
                addString(equation, "/");
                canDecimal = true;
                canCloseParen = false;
                canEqual = false;
                break;
            /*case(R.id.Exponent):
                addString(equation, "^");
                canCloseParen = false;
                canDecimal = true;
                canEqual = false;
                break;*/
            case(R.id.Percent):
                addString(equation, "%");
                canCloseParen = true;
                break;
            case(R.id.Negative):
                addString(equation, "-");
                canCloseParen = false;
                canEqual = false;
                break;
            case(R.id.Decimal):
                //if(canDecimal) {
                    addString(equation, ".");
                    canDecimal = false;
                    canEqual = false;
                    canCloseParen = false;
                //}
                break;
            case(R.id.OpenParen):
                addString(equation, "(");
                canDecimal = true;
                canCloseParen = false;
                parenCount++;
                canEqual = false;
                break;
            case(R.id.CloseParen):
                //if (parenCount > 0 && canCloseParen) {
                    addString(equation, ")");
                    canDecimal = true;
                    parenCount--;
                    canEqual = true;
                //}
                break;

            // Numbers
            // LOVE THE CONDITIONALS. YAYYYYYYY
            case(R.id.Zero): addString(equation, "0"); canCloseParen = true; canEqual = true; break;
            case(R.id.One): addString(equation, "1"); canCloseParen = true; canEqual = true; break;
            case(R.id.Two): addString(equation, "2"); canCloseParen = true; canEqual = true; break;
            case(R.id.Three): addString(equation, "3"); canCloseParen = true; canEqual = true; break;
            case(R.id.Four): addString(equation, "4"); canCloseParen = true; canEqual = true; break;
            case(R.id.Five): addString(equation, "5"); canCloseParen = true; canEqual = true; break;
            case(R.id.Six): addString(equation, "6"); canCloseParen = true; canEqual = true; break;
            case(R.id.Seven): addString(equation, "7"); canCloseParen = true; canEqual = true; break;
            case(R.id.Eight): addString(equation, "8"); canCloseParen = true; canEqual = true; break;
            case(R.id.Nine): addString(equation, "9"); canCloseParen = true; canEqual = true; break;
        }
    }
}
