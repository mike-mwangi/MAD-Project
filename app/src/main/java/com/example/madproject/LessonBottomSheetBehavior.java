package com.example.madproject;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class LessonBottomSheetBehavior extends BottomSheetBehavior {
    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull MotionEvent event) {
        if (event.getX() < child.getTranslationX()) {
            return false;
        }
        return super.onTouchEvent(parent, child, event);
    }
}
