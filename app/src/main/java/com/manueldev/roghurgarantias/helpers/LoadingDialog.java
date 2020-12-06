package com.manueldev.roghurgarantias.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.manueldev.roghurgarantias.R;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;
    private String textDialog;

    private TextView tvTitleDialog;

    public LoadingDialog(Activity activity, String textDialog){
        this.activity = activity;
        this.textDialog = textDialog;
    }

    public Activity getActivity() {
        return activity;
    }

    public LoadingDialog setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public String getTextDialog() {
        return textDialog;
    }

    public LoadingDialog setTextDialog(String textDialog) {
        this.textDialog = textDialog;
        return this;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();

        View v = inflater.inflate(R.layout.loading_progess_dialog,null);
        tvTitleDialog =  v.findViewById(R.id.tv_title_loading_progress_dialog);
        tvTitleDialog.setText(textDialog);

        builderAlertDialog.setView(v);
        builderAlertDialog.setCancelable(false);

        alertDialog = builderAlertDialog.create();
        alertDialog.show();

    }

    public void dismiss(){
        alertDialog.dismiss();
    }

}
