package com.mycompany.calculator;

import android.content.SharedPreferences;
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


public class GraphingEntryScreen extends AppCompatActivity implements BasicKeypad.OnFragmentInteractionListener, AdvancedKeypad.OnFragmentInteractionListener {

    public void onFragmentInteraction(Uri uri) {}

    SharedPreferences prefs;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_screen);

        // Setup preferences
        prefs = getSharedPreferences("Equations", 0);

        // Grab key to use when saving
        Bundle b = getIntent().getExtras();
        key = b.getString("key");

        // find toolbar and remove navigation spinner
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        toolbar.removeView(findViewById(R.id.ToolbarSpinner));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Keep soft keyboard from opening
        EditText equation = (EditText) findViewById(R.id.Equation);
        equation.setOnTouchListener(Input.hideSoftKeyboard);
        // Set equation to be equal to passed in value
        equation.setText(b.getString("equation"));

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

        if (view.getId() == R.id.Answer)
            s = "x";
        else if (view.getId() == R.id.Equals){
            saveEquation();
            finish();
        }

        Input.addStr(equation, s);
    }

    public void saveEquation() {
        EditText equationView = (EditText) findViewById(R.id.Equation);
        String equation = equationView.getText().toString();
        SharedPreferences.Editor prefsEdit = prefs.edit();
        Log.i("Equation Entry", "Saving equation: " + equation);

        prefsEdit.putString(key, equation);
        prefsEdit.commit();
    }

    public void delete(View view){
        Input.backspace((EditText) findViewById(R.id.Equation));
    }

    public void openHistory(View view) {}
}
