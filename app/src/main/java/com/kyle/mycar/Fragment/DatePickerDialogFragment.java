package com.kyle.mycar.Fragment;


import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kyle.mycar.R;

/**
 *
 */
public class DatePickerDialogFragment extends AppCompatDialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker_dialog, container, false);
    }

}
