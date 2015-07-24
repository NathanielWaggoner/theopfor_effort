package com.mycompany.calculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GraphingScreen extends AppCompatActivity implements GraphTable.OnFragmentInteractionListener {

    SharedPreferences prefs;
    SharedPreferences.Editor prefsEdit;

    @Override
    public void onFragmentInteraction(Uri uri) {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphing_screen);

        // setup equations
        prefs = getSharedPreferences("Equations", 0);
        prefsEdit = prefs.edit();
        // Make sure that all the keys exists, we don't need null values
        for (int i = 1; i <= 6; i++){
            String key = "Y" + Integer.toString(i);
            Log.i("GraphingKeys", key);

            if (!prefs.contains(key)){
                prefsEdit.putString(key, "0");
            }
        }
        // Apply changes
        prefsEdit.commit();

        // find toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Make spinner for toolbar and set it to "Graphing"
        Spinner toolbarSpinner = (Spinner) findViewById(R.id.ToolbarSpinner);
        toolbarSpinner.setAdapter(SharedFunctions.setupSpinner(getSupportActionBar(), getResources()));
        toolbarSpinner.setSelection(1);
        toolbarSpinner.setOnItemSelectedListener(new SharedFunctions.toolbarSpinnerEvents());

        // Instantiate spinner for equation name
        Spinner spinner = (Spinner) findViewById(R.id.GraphEquationSpinner);
        String[] graphEquationNames = getResources().getStringArray(R.array.GraphSpinnerItems);
        List<String> graphEquationItems = new ArrayList<>(Arrays.asList(graphEquationNames));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_graphing_layout, graphEquationItems);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new equationSpinnerEvents());

        // Set texview to display corresponding equation
        TextView display = (TextView) findViewById(R.id.GraphEquationDisplay);
        display.setText(prefs.getString("Y" + Integer.toString(spinner.getSelectedItemPosition() + 1), ""));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0){
            // Update textview
            TextView display = (TextView) findViewById(R.id.GraphEquationDisplay);
            Spinner spinner = (Spinner) findViewById(R.id.GraphEquationSpinner);
            String key = "Y" + Integer.toString(spinner.getSelectedItemPosition() + 1);
            display.setText(prefs.getString(key, ""));
        }
    }

    public class equationSpinnerEvents implements Spinner.OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            TextView display = (TextView) findViewById(R.id.GraphEquationDisplay);
            String key = "Y" + Integer.toString(pos + 1);
            String value = prefs.getString(key, "");
            display.setText(value);
            Log.i("GRAPHING", key + " set to " + value);
        }

        public void onNothingSelected(AdapterView<?> parent) {}
    }

    public void launchEntryActivity(View v){
        Spinner spinner = (Spinner) findViewById(R.id.GraphEquationSpinner);
        TextView display = (TextView) findViewById(R.id.GraphEquationDisplay);
        String key = "Y" + Integer.toString(spinner.getSelectedItemPosition() + 1);
        Intent newActivity = new Intent(v.getContext(), GraphingEntryScreen.class);
        newActivity.putExtra("key", key);
        newActivity.putExtra("equation", display.getText());
        Log.i("GRAPHING", key);
        startActivityForResult(newActivity, 0);
    }
}
