package com.mycompany.calculator;

import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedFunctions{
    public static ArrayAdapter<String> setupToolbar(ActionBar toolbar, Resources res){
        String[] screenNames = res.getStringArray(R.array.ScreenNames);
        List<String> spinnerItems = new ArrayList<>(Arrays.asList(screenNames));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(toolbar.getThemedContext(), R.layout.spinner_layout, spinnerItems);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_layout);

        return arrayAdapter;
    }
}