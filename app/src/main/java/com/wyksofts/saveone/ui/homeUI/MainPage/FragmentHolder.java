package com.wyksofts.saveone.ui.homeUI.MainPage;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wyksofts.saveone.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentHolder extends Fragment {

    private String[] Headers = new String[] { "Home", "Map", "Reviews" };
    private int[] icons =  new int[]{
            R.drawable.baseline_home_24,
            R.drawable.baseline_map_24,
            R.drawable.baseline_chat_24,};

    private TabLayout tabLayout;

    private ViewPager viewPager;

    public FragmentHolder() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.frag_home_main, container, false);

        viewPager = view.findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout = view.findViewById(R.id.tabview);
        tabLayout.setupWithViewPager(this.viewPager);
        showHeaders();
        return view;
    }

    public void onResume() {
        super.onResume();
    }

    public void setViewPager(ViewPager paramViewPager) {
        viewPager = paramViewPager;
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<Fragment>();

        public ViewPagerAdapter(FragmentManager param1FragmentManager) {
            super(param1FragmentManager);
        }

        public void addFrag(Fragment param1Fragment) {
            mFragmentList.add(param1Fragment);
        }

        public int getCount() {
            return mFragmentList.size();
        }

        public Fragment getItem(int param1Int) {
            return mFragmentList.get(param1Int);
        }

        public CharSequence getPageTitle(int param1Int) {
            return null;
        }
    }

    private void setupViewPager(ViewPager pager) {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPagerAdapter.addFrag(new HomePage());
        viewPagerAdapter.addFrag(new MapView());
        viewPagerAdapter.addFrag(new ReviewsView());
        pager.setAdapter(viewPagerAdapter);
    }

    private void showHeaders() {

        tabLayout.getTabAt(0).setIcon(icons[0]);
        tabLayout.getTabAt(1).setIcon(icons[1]);
        tabLayout.getTabAt(2).setIcon(icons[2]);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        tabLayout.setTabGravity(0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab param1Tab) {
                viewPager.refreshDrawableState();
            }
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition())
                        .getIcon()
                        .setColorFilter(getResources()
                        .getColor(R.color.colorAccent),
                                PorterDuff.Mode.SRC_IN);
            }
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.refreshDrawableState();
                tabLayout.getTabAt(tab.getPosition())
                        .getIcon()
                        .setColorFilter(getResources()
                                        .getColor(R.color.blue),
                                PorterDuff.Mode.SRC_IN);
            }
        });

        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.select();
    }


    public void CloseFragment(int paramInt) {}

    public void OpenFragment(int paramInt) {}

    public ViewPager getViewPager() {
        return viewPager;
    }

}