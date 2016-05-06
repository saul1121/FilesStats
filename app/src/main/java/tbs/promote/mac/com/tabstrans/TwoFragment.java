package tbs.promote.mac.com.tabstrans;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;



public class TwoFragment extends Fragment {


    private ListView mAllFiles ;
    ArrayList<String> mFiles;
    public TwoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_two, container, false);
        mAllFiles = (ListView) v.findViewById(R.id.frequentId);

        ((MainActivity)getActivity()).setFragmentTwoRefreshListener(new MainActivity.FragmentTwoRefreshListener() {



            @Override
            public void onRefresh(Context cont, ArrayList<String> all) {

                mFiles= all;
                ArrayAdapter adapterNamesSizes = new ArrayAdapter<String>(cont,
                        R.layout.text_layout,
                        mFiles);
                 //Names and sizes
                mAllFiles.setAdapter(adapterNamesSizes);

            }
        });


        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState !=null){
            mFiles=   savedInstanceState.getStringArrayList("frequencyFiles");

            ArrayAdapter adapterNamesSizes = new ArrayAdapter<String>(getActivity(),
                    R.layout.text_layout,
                    mFiles);
            mAllFiles.setAdapter(adapterNamesSizes);
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {


        outState.putStringArrayList("frequencyFiles", mFiles);
        Log.d("<<<<<<<<<FFRAG2", mFiles.size()+"");

        super.onSaveInstanceState(outState);

    }





}
