package com.fitastyclient.dialogs;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.fitastyclient.Utils;
import java.util.Objects;

public abstract class MyDialogFragment extends DialogFragment {

    protected View view;

    protected void setViewText(int viewId, String text) {
        ((TextView) this.view.findViewById(viewId)).setText(text);
    }

    protected void clearViewText(int viewId) {
        setViewText(viewId, Utils.EMPTY);
    }

    protected void changeViewVisibility(int viewId, int visibility) {
        this.view.findViewById(viewId).setVisibility(visibility);
    }

    protected void enableCheckBox(int viewId) {
        ((CheckBox) this.view.findViewById(viewId)).toggle();
    }

    protected int getColorById(int colorId) {
        return getResources().getColor(colorId);
    }

    protected AlertDialog.Builder getBuilder() {
        return new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
    }

    protected void setInflaterView(int layoutId) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        this.view = inflater.inflate(layoutId, null);
    }

    protected void setCloseButton(AlertDialog.Builder builder) {
        builder.setView(this.view).setNegativeButton(Utils.CLOSE, null);
    }

    protected void setViewToGramsValue(int viewId, double value) {
        setViewText(viewId, Utils.cleanDoubleToString(value) + Utils.GRAM);
    }
}
