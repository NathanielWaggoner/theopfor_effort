package com.mycompany.calculator;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GraphTable.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GraphTable#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphTable extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private Equation equation;
    private final String X_KEY = "X";
    private final String Y1_KEY = "Y1";
    final int capacity = 200;
    private OnFragmentInteractionListener mListener;
    Table table;

    View.OnClickListener enterKeyListener;
    TextView.OnEditorActionListener closeOnDone;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param e Equation to display
     * @return A new instance of fragment GraphTable.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphTable newInstance(String e) {
        GraphTable fragment = new GraphTable();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, e);
        fragment.setArguments(args);
        return fragment;
    }

    public GraphTable() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            equation = new Equation(getArguments().getString(ARG_PARAM1));
        }
        else{
            equation = new Equation(getActivity().getSharedPreferences("Equations", 0).getString("Y1", "x"));
        }

        enterKeyListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout container = (LinearLayout) v.getParent();
                double start = Double.parseDouble(((EditText)container.findViewById(R.id.StartValue)).getText().toString());
                double step = Double.parseDouble(((EditText) container.findViewById(R.id.StepValue)).getText().toString());
                table.changeProperties(start, step);
                Log.i("Table", "Changing start: " + start + " and step: " + step);

                // Close keyboard and clear focus
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                getView().findViewById(R.id.StartValue).clearFocus();
                getView().findViewById(R.id.StepValue).clearFocus();
            }
        };

        closeOnDone = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    v.clearFocus();

                    // Close keyboard
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                }
                return false;
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_graph_table, container, false);
        table = new Table((ListView) v.findViewById(R.id.TableListView), capacity, equation, getActivity());
        v.findViewById(R.id.StartStepEnter).setOnClickListener(enterKeyListener);
        ((EditText)v.findViewById(R.id.StepValue)).setOnEditorActionListener(closeOnDone);
        ((EditText)v.findViewById(R.id.StartValue)).setOnEditorActionListener(closeOnDone);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void changeEquation(Equation e){
        if (!e.equation.equals(equation.equation)) {
            equation = e;
            table.changeEquation(e);
        }
    }
}
