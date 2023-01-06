package com.company.todolistproject;

import static com.company.todolistproject.AppConstants.POSITION;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * Created by Rafal Zaborowski on 06.01.2023.
 */
public class DeleteConfirmDialogFragment extends DialogFragment {
    public static DeleteConfirmDialogFragment newInstance(int position) {
        DeleteConfirmDialogFragment frag = new DeleteConfirmDialogFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        frag.setArguments(args);
        return frag;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int position = getArguments()==null? 0 : getArguments().getInt(POSITION);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.list_alert_title)
                .setMessage(R.string.list_alert_message)
                .setPositiveButton(R.string.button_text_positive, (dialog, whichButton) -> ((MainActivity)getActivity()).onDeleteConfirmDialogClick(position))
                .setNegativeButton(R.string.button_text_negative, (dialog, whichButton) -> dismiss())
                .create();
    }
}
