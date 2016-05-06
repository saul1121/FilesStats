package tbs.promote.mac.com.tabstrans;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class OneFragment extends Fragment {

    TextView mAverageFile;


    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (savedInstanceState != null) {
            String b = (String) savedInstanceState.get("average");
            mAverageFile.setText(b);
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("average", mAverageFile.getText().toString());
           super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_one, container, false);

        mAverageFile = (TextView) v.findViewById(R.id.fileSizeId);


        ((MainActivity) getActivity()).setFragmentOneRefreshListener(new MainActivity.FragmentOneRefreshListener() {
            @Override
            public void onRefresh(String newValue) {

                mAverageFile.setText(newValue);

            }
        });

        return v;
    }



}
