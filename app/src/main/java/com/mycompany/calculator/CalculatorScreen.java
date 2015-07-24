package com.mycompany.calculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CalculatorScreen extends AppCompatActivity implements BasicKeypad.OnFragmentInteractionListener, AdvancedKeypad.OnFragmentInteractionListener{
    public static final String TAG = "Calculator";

    // Variable to clear edittext when number pressed after equation solve
    public static boolean solved = false;

    // Create a history object to store the history
    public History history;

    public void onFragmentInteraction(Uri uri){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_screen);

        // Initialize history
        history = new History(20);

        // find toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup spinner in toolbar
        Spinner spinner = (Spinner) findViewById(R.id.ToolbarSpinner);
        spinner.setAdapter(SharedFunctions.setupSpinner(getSupportActionBar(), getResources()));
        spinner.setOnItemSelectedListener(new SharedFunctions.toolbarSpinnerEvents());

        // Initialize equation entry parameters
        final EditText equation = (EditText) findViewById(R.id.Equation);
        equation.requestFocus();
        equation.setSelection(equation.getText().length());

        // Prevent soft keyboard from opening
        equation.setOnTouchListener(Input.hideSoftKeyboard);

        // Long hold delete to clear
        Button delete = (Button) findViewById(R.id.Delete);
        delete.setOnLongClickListener(Input.clear);

        Input.initKeypad(toolbar.getRootView(), getSupportFragmentManager(), false);
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

    public void delete(View view){
        Input.backspace((EditText) findViewById(R.id.Equation));
    }

    public void answerEquation(View view){
        EditText equation = (EditText) findViewById(R.id.Equation);
        String text = equation.getText().toString();
        Log.d(TAG, text);
        String result = Core.spaceString(text);
        result = Core.postfixConversion(result);

        // Format decimal to remove trailing zeros on whole numbers
        DecimalFormat format = new DecimalFormat("#.######");
        format.setGroupingSize(0);
        format.setDecimalSeparatorAlwaysShown(false);
        String finalAnswer = format.format(Core.solve(result));

        history.addItem(text, Double.parseDouble(finalAnswer));
        equation.setText(finalAnswer);
        equation.requestFocus();
        equation.setSelection(equation.getText().length());
        solved = true;
    }

    public void buttonPress(View view) {
        if (view.getId() == R.id.Equals) {
            answerEquation(view);
            return;
        }

        EditText equation = (EditText) findViewById(R.id.Equation);
        final Integer[] NUMBERS = {R.id.Zero, R.id.One, R.id.Two, R.id.Three, R.id.Four, R.id.Five,
                R.id.Six, R.id.Seven, R.id.Eight, R.id.Nine};

        // If button is a number and equation was just solved, clear the box
        if (Arrays.asList(NUMBERS).contains(view.getId()) && solved){
            equation.setText("0");
            Log.i("CALC", "Clearing equation box");
        }

        String s = Input.getButtonStr(view.getId());
        Input.addStr(equation, s);

        solved = false;
    }

    public void openHistory(View view){
        final EditText equation = (EditText) findViewById(R.id.Equation);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        List<String> itemList = new ArrayList<>();

        // Populate a list to display in the dialog
        for (int i = 0; i < history.historyQueue.size(); i++){
            History.HistoryItem temp = history.getItem(i + 1);
            itemList.add(temp.getEquation() + "=" + temp.getAnswer());
        }
        final CharSequence[] dialogItems = itemList.toArray(new String[itemList.size()]);

        Log.i("History", "History dialog populated");

        dialogBuilder.setTitle(R.string.history);
        dialogBuilder.setItems(dialogItems, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Place equation into equation
                Log.i("History", Integer.toString(which));
                String historyText = history.getItem(which+1).getEquation();
                equation.setText(historyText);
                dialog.dismiss();

                // Reclaim focus and position cursor
                equation.requestFocus();
                equation.setSelection(equation.getText().length());
            }
        });

        // Create and display the dialog
        AlertDialog dialog = dialogBuilder.create();
        Log.i("History", "Dialog will be shown");
        dialog.show();
    }
}
