package com.example.madproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.madproject.Adapters.PostAdapter;
import com.google.android.material.tabs.TabLayout;

public class PostingActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    PostAdapter adapter;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        tabLayout=findViewById(R.id.tabLayout);
        viewPager2=findViewById(R.id.viewpager2);
        backButton = findViewById(R.id.backArrow);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogClass dialogClass = new DialogClass();
                dialogClass.show(getSupportFragmentManager(),"example dialog");
            }
        });


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