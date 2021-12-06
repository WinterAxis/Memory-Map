package com.zybooks.memorymap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MapListFragment extends Fragment {

    private List<String> mMaps_List;
    private SharedPreferences maps_pref;
    private ViewGroup parentLayout;

    public MapListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //grabs all information from our shared preference
        maps_pref = getContext().getSharedPreferences("maps_pref", 0);
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflates the fragment_map_list layout
        //One large view consisting of a recycler view
        View rootView = inflater.inflate(R.layout.fragment_map_list, container, false);

        // List of map names
        mMaps_List = new ArrayList<>();
        Set<String> maps = maps_pref.getStringSet("Maps", new HashSet<>());
        for (String map : maps) {
            mMaps_List.add(map);
        }

        // Click listener for the RecyclerView
        View.OnClickListener onClickListener = itemView -> {

            Bundle args = new Bundle();
            args.putString(MapEditorFragment.ARG_MAP_ID, (String) itemView.getTag());

            //navigate to the corresponding edit view
            Navigation.findNavController(itemView).navigate(R.id.navigation_map_editor, args);

        };

        // Send maps to RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.map_list);
        recyclerView.setAdapter(new MapAdapter(mMaps_List, onClickListener));

        //add a horizontal line to separate inserted maps
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);

        return rootView;
    }

    //extends RecyclerView.Adapter
    private class MapAdapter extends RecyclerView.Adapter<MapHolder> {

        private final List<String> mMaps;
        private final View.OnClickListener mOnClickListener;

        //takes a list of strings of the map ids (map_id)
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

        //sets tags, onClickListeners, and passes context to the MapHolder
        @Override
        public void onBindViewHolder(MapHolder holder, int position) {
            String map_id = mMaps.get(position);
            holder.bind(map_id, maps_pref.getString(map_id+"_name", "Unnamed"));
            holder.itemView.setTag(map_id);
            holder.itemView.setOnClickListener(mOnClickListener);
            holder.context = getContext();
        }

        @Override
        public int getItemCount() {
            return mMaps.size();
        }
    }


    //extends RecyclerView.ViewHolder
    private static class MapHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "MyViewHolder";
        private final TextView mNameTextView;
        private final ImageButton mDeleteButton;
        private final ImageButton mRenameButton;
        private SharedPreferences maps_pref;
        public Context context;

        //inflates the list_item_map
        //sets up the individual views used in the recycler view
        public MapHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_map, parent, false));
            mNameTextView = itemView.findViewById(R.id.map_item);
            mDeleteButton = itemView.findViewById(R.id.deleteMap);
            mRenameButton = itemView.findViewById(R.id.renameMap);
            mDeleteButton.setOnClickListener(this);
            mRenameButton.setOnClickListener(this);
        }

        public void bind(String map_id, String map_name) {
            mNameTextView.setText(map_name);
            mDeleteButton.setTag(map_id);
            mRenameButton.setTag(map_id);
        }

        //sends to the corresponding method for the clicks
        @Override
        public void onClick(View view) {
            int mapNum = getAdapterPosition() +1;
            if(view == mDeleteButton){
                deleteMap(view, context);
            }else{
                changeName(view, context);
            }

        }


        public void deleteMap(View view, Context context) {
            SharedPreferences maps_pref = context.getSharedPreferences("maps_pref", 0);
            SharedPreferences.Editor editor = maps_pref.edit();
            Set<String> newSet = new HashSet<String>(maps_pref.getStringSet("Maps", new HashSet<String>()));
            newSet.remove(view.getTag());
            editor.putStringSet("Maps", newSet);

            //removes the map from the shared preference set
            editor.remove(view.getTag()+"_name");
            editor.apply();

            //deletes the corresponding preference file
            String filePath = context.getFilesDir().getParent()+"/shared_prefs/"+view.getTag()+".xml";
            File deletePrefFile = new File(filePath );
            deletePrefFile.delete();

            //navigates back to the homeFragment
            Navigation.findNavController(view).navigateUp();
        }

        private void changeName(View view, Context context) {
            SharedPreferences maps_pref = context.getSharedPreferences("maps_pref", 0);
            String map = (String) view.getTag();

            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.name_popup_window, null);

            // set the windows text
            EditText title = popupView.findViewById(R.id.map_title);
            title.setText(maps_pref.getString(map+"_name", ""));

            //changed text listener
            title.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    SharedPreferences.Editor editor = maps_pref.edit();
                    editor.putString(map+"_name", String.valueOf(s));
                    editor.apply();
                    mNameTextView.setText(s);
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
            });

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.setElevation(20);
            }

            // show the popup window in the center
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        }
    }

}