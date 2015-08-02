package com.mycompany.calculator;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

class Table{

    final String X_KEY = "X";
    final String Y1_KEY = "Y1";

    int size;
    Equation equation;
    ListView table;
    ListViewAdapter adapter;
    ArrayList<HashMap<String, Double>> list;
    Activity activity;

    Table(ListView listView, int capacity, Equation e, Activity a){
        table = listView;
        size = capacity;
        equation = e;
        adapter = new ListViewAdapter();
        activity = a;
        list = new ArrayList<>();

        new InitialTableLoader().execute();
    }

    public void append(Double xValue) {
        HashMap<String, Double> item = new HashMap<>();
        item.put(X_KEY, xValue);
        item.put(Y1_KEY, equation.getY(xValue));
        list.add(item);
    }

    public void prepend(Double xValue) {
        HashMap<String, Double> item = new HashMap<>();
        item.put(X_KEY, xValue);
        item.put(Y1_KEY, equation.getY(xValue));
        list.add(0, item);
    }

    public void removeStart() {
        list.remove(0);
    }

    public void removeEnd() {
        list.remove(size - 1);
    }

    private class ListViewAdapter extends BaseAdapter {

        private class ViewHolder{
            TextView xValue;
            TextView y1;
        }

        public ListViewAdapter(){
            super();
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
            HashMap<String, Double> map = list.get(pos);

            if (v == null) {
                ViewHolder holder = new ViewHolder();
                LayoutInflater inflater = activity.getLayoutInflater();
                v = inflater.inflate(R.layout.table_graph_row, parent, false);

                holder.xValue = (TextView) v.findViewById(R.id.XValue);
                holder.y1 = (TextView) v.findViewById(R.id.Y1Value);
                v.setTag(holder);
            }

            ViewHolder holder = (ViewHolder) v.getTag();
            holder.xValue.setText(Double.toString(map.get(X_KEY)));
            holder.y1.setText(Double.toString(map.get(Y1_KEY)));

            return v;
        }
    }

    private class InitialTableLoader extends AsyncTask<String, Void, ListViewAdapter>{

        @Override
        protected ListViewAdapter doInBackground(String... params) {
            // Generate N items into an ArrayList and Adapter
            for (Double i = 0.0; i < size; i++){
                append(i);
            }

            return adapter;
        }

        protected void onPostExecute(ListViewAdapter adapter){
            table.setAdapter(adapter);
        }
    }
}