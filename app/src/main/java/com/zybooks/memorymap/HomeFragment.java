package com.zybooks.memorymap;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.HashSet;
import java.util.Set;


@RequiresApi(api = Build.VERSION_CODES.P)
public class HomeFragment extends Fragment {

    private SharedPreferences maps_pref;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        maps_pref = context.getSharedPreferences("maps_pref", 0);
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
        Button deleteMaps = parentView.findViewById(R.id.deleteButton);
        deleteMaps.setOnClickListener(v -> delete());
        return parentView;
    }

    private void newMap(View view) {
        Log.d("Event", "newMap: Here");
        int mapNum = maps_pref.getInt("Next_Map_Id", 1);
        String map_id = "Map_"+mapNum;
        SharedPreferences.Editor editor = maps_pref.edit();
        int increase = mapNum + 1;
        //Name To Be Changed Later
        Set<String> newSet = new HashSet<String>(maps_pref.getStringSet("Maps", new HashSet<String>()));
        newSet.add(map_id);
        editor.putStringSet("Maps", newSet);
        editor.putString(map_id+"_name", "Map_1");
        editor.putString(map_id+"_file", map_id+".xml");
        editor.putInt("Next_Map_Id", increase);
        editor.apply();

        Bundle args = new Bundle();
        Log.d("TAG", "newMap: "+mapNum);
        args.putString(MapEditorFragment.ARG_MAP_ID, "Map_"+mapNum);

        Navigation.findNavController(view).navigate(R.id.navigation_map_editor, args);
    }

    private void delete(){
        SharedPreferences.Editor editor = maps_pref.edit();
        editor.clear();
        editor.putInt("Next_Map_Id", 1);
        editor.apply();
    }

}