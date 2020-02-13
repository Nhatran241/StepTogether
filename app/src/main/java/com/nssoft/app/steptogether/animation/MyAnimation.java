package com.nssoft.app.steptogether.animation;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.nssoft.app.steptogether.R;


public class MyAnimation {
    Dialog dialog;
    public static String AnimationLoading = "AnimationLoading";

    private static MyAnimation INSTANCE = null;
    private MyAnimation( ) {
    }
    public static MyAnimation getInstance( ) {
        if (INSTANCE == null) {
            INSTANCE = new MyAnimation();
        }
        return(INSTANCE);
    }

    public void showAnimation(Context context,String animation){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        switch (animation){
            case "AnimationLoading" : {
                dialog.setContentView(R.layout.animation_loading);
                break;
            }
        }
            dialog.show();
    }
    public void hideAnimation(){
        dialog.dismiss();
    }
}
