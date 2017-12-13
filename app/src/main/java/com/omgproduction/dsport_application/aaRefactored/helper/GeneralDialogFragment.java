package com.omgproduction.dsport_application.aaRefactored.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.fragments.FriendsFragment;
import com.omgproduction.dsport_application.aaRefactored.interfaces.onDialogFragmentClickListener;

public class GeneralDialogFragment extends AlertDialog {

    private FragmentActivity fragmentActivity;
    private AlertDialog.Builder builder;
    private onDialogFragmentClickListener onDialogFragmentClickListener;




    public GeneralDialogFragment(@NonNull Context context, @NonNull FragmentActivity fragmentActivity, onDialogFragmentClickListener onDialogFragmentClickListener) {
        super(context);
        this.fragmentActivity = fragmentActivity;
        this.onDialogFragmentClickListener = onDialogFragmentClickListener;
    }



    public AlertDialog createDialog() {

        builder = new AlertDialog.Builder(fragmentActivity);

        builder.setMessage(R.string.alert_dialog_message)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_dialog_try_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDialogFragmentClickListener.onOkClicked();
                    }
                })
                .setNegativeButton(R.string.alter_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDialogFragmentClickListener.onCancelClicked();
                    }
                });


        return builder.create();
    }

    public void show(AlertDialog alertDialog){
        alertDialog.show();
    }

}
