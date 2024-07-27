package com.example.haneum_d;


import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class ResultBottomSheet {
    public ResultBottomSheet(final BottomSheetBehavior behavior, final Activity activity){
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback(){
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState){
                switch (newState){
                    case BottomSheetBehavior.STATE_HIDDEN:
                        //Toast.makeText(activity, "STATE_HIDDEN", Toast.LENGTH_SHORT).show();
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        //Toast.makeText(activity, "STATE_DRAGGING", Toast.LENGTH_SHORT).show();
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        //Toast.makeText(activity, "STATE_EXPANDED", Toast.LENGTH_SHORT).show();
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        //Toast.makeText(activity, "STATE_HALF_EXPANDED", Toast.LENGTH_SHORT).show();
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset){

            }
        });
    }
}
