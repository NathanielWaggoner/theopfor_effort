package com.mycompany.calculator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;

class Input{

    public static final EditText.OnTouchListener hideSoftKeyboard = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.onTouchEvent(event);
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            return true;
        }
    };

    public static final Button.OnLongClickListener clear = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            EditText equation = (EditText) v.getRootView().findViewById(R.id.Equation);
            equation.setText("0");
            equation.requestFocus();
            equation.setSelection(equation.getText().length());
            return true;
        }
    };

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
        final String[] NUMBERS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "x"};

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

    public static void initKeypad(View v, FragmentManager manager, boolean graphing) {
        ViewPager mPager = (ViewPager) v.findViewById(R.id.KeypadSlider);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(manager, graphing);
        mPager.setAdapter(mPagerAdapter);
    }

    private static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private boolean graphing;

        public ScreenSlidePagerAdapter(FragmentManager fm, boolean b){
            super(fm);
            graphing = b;
        }

        @Override
        public Fragment getItem(int position){
            if (position == 0) {
                Bundle args = new Bundle();
                args.putBoolean("GRAPHING", graphing);
                BasicKeypad b = new BasicKeypad();
                b.setArguments(args);
                return b;
            }
            else
                return new AdvancedKeypad();
        }

        @Override
        public int getCount(){
            return 2;
        }
    }
}