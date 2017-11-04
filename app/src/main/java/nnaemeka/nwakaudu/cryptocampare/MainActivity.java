package nnaemeka.nwakaudu.cryptocampare;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import nnaemeka.nwakaudu.cryptocampare.fragment.BTCFragment;
import nnaemeka.nwakaudu.cryptocampare.fragment.ETHFragment;



public class MainActivity extends AppCompatActivity {


    private final String[] PAGE_TITLES = new String[] {
            "BTC",
            "ETH",};
    private final Fragment[] PAGES = new Fragment[] {
            new BTCFragment(),
            new ETHFragment(),};
    private ViewPager mViewPager;

    //method the handle instruction to do once activity is created
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mViewPager = (ViewPager) findViewById(R.id.viewpager);
            mViewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(mViewPager);
            }

    // method that handles the fragments for bitcoin and ethereum
    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return PAGES[position];
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }

    }

    //action to perform once activity begins
    @Override
        protected void onStart() {
            super.onStart();
        }
    //action to perform once activity breaks and begins
    @Override
    protected void onResume(){
        super.onResume();
    }
}


