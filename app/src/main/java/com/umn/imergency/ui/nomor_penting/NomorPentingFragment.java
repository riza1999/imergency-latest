package com.umn.imergency.ui.nomor_penting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.gauravbhola.ripplepulsebackground.RipplePulseLayout;
import com.umn.imergency.R;

public class NomorPentingFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nomor_penting, container, false);

        return view;
    }
}
