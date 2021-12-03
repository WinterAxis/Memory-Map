package com.zybooks.memorymap;

import android.content.ClipData;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MapListFragment extends Fragment {

    private List<String> mMaps_temp;
    private SharedPreferences maps_pref;
    private ViewGroup parentLayout;

    public MapListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMaps_temp = new ArrayList<>();
        mMaps_temp.add("Map_1");
        mMaps_temp.add("Map_2");

        //causes the app to crash and idk why
        /*Set<String> maps = maps_pref.getStringSet("Maps", null);
        for (String pin : maps) {
            parentLayout.removeView(parentLayout.findViewWithTag(pin));
        }
        for (String map : maps) {
            String map_name = maps_pref.getString(map + "_name", "map_name");
            mMaps_temp.add(map_name);
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_list, container, false);

        // Click listener for the RecyclerView
        View.OnClickListener onClickListener = itemView -> {

            Bundle args = new Bundle();
            args.putString(MapEditorFragment.ARG_MAP_ID, (String) itemView.getTag());

            Navigation.findNavController(itemView).navigate(R.id.navigation_map_editor, args);

            Log.d("TAG", "onCreateView: Thing clicked "+itemView.getTag());
        };

        // Send bands to RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.band_list);

        recyclerView.setAdapter(new MapAdapter(mMaps_temp, onClickListener));
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);

        return rootView;
    }


    private class MapAdapter extends RecyclerView.Adapter<MapHolder> {

        private final List<String> mMaps;
        private final View.OnClickListener mOnClickListener;

        public MapAdapter(List<String> maps, View.OnClickListener onClickListener) {
            mMaps = maps;
            mOnClickListener = onClickListener;
        }

        @NonNull
        @Override
        public MapHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new MapHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(MapHolder holder, int position) {
            String map_id = mMaps.get(position);
            holder.bind(map_id);
            holder.itemView.setTag(map_id);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mMaps.size();
        }
    }

    private static class MapHolder extends RecyclerView.ViewHolder {

        private final TextView mNameTextView;

        public MapHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_map, parent, false));
            mNameTextView = itemView.findViewById(R.id.map_item);
        }

        public void bind(String map_id) {
            //Update for actual name
            mNameTextView.setText(map_id);
        }
    }

}