package com.umn.imergency.ui.tombol_darurat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.gauravbhola.ripplepulsebackground.RipplePulseLayout;
import com.umn.imergency.R;

import org.w3c.dom.Text;

public class TombolDaruratFragment extends Fragment {
    private Button button_red;
    private RadioGroup radiogroup_instances1, radiogroup_instances2;
    private TextView textview_selected_instance;
    private RipplePulseLayout container_ripple_pulse;

    private boolean isChecking;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tombol_darurat, container, false);

        textview_selected_instance = view.findViewById(R.id.textview_selected_instance);
        button_red = view.findViewById(R.id.button_red);
        radiogroup_instances1 = view.findViewById(R.id.radiogroup_instances1);
        radiogroup_instances2 = view.findViewById(R.id.radiogroup_instances2);
        container_ripple_pulse = view.findViewById(R.id.container_ripple_pulse);

        container_ripple_pulse.startRippleAnimation();

        // On Checked listeners
        radiogroup_instances1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i != -1 && isChecking) {
                    isChecking = false;
                    radiogroup_instances2.clearCheck();

                    switch(i) {
                        case R.id.radio_hospital: {
                            setSelectedInstanceText("Rumah Sakit");
                            break;
                        }
                        case R.id.radio_police: {
                            setSelectedInstanceText("Polisi");
                            break;
                        }
                        case R.id.radio_damkar: {
                            setSelectedInstanceText("Pemadam Kebakaran");
                            break;
                        }
                        case R.id.radio_pharmacy: {
                            setSelectedInstanceText("Apotek");
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
                isChecking = true;
            }
        });
        radiogroup_instances2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i != -1 && isChecking) {
                    isChecking = false;
                    radiogroup_instances1.clearCheck();

                    switch (i) {
                        case R.id.radio_vet: {
                            setSelectedInstanceText("Dokter Hewan");
                            break;
                        }
                        case R.id.radio_tow: {
                            setSelectedInstanceText("Mobil Derek");
                            break;
                        }
                        case R.id.radio_spbu: {
                            setSelectedInstanceText("SPBU");
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
                isChecking = true;
            }
        });
        return view;
    }

    private void setSelectedInstanceText(String selectedInstance) {
        textview_selected_instance.setText(selectedInstance);
    }
}
