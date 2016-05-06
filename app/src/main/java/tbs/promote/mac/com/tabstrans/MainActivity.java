package tbs.promote.mac.com.tabstrans;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.WindowDecorActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    MyViewPagerAdapter adapter;
    private FragmentOneRefreshListener fragmentOneRefreshListener;
    private FragmentTwoRefreshListener fragmentTwoRefreshListener;
    private FragmentThreeRefreshListener fragmentThreeRefreshListener;



    private ProgressBar mProgressBar;


    private int[] tabIcons = {
            R.drawable.ic_adjust_white_18dp,
            R.drawable.ic_sd_storage_white_18dp,
            R.drawable.ic_assessment_white_18dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();

    }


    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setIcon(tabIcons[0]);
        mTabLayout.getTabAt(1).setIcon(tabIcons[1]);
        mTabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }


    private void setupViewPager(ViewPager viewPager) {

        adapter = new MyViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new OneFragment(), "Avg Size");
        adapter.addFragment(new TwoFragment(), "Frequents");
        adapter.addFragment(new ThreeFragment(), "top 10");
        viewPager.setAdapter(adapter);
    }

    public static class MyViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public MyViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }

    public FragmentOneRefreshListener getFragmentOneRefreshListener() {
        return fragmentOneRefreshListener;
    }

    public void setFragmentOneRefreshListener(FragmentOneRefreshListener fragmentRefreshListener) {
        this.fragmentOneRefreshListener = fragmentRefreshListener;
    }

    public FragmentTwoRefreshListener getFragmentTwoRefreshListener() {
        return fragmentTwoRefreshListener;
    }

    public void setFragmentTwoRefreshListener(FragmentTwoRefreshListener fragmentTwoRefreshListener) {
        this.fragmentTwoRefreshListener = fragmentTwoRefreshListener;
    }

    public FragmentThreeRefreshListener getFragmentThreeRefreshListener() {
        return fragmentThreeRefreshListener;
    }

    public void setFragmentThreeRefreshListener(FragmentThreeRefreshListener fragmentThreeRefreshListener) {
        this.fragmentThreeRefreshListener = fragmentThreeRefreshListener;
    }

    public void startProcess(View view) {

        new HandlerFiles().execute();


    }

    public void stopProcess(View view) {
        new HandlerFiles().cancel(true);
    }


    public interface FragmentOneRefreshListener {
        void onRefresh(String newValue);
    }

    public interface FragmentTwoRefreshListener {

        void onRefresh(Context context, ArrayList<String> all);

    }

    public interface FragmentThreeRefreshListener {
        void onRefresh(Context context, ArrayList<String> all);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d("SAAV", "MAIN>>>>");
        getSupportFragmentManager().putFragment(outState, "oneFragment", adapter.getItem(0));
        getSupportFragmentManager().putFragment(outState, "twoFragment", adapter.getItem(1));
        getSupportFragmentManager().putFragment(outState, "threeFragment", adapter.getItem(2));

    }

    public class HandlerFiles extends AsyncTask<String, Integer, HashMap> {

        private final String LOG_TAG = HandlerFiles.class.getName() + "++";

        private EditText teSecondsProgressedM;

        @Override
        protected void onPreExecute() {
            Log.d(LOG_TAG, "Pre-Execute");

            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(0);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);
        }




        @Override
        protected void onPostExecute(HashMap result) {

            mProgressBar.setVisibility(View.INVISIBLE);


            if (result != null) {

                HashMap all = (HashMap) result.get("namesSizes");

                ArrayList<String> freqList2 = new ArrayList();

                Iterator it = all.entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    freqList2.add(pair.getKey() + " - " + pair.getValue());
                    it.remove();
                }

                if (getFragmentThreeRefreshListener() != null) {
                    getFragmentThreeRefreshListener().onRefresh(getApplicationContext(), freqList2);
                }


                Long avSize = (Long) result.get("averageSize");


                Log.d(LOG_TAG, avSize + " ++");

                if (getFragmentOneRefreshListener() != null) {
                    getFragmentOneRefreshListener().onRefresh(avSize / 1024 + " KB");
                }


                HashMap freqMap = (HashMap) result.get("frequency");
                ArrayList<String> freqList = new ArrayList();


                //frequency
                Iterator it2 = freqMap.entrySet().iterator();

                while (it2.hasNext()) {
                    Map.Entry pair = (Map.Entry) it2.next();
                    freqList.add(pair.getKey() + " - " + pair.getValue());
                    it2.remove();
                }

                if (getFragmentTwoRefreshListener() != null) {
                    getFragmentTwoRefreshListener().onRefresh(getApplicationContext(), freqList);
                }



                Log.d(LOG_TAG, freqList.size() + " freq size");


            }


        }


        @Override
        protected HashMap doInBackground(String... arg) {

            publishProgress(1);
            HashMap results = new HashMap();
            //if(Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)) {
            if (true) {
                Log.d(LOG_TAG, "YOU HAVE EXTERNAL MEMORY ");

                if (canRead()) {
                    Log.d(LOG_TAG, "IS READABLE YES ");

                    File mainDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

                    File[] mainFiles = mainDir.listFiles();

                    if (null != mainFiles && mainFiles.length > 0) {

                        Log.d(LOG_TAG, "HOW MANY OF FILES " + mainFiles.length);


                        Arrays.sort(mainFiles, new FileSizeComparator());

                        //name and size of the 10 biggest
                        HashMap values = sortByValues(getNameSizeMap(mainFiles), 10);
                        Log.d(LOG_TAG, "HOW MANY OF FILES >" + values.size());
                        publishProgress(30);
                        results.put("namesSizes", values);
                        //average size of the files
                        long average = averageSize(mainFiles);
                        results.put("averageSize", average);
                        publishProgress(60);
                        //5 more frequently extensions
                        HashMap freqExts = frequencyExtensions(getFiles(mainFiles));
                        results.put("frequency", freqExts);
                        publishProgress(90);
                    } else {
                        Log.d(LOG_TAG, "NO FILES ........");
                    }

                } else {

                    Log.d(LOG_TAG, "NOT READABLE ........");
                }


            } else {
                Log.d(LOG_TAG, "NO SDCARD ........");

            }


            return results;

        }


        public long averageSize(File a[]) {

            long tmpLengh = a.length;
            long sum = 0;
            String exts[] = new String[a.length + 1];
            for (int i = 0; i < a.length; i++) {
                sum += a[i].length();
                exts[i] = getFileExtension(a[i]);
            }
            sum = sum / tmpLengh;
            return sum;

        }


     /*   public ArrayList getNameSize(File[] allFiles) {

            HashMap<String, Long> results = new HashMap<>();
            ArrayList listValues = new ArrayList();
            for (int i = 0; i < allFiles.length; i++) {

                if (allFiles[i].isFile()) {
                    listValues.add(allFiles[i].getName() + " - " + allFiles[i].length());
                    results.put(allFiles[i].getName(), allFiles[i].length());
                } else if (allFiles[i].listFiles() != null) {

                    listValues.addAll(getNameSize(allFiles[i].listFiles()));

                    results.put(allFiles[i].getName(), allFiles[i].length());
                }
            }
            return listValues;
        }
*/
        public HashMap<String, Long> getNameSizeMap(File[] allFiles) {

            HashMap<String, Long> results = new HashMap<>();
            ArrayList listValues = new ArrayList();
            for (int i = 0; i < allFiles.length; i++) {

                if (allFiles[i].isFile()) {
                    results.put(allFiles[i].getName(), allFiles[i].length());
                } else if (allFiles[i].listFiles() != null) {
                    results.putAll(getNameSizeMap(allFiles[i].listFiles()));
                }
            }
            return results;
        }


        public ArrayList<String> getFiles(File[] allFiles) {

            ArrayList<String> fls = new ArrayList();
            for (int i = 0; i < allFiles.length; i++) {
                {
                    if (allFiles[i].isFile()) {
                        fls.add(allFiles[i].getName());
                    } else if (allFiles[i].listFiles() != null) {

                        fls.addAll(getFiles(allFiles[i].listFiles()));
                    }
                }
            }

            return fls;
        }


        public HashMap<String, Integer> frequencyExtensions(ArrayList<String> fls) {

            final HashMap<String, Integer> counter = new HashMap<String, Integer>();
            String tmp;
            for (String a : fls) {
                tmp = getFileExtension(a);
                counter.put(tmp, 1 + (counter.containsKey(tmp) ? counter.get(tmp) : 0));
            }

            return sortByValues(counter, 5);
        }


        private HashMap sortByValues(HashMap map, int amount) {
            List list = new LinkedList(map.entrySet());

            Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((Comparable) ((Map.Entry) (o2)).getValue())
                            .compareTo(((Map.Entry) (o1)).getValue());
                }
            });

            int tmp = 0;
            HashMap sortedHashMap = new LinkedHashMap();
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                tmp++;
                Map.Entry entry = (Map.Entry) it.next();
                sortedHashMap.put(entry.getKey(), entry.getValue());
                if (tmp == amount) break;

            }
            return sortedHashMap;
        }


/*
        public HashMap<String, Integer> frequencyExtensionsErase(File a[]) {

            final HashMap<String, Integer> counter = new HashMap<String, Integer>();

            ArrayList<String> freqs = new ArrayList();

            String exts[] = new String[a.length];
            HashMap results = new HashMap();


            for (int i = 0; i < a.length; i++) {


                if (a[i].isFile()) {
                    exts[i] = getFileExtension(a[i]);

                    counter.put(exts[i], 1 + (counter.containsKey(exts[i]) ? counter.get(exts[i]) : 0));

                    if (i == 4) break;
                }

            }


            return counter;
        }
*/


        private String getFileExtension(String fileName) {

            if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
                return fileName.substring(fileName.lastIndexOf(".") + 1);
            else return "";
        }

        private String getFileExtension(File file) {
            String fileName = file.getName();
            if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
                return fileName.substring(fileName.lastIndexOf(".") + 1);
            else return "";
        }

        public boolean canRead() {

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
                return true;
            }
            return false;
        }


        public class FileSizeComparator implements Comparator<File> {

            @Override
            public int compare(File a, File b) {
                int aa = Integer.parseInt(String.valueOf(a.length() / 1024));
                int bb = Integer.parseInt(String.valueOf(b.length() / 1024));
                return aa - bb;
            }
        }


    }


}
