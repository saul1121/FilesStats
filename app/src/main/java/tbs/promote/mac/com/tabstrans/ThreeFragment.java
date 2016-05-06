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


/**
 * A simple {@link Fragment} subclass.
 */
public class ThreeFragment extends Fragment {

    ListView mTopListView ;
    ArrayList mTopFiles;

    public ThreeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_three, container, false);

        mTopListView = (ListView) v.findViewById(R.id.listFiles);

        ((MainActivity)getActivity()).setFragmentThreeRefreshListener(new MainActivity.FragmentThreeRefreshListener() {

            @Override
            public void onRefresh(Context cont, ArrayList<String> all) {

                mTopFiles= all;
                ArrayAdapter adapter = new ArrayAdapter<String>(cont,
                        R.layout.text_layout,
                        mTopFiles);

                //Names and sizes
                mTopListView.setAdapter(adapter);

            }
        });

        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState !=null){
            mTopFiles=   savedInstanceState.getStringArrayList("topFiles");

            ArrayAdapter adapterNamesSizes = new ArrayAdapter<String>(getActivity(),
                    R.layout.text_layout,
                    mTopFiles);
            mTopListView.setAdapter(adapterNamesSizes);
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putStringArrayList("topFiles", mTopFiles);
        super.onSaveInstanceState(outState);

    }



}
