package com.mycompany.calculator;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Arrays;


public class CalculatorScreen extends ActionBarActivity {
    public static final String[] OPERATIONS = {"+", "-", "*", "/"};
    public static final String[] NUMBERS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

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

        if (!op) {
            if (target.length() == 1 && text.charAt(0) == '0') {
                target.setText(s);
            } else {
                target.setText(text + s);
            }
        }
        else{
            if (target.getText().length() > 0){
                if (Arrays.asList(NUMBERS).contains(((Character)text.charAt(text.length() - 1)).toString())){
                    target.setText(text + s);
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

    public void addChar(View view){
        TextView equation = (TextView) findViewById(R.id.Equation);
        TextView answer = (TextView) findViewById(R.id.Answer);
        HorizontalScrollView scroll = (HorizontalScrollView) findViewById(R.id.EquationScroll);

        switch (view.getId()){

            // Buttons with purpose
            case(R.id.Clear):
                equation.setText("0");
                answer.setText("0");
                break;

            // Operations
            case(R.id.Add):
                addString(equation, scroll, "+");
                break;
            case(R.id.Subtract):
                addString(equation, scroll, "-");
                break;
            case(R.id.Multiply):
                addString(equation, scroll, "*");
                break;
            case(R.id.Divide):
                addString(equation, scroll, "/");
                break;
            case(R.id.Exponent):
                addString(equation, scroll, "^");
                break;
            case(R.id.Percent):
                addString(equation, scroll, "+");
                break;
            case(R.id.Negative):
                addString(equation, scroll, "-");
                break;
            case(R.id.Decimal):
                addString(equation, scroll, ".");
                break;

            // Numbers
            case(R.id.Zero):
                addString(equation, scroll, "0");
                break;
            case(R.id.One):
                addString(equation, scroll, "1");
                break;
            case(R.id.Two):
                addString(equation, scroll, "2");
                break;
            case(R.id.Three):
                addString(equation, scroll, "3");
                break;
            case(R.id.Four):
                addString(equation, scroll, "4");
                break;
            case(R.id.Five):
                addString(equation, scroll, "5");
                break;
            case(R.id.Six):
                addString(equation, scroll, "6");
                break;
            case(R.id.Seven):
                addString(equation, scroll, "7");
                break;
            case(R.id.Eight):
                addString(equation, scroll, "8");
                break;
            case(R.id.Nine):
                addString(equation, scroll, "9");
                break;
        }
    }

}
