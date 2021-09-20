package com.example.madproject.Adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.madproject.AudioFragment;
import com.example.madproject.BlogFragment;

public class PostAdapter extends FragmentStateAdapter {


    public PostAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1 :
                return new AudioFragment();
            default:
                return new BlogFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
