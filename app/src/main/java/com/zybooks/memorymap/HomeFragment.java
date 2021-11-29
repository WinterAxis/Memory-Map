package com.zybooks.memorymap;

import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


@RequiresApi(api = Build.VERSION_CODES.P)
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_home, container, false);

        Button newMapBtn = parentView.findViewById(R.id.new_map_button);
        Log.d("Event", "newMapBtn: found?"+newMapBtn);
        newMapBtn.setOnClickListener(v -> newMap(v));

        Button listMapBtn = parentView.findViewById(R.id.list_map_button);
        listMapBtn.setOnClickListener(v -> {
            Log.d("Event", "listMapBtn: clicked?");
            Navigation.findNavController(v).navigate(R.id.navigation_map_list);
        });

        return parentView;
    }

    private void newMap(View view) {
        Log.d("Event", "newMap: Here");
        Navigation.findNavController(view).navigate(R.id.navigation_map_editor);
    }

}