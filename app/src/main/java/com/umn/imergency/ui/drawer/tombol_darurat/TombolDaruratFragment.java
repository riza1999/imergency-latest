package com.umn.imergency.ui.drawer.tombol_darurat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.umn.imergency.helpers.Location;
import com.umn.imergency.ui.PermissionCallActivity;

import java.util.Map;

public class TombolDaruratFragment extends Fragment {
    private Button button_red;
    private RadioGroup radiogroup_instances1, radiogroup_instances2;
    private TextView textview_selected_instance;
    private RipplePulseLayout container_ripple_pulse;

    private boolean isChecking = true;
    private String selected_instance = "hospital";

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
                            selected_instance = "hospital";
                            break;
                        }
                        case R.id.radio_police: {
                            setSelectedInstanceText("Polisi");
                            selected_instance = "police_station";
                            break;
                        }
                        case R.id.radio_damkar: {
                            setSelectedInstanceText("Pemadam Kebakaran");
                            selected_instance = "fire_station";
                            break;
                        }
                        case R.id.radio_pharmacy: {
                            setSelectedInstanceText("Apotek");
                            selected_instance = "pharmacy";
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
                            selected_instance = "veterinary_care";
                            break;
                        }
                        case R.id.radio_tow: {
                            setSelectedInstanceText("Mobil Derek");
                            selected_instance = "tow_service";
                            break;
                        }
                        case R.id.radio_spbu: {
                            setSelectedInstanceText("SPBU");
                            selected_instance = "gas_station";
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
        button_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Location(view.getContext()) {
                    @Override
                    public void onLocationReceived(Map<String, String> location) {
                        String latitude = location.get("latitude");
                        String longitude = location.get("longitude");
                        String location_query = getLocationQuery();

                        Intent intent = new Intent(getContext(), SearchResultActivity.class);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);
                        intent.putExtra("location_query", location_query);
                        intent.putExtra("type", selected_instance);
                        startActivity(intent);
                    }
                };
            }
        });

        return view;
    }

    private void setSelectedInstanceText(String selectedInstance) {
        textview_selected_instance.setText(selectedInstance);
    }

    private String getLocationQuery() {
        switch (selected_instance) {
            case "hospital": {
                return "Rumah+sakit+OR+hospital";
            }
            case "police_station": {
                return "kantor+polisi+OR+police+station";
            }
            case "fire_station": {
                return "Pemadam+Kebakaran+OR+fire+station";
            }
            case "pharmacy": {
                return "apotek+OR+pharmacy";
            }
            case "veterinary_care": {
                return "dokter+hewan+OR+vet";
            }
            case "tow_service": {
                return "derek+OR+mobil+derek+OR+Tow+Service";
            }
            case "gas_station": {
                return "pom+bensin+OR+pompa+bensin+OR+gas+station";
            }
            default: {
                return "";
            }
        }
    }
}
