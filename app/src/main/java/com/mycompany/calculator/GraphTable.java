package com.mycompany.calculator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


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

    private Equation e;
    private final String X_KEY = "X";
    private final String Y1_KEY = "Y1";
    final int capacity = 10;
    private OnFragmentInteractionListener mListener;
    Table table;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param equation Equation to display
     * @return A new instance of fragment GraphTable.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphTable newInstance(String equation) {
        GraphTable fragment = new GraphTable();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, equation);
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
            e = new Equation(getArguments().getString(ARG_PARAM1));
        }
        else{
            e = new Equation(getActivity().getSharedPreferences("Equations", 0).getString("Y1", "x"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_graph_table, container, false);
        table = new Table((ListView) v.findViewById(R.id.TableListView), capacity, e, getActivity());
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
}
