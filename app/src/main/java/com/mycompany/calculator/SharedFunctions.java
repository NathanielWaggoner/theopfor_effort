package com.mycompany.calculator;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedFunctions{

    public static class toolbarSpinnerEvents implements AdapterView.OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
            Intent newScreen;
            String selectedItem = parent.getItemAtPosition(pos).toString();
            Log.i("CALC", "Spinner item: \"" + selectedItem + "\" selected");

            switch(selectedItem){
                case "Calculator ":
                    newScreen = new Intent(view.getContext(), CalculatorScreen.class);
                    view.getContext().startActivity(newScreen);
                    break;
                case "Graphing ":
                    newScreen = new Intent(view.getContext(), GraphingScreen.class);
                    view.getContext().startActivity(newScreen);
                    break;
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {}
    }

    public static ArrayAdapter<String> setupSpinner(ActionBar toolbar, Resources res){
        String[] screenNames = res.getStringArray(R.array.ScreenNames);
        List<String> spinnerItems = new ArrayList<>(Arrays.asList(screenNames));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(toolbar.getThemedContext(), R.layout.spinner_toolbar_layout, spinnerItems);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_toolbar_layout);

        return arrayAdapter;
    }
}