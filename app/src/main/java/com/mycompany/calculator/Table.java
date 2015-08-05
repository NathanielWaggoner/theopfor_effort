package com.mycompany.calculator;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import android.widget.LinearLayout;
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
    LinearLayout loadingView;

    boolean alreadyCentered = false;
    boolean currentlyLoading = true;

    double start;
    double step;

    Table(ListView listView, int capacity, Equation e, Activity a){
        table = listView;
        size = capacity;
        equation = e;
        adapter = new ListViewAdapter();
        activity = a;
        list = new ArrayList<>();
        start = 0;
        step = 1;

        loadingView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.table_loading_bar, null);
        loadingView.setGravity(Gravity.CENTER_HORIZONTAL);

        TableEvents events = new TableEvents();
        table.getViewTreeObserver().addOnGlobalLayoutListener(events.setTableMiddleSelection);
        table.setOnScrollListener(events.autoGrowth);

        new InitialTableLoader().execute();
    }

    public void reload(){
        table.setAdapter(null);
        list.clear();
        alreadyCentered = false;
        currentlyLoading = true;
        ((View)table.getParent()).findViewById(R.id.TableLoadProgressBar).setVisibility(View.VISIBLE);
        new InitialTableLoader().execute();
    }

    public void changeEquation(Equation e){
        equation = e;
        reload();
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
        list.remove(size + 1);
    }

    public void changeProperties(double startValue, double stepValue) {
        if (start != startValue || step != stepValue) {
            start = startValue;
            step = stepValue;
            reload();
        }
    }

    private class ListViewAdapter extends BaseAdapter {
        int rowHeight;

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
                rowHeight = v.getHeight();

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

    private class InitialTableLoader extends AsyncTask<String, Void, ListViewAdapter> {

        @Override
        protected ListViewAdapter doInBackground(String... params) {
            int top = size / 2 + 1;
            int bottom = 0 - size / 2;

            // Generate N items into an ArrayList and Adapter
            for (int i = bottom; i < top; i++) {
                append(start + i * step);
            }

            return adapter;
        }

        protected void onPostExecute(ListViewAdapter adapt){
            table.addFooterView(loadingView);
            table.addHeaderView(loadingView);
            table.setAdapter(adapt);
            table.removeFooterView(loadingView);
            table.removeHeaderView(loadingView);
        }
    }

    private class LoadUp extends AsyncTask<String, Void, ListViewAdapter> {

        @Override
        protected ListViewAdapter doInBackground(String... params) {

            double lastX = list.get(0).get(X_KEY);

            // Add size/2 items.
            for (int i = ((int)lastX) - 1; i >= lastX - size / 2; i--){
                prepend(start + i*step);
                removeEnd();
            }

            return adapter;
        }

        protected void onPostExecute(ListViewAdapter adapt) {
            table.removeHeaderView(loadingView);

            // Data set is changed, but keep position
            int index = table.getFirstVisiblePosition() + size/2;
            View v = table.getChildAt(table.getHeaderViewsCount());
            int top = v.getTop();
            adapt.notifyDataSetChanged();
            table.setSelectionFromTop(index, top);


            currentlyLoading = false;
        }
    }

    private class LoadDown extends AsyncTask<String, Void, ListViewAdapter> {

        @Override
        protected ListViewAdapter doInBackground(String... params) {

            double lastX = list.get(list.size() - 1).get(X_KEY);

            // Add size/2 items.
            for (int i = ((int)lastX) + 1; i <= lastX + size / 2; i++){
                append(start + i*step);
                removeStart();
            }

            return adapter;
        }

        protected void onPostExecute(ListViewAdapter adapt) {
            table.removeFooterView(loadingView);

            // Data set is changed, but keep position
            int index = table.getFirstVisiblePosition() - size/2;
            View v = table.getChildAt(table.getHeaderViewsCount());
            int top = v.getTop();
            adapt.notifyDataSetChanged();
            table.setSelectionFromTop(index, top);

            currentlyLoading = false;
        }
    }

    private class TableEvents {
        ViewTreeObserver.OnGlobalLayoutListener setTableMiddleSelection = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!alreadyCentered) {
                    int height = table.getMeasuredHeight();
                    int rowHeight = 0;

                    // Get row height, but it might not be created yet
                    if (table.getChildAt(0) != null)
                        rowHeight = table.getChildAt(0).getHeight();

                    // Center 0
                    table.setSelectionFromTop(size / 2, height / 2 - rowHeight / 2);

                    // When view is FINALLY inflated, we don't need to do this anymore
                    if (table.getCount() >= size) {
                        alreadyCentered = true;
                        currentlyLoading = false;

                        // Hide progress bar
                        ((View)table.getParent()).findViewById(R.id.TableLoadProgressBar).setVisibility(View.GONE);
                    }
                }
            }
        };

        AbsListView.OnScrollListener autoGrowth = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int state) {}

            @Override
            public void onScroll(AbsListView view, int firstVisible, int visibleItemCount, int totalItemCount) {
                if (currentlyLoading)
                    return;

                /*double topThreshold = .25*size - 1;     // 1/4 of the way up
                double bottomThreshold = .75*size - 1;  // 3/4 of the way down*/

                if (firstVisible <= 0){
                    currentlyLoading = true;
                    table.addHeaderView(loadingView);
                    table.setSelection(firstVisible + 2);
                    new LoadUp().execute();
                }
                else if(firstVisible + visibleItemCount >= size){
                    currentlyLoading = true;
                    table.addFooterView(loadingView);
                    new LoadDown().execute();
                }
            }
        };
    }
}