package com.mycompany.calculator;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class GraphingEntryScreen extends AppCompatActivity implements BasicKeypad.OnFragmentInteractionListener, AdvancedKeypad.OnFragmentInteractionListener {

    public void onFragmentInteraction(Uri uri) {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_screen);

        // find toolbar and remove navigation spinner
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        toolbar.removeView(findViewById(R.id.ToolbarSpinner));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Keep soft keyboard from opening
        EditText equation = (EditText) findViewById(R.id.Equation);
        equation.setOnTouchListener(Input.hideSoftKeyboard);

        // Long hold delete to clear
        Button delete = (Button) findViewById(R.id.Delete);
        delete.setOnLongClickListener(Input.clear);

        Input.initKeypad(toolbar.getRootView(), getSupportFragmentManager(), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graphing_entry_screen, menu);
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

    public void buttonPress(View view) {
        EditText equation = (EditText) findViewById(R.id.Equation);
        String s = Input.getButtonStr(view.getId());
        Input.addStr(equation, s);
    }

    public void delete(View view){
        Input.backspace((EditText) findViewById(R.id.Equation));
    }

    public void openHistory(View view) {}
}
