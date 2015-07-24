package com.mycompany.calculator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


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
    private static final String ARG_PARAM2 = "param2";

    private Equation e;
    private int capacity;
    private ListView listView;
    private ListViewAdapter adapter;
    private ArrayList<HashMap<String, Double>> list;
    private final String X_KEY = "X";
    private final String Y1_KEY = "Y1";

    private OnFragmentInteractionListener mListener;

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
        capacity = 10;
        listView = (ListView) v.findViewById(R.id.TableListView);
        list = new ArrayList<>();
        adapter = new ListViewAdapter(getActivity(), list);

        // Generate N items into an ArrayList and Adapter
        for (Double i = 0.0; i < capacity; i++){
            HashMap<String, Double> item = new HashMap<>();
            item.put(X_KEY, i);
            item.put(Y1_KEY, e.getY(i));
            list.add(item);
        }

        listView.setAdapter(adapter);

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
        public void onFragmentInteraction(Uri uri);
    }

    private class ListViewAdapter extends BaseAdapter{
        public ArrayList<HashMap<String, Double>> list;
        Activity activity;

        private class ViewHolder{
            TextView xValue;
            TextView y1;
        }

        public ListViewAdapter(Activity activity, ArrayList<HashMap<String, Double>> items){
            super();
            this.activity = activity;
            this.list = items;
        }

        @Override
        public int getCount(){
            return list.size();
        }

        @Override
        public Object getItem(int pos){
            return list.get(pos);
        }

        @Override
        public long getItemId(int pos){
            return 0;
        }

        @Override
        public View getView(int pos, View v, ViewGroup parent){
            ViewHolder holder = new ViewHolder();
            LayoutInflater inflater = getActivity().getLayoutInflater();

            HashMap<String, Double> map = list.get(pos);

            if (v == null) {
                v = inflater.inflate(R.layout.table_graph_row, parent, false);
            }

            holder.xValue = (TextView) v.findViewById(R.id.XValue);
            holder.y1 = (TextView) v.findViewById(R.id.Y1Value);

            holder.xValue.setText(Double.toString(map.get(X_KEY)));
            holder.y1.setText(Double.toString(map.get(Y1_KEY)));

            return v;
        }
    }

}
