package com.github.sviatoslavslysh.vacationvibes.functionality.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class TutorialFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder tutorial1 = new AlertDialog.Builder(getActivity());
        tutorial1.setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit" +
                ", sed do eiusmod tempor incididunt ut labore et dolore magna" +
                " aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut" +
                " aliquip ex ea commodo consequat. Duis aute irure dolor" +
                " in reprehenderit in voluptate velit esse cillum dolore eu " +
                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in" +
                " culpa qui officia deserunt mollit anim id est laborum.")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("yeah");
                    }
                });

        return tutorial1.create();
    }

}
