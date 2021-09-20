package com.example.madproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.madproject.Adapters.PostAdapter;
import com.google.android.material.tabs.TabLayout;

public class PostingActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        tabLayout=findViewById(R.id.tabLayout);
        viewPager2=findViewById(R.id.viewpager2);



        FragmentManager fm = getSupportFragmentManager();
        adapter = new PostAdapter(fm,getLifecycle());

        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
            tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

}