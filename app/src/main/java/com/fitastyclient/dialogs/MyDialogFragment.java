package com.fitastyclient.dialogs;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.fitastyclient.Utils;
import java.util.Objects;

public abstract class MyDialogFragment extends DialogFragment {

    protected abstract void populateFieldsFromView(View view);

    protected void setViewText(View view, int viewId, String text) {
        ((TextView) view.findViewById(viewId)).setText(text);
    }

    protected void enableCheckBox(View view, int viewId) {
        ((CheckBox) view.findViewById(viewId)).toggle();
    }

    protected int getColorById(int colorId) {
        return getResources().getColor(colorId);
    }

    protected Dialog getDialog(int layoutId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(layoutId, null);
        populateFieldsFromView(view);
        builder.setView(view).setPositiveButton(Utils.CLOSE, null);
        return builder.create();
    }
}
