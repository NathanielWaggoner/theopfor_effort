package com.mycompany.calculator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BasicKeypad.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BasicKeypad#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicKeypad extends Fragment {
    private boolean graphing;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param graphingMode Is Calculator in graphing mode?
     * @return A new instance of fragment BasicKeypad.
     */
    public static BasicKeypad newInstance(Boolean graphingMode) {
        BasicKeypad fragment = new BasicKeypad();
        Bundle args = new Bundle();
        args.putBoolean("GRAPHING", graphingMode);
        fragment.setArguments(args);
        return fragment;
    }

    public BasicKeypad() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            graphing = getArguments().getBoolean("GRAPHING");
            Log.i("CALC", Boolean.toString(graphing));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_basic_keypad, container, false);
        if (graphing) {
            Button button = (Button) v.findViewById(R.id.Equals);
            button.setText("↲");
            button.setTextSize(32);
            Log.i("CALC", "Equal button set to enter");
        }
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
