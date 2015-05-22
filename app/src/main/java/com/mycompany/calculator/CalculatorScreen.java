package com.mycompany.calculator;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Arrays;


public class CalculatorScreen extends ActionBarActivity {
    public static final String TAG = "Calculator";
    public static final String[] OPERATIONS = {"+", "-", "*", "/", "^", "%"};
    public static final String[] NUMBERS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    // Controls whether or not a decimal can be placed
    public static boolean canDecimal = true;
    // Count for parentheses to see if ) can be entered
    public static int parenCount = 0;
    // Variable to hold stored number from MS button
    public static Double memStored = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_screen);

        TextView equation = (TextView) findViewById(R.id.Equation);
        equation.setMovementMethod(ScrollingMovementMethod.getInstance());
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

    public void addString(TextView target, final HorizontalScrollView scrollView, String s){

        boolean op = Arrays.asList(OPERATIONS).contains(s);
        String text = target.getText().toString();

        if (!op || s.equals("(") || s.equals(")")) {
            if (target.length() == 1 && text.charAt(0) == '0') {
                target.setText(s);
            } else {
                target.setText(text + s);
            }
        }
        else{
            if (target.getText().length() > 0){
                // Append numbers normally
                if (Arrays.asList(NUMBERS).contains(((Character)text.charAt(text.length() - 1)).toString())){
                    target.setText(text + s);
                }
                else if (text.charAt(text.length() - 1) == ')'){
                    target.setText(text + s);
                }
                // Be selective with operations
                else{
                    // Don't let operations stack
                    if (text.charAt(text.length()-1) != '-' || (text.charAt(text.length() - 1) == '-' && text.charAt(text.length() - 2) == '-')) {
                        StringBuilder temp = new StringBuilder(text);

                        // Remove negative if changing operation
                        if (text.charAt(text.length() - 2) == '-' && text.charAt(text.length() - 1) == '-')
                            temp.setLength(text.length() - 2);
                        else{
                            // otherwise just remove last operation
                            temp.setLength(text.length() - 1);
                        }

                        // append the text
                        temp.append(s);
                        target.setText(temp.toString());
                    }
                    // Negatives are okay
                    else{
                        if (s.equals("-"))
                            target.setText(text + s);
                        else{
                            StringBuilder temp = new StringBuilder(text);
                            temp.setLength(text.length() - 1);
                            temp.append(s);
                            target.setText(temp.toString());
                        }
                    }
                }
            }
        }

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });
    }

    public void answerEquation(View view){
        TextView equation = (TextView) findViewById(R.id.Equation);
        final HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.EquationScroll);

        String result = equation.getText().toString();
        Log.d(TAG, result);
        result = Core.spaceString(result);
        result = Core.postfixConversion(result);

        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);

        equation.setText(format.format(Core.solve(result)));

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });
    }

    public void buttonPress(View view){
        TextView equation = (TextView) findViewById(R.id.Equation);
        HorizontalScrollView scroll = (HorizontalScrollView) findViewById(R.id.EquationScroll);

        // Find out which button was pressed
        switch (view.getId()){

            // Buttons with purpose
            case(R.id.Delete):
                equation.setText("0");
                break;
            case(R.id.History): break;

            case(R.id.MemStore):
                answerEquation(view);
                memStored = Double.parseDouble(equation.getText().toString());
                Log.d(TAG, "Stored " + memStored + " in memory");
                break;
            case(R.id.MemRecall):
                if (memStored!=null) {
                    DecimalFormat format = new DecimalFormat();
                    format.setDecimalSeparatorAlwaysShown(false);

                    addString(equation, scroll, format.format(memStored).toString());
                    Log.d(TAG, "Recalled " + memStored);
                }
                break;
            case(R.id.MemAdd): break;
            case(R.id.MemSub): break;

            // Operations
            case(R.id.Add):
                addString(equation, scroll, "+");
                canDecimal = true;
                break;
            case(R.id.Subtract):
                addString(equation, scroll, "-");
                canDecimal = true;
                break;
            case(R.id.Multiply):
                addString(equation, scroll, "*");
                canDecimal = true;
                break;
            case(R.id.Divide):
                addString(equation, scroll, "/");
                canDecimal = true;
                break;
            /*case(R.id.Exponent):
                addString(equation, scroll, "^");
                break;*/
            case(R.id.Percent):
                addString(equation, scroll, "%");
                break;
            case(R.id.Negative):
                addString(equation, scroll, "-");
                break;
            case(R.id.Decimal):
                if(canDecimal) {
                    addString(equation, scroll, ".");
                    canDecimal = false;
                }
                break;
            case(R.id.OpenParen):
                addString(equation, scroll, "(");
                canDecimal = true;
                parenCount++;
                break;
            case(R.id.CloseParen):
                if (parenCount > 0) {
                    addString(equation, scroll, ")");
                    canDecimal = true;
                    parenCount--;
                }
                break;

            // Numbers
            case(R.id.Zero): addString(equation, scroll, "0"); break;
            case(R.id.One): addString(equation, scroll, "1"); break;
            case(R.id.Two): addString(equation, scroll, "2"); break;
            case(R.id.Three): addString(equation, scroll, "3"); break;
            case(R.id.Four): addString(equation, scroll, "4"); break;
            case(R.id.Five): addString(equation, scroll, "5"); break;
            case(R.id.Six): addString(equation, scroll, "6"); break;
            case(R.id.Seven): addString(equation, scroll, "7"); break;
            case(R.id.Eight): addString(equation, scroll, "8"); break;
            case(R.id.Nine): addString(equation, scroll, "9"); break;
        }
    }

}
