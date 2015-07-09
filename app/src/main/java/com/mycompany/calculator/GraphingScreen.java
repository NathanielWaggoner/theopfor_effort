package com.mycompany.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GraphingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphing_screen);

        // find toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Make spinner for toolbar
        Spinner toolbarSpinner = (Spinner) findViewById(R.id.ToolbarSpinner);
        toolbarSpinner.setAdapter(SharedFunctions.setupSpinner(getSupportActionBar(), getResources()));

        // Instantiate spinner for equation name
        Spinner spinner = (Spinner) findViewById(R.id.GraphEquationSpinner);
        String[] graphEquationNames = getResources().getStringArray(R.array.GraphSpinnerItems);
        List<String> graphEquationItems = new ArrayList<>(Arrays.asList(graphEquationNames));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_graphing_layout, graphEquationItems);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graphing_screen, menu);
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
}
