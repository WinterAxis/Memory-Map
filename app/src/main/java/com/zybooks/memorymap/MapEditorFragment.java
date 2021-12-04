package com.zybooks.memorymap;

import android.content.ClipData;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


@RequiresApi(api = Build.VERSION_CODES.P)
public class MapEditorFragment extends Fragment {

    public static final String ARG_MAP_ID = "map_id";

    public static final String PREF_IMG_URI = "img_uri";

    private ConstraintLayout parentLayout;
    private String Map_ID = "Map_0";
    private SharedPreferences map_pref;
    private ImageView map_image_view;
    private ImageView pin_drop_view;
    private int colorId;

    public MapEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null) {
            Map_ID = args.getString(ARG_MAP_ID);
        }
        map_pref = getContext().getSharedPreferences(Map_ID, 0);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_map_editor, container, false);
        parentLayout  = (ConstraintLayout) parentView.findViewById(R.id.ConstraintLayout_map_editor);

        map_image_view = parentView.findViewById(R.id.map_image);
        map_image_view.setOnDragListener(new MyDragListener());

        SharedPreferences.Editor editor = map_pref.edit();
        if (!map_pref.contains("Next_Pin_Id")) {
            editor.putInt("Next_Pin_Id", 1);
            Set<String> set = new HashSet<String>();
            editor.putStringSet("Pins", set);
        }
        editor.apply();

        //Sets Map Image
        String map_uri_str = map_pref.getString(PREF_IMG_URI, null);
        if (map_uri_str != null) {
            setImg(Uri.parse(map_uri_str));
        }

        loadPins();

        return parentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("TAG", "onCreateOptionsMenu: ");
        inflater.inflate(R.menu.appbar_menu, menu);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService (getContext().LAYOUT_INFLATER_SERVICE);
        View sampleActionView = layoutInflater.inflate(R.layout.sample_action_view, null);
        MenuItem searchMenuItem = menu.findItem(R.id.action_pin);
        searchMenuItem.setActionView(sampleActionView);
        sampleActionView.setOnTouchListener(new MyTouchListener());
        sampleActionView.setOnDragListener(new MyDragListener());
        pin_drop_view = sampleActionView.findViewById(R.id.mobile_link_action_bar_button);
        //Lines needed to change pin_drop images
        pin_drop_view.setImageResource(R.drawable.pin_place);
        pin_drop_view.setTag("pin_place");
        Resources resources = getContext().getResources();
        colorId = resources.getIdentifier("white", "color", getContext().getPackageName());

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_set_map:
                if (menuItem.getItemId() == R.id.action_set_map)
                    mGetImageContent.launch("image/*");
                return true;
            case R.id.action_set_pin_icon:
                if (menuItem.getItemId() == R.id.action_set_pin_icon)
                    showIconPopup();
                return true;
            case R.id.action_set_pin_color:
                if (menuItem.getItemId() == R.id.action_set_pin_color)
                    showColorPopup();
                return true;

        }
        return false;
    }

    public void onClickPinPopup(View view) {
        Log.d("TAG", "onClickPinPopup: "+view.getTag());
        String pin = (String) view.getTag();

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pin_popup_window, null);

        // set the windows text
        EditText title = popupView.findViewById(R.id.pin_title);
        title.setText(map_pref.getString(pin+"_Title", ""));
        title.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = map_pref.edit();
                editor.putString(pin+"_Title", String.valueOf(s));
                editor.apply();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        EditText dis = popupView.findViewById(R.id.pin_description);
        dis.setText(map_pref.getString(pin+"_Description", ""));
        dis.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = map_pref.edit();
                editor.putString(pin+"_Description", String.valueOf(s));
                editor.apply();
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

        // show the popup window
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
    }

    public void showIconPopup() {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        GridLayout popupView = (GridLayout) inflater.inflate(R.layout.icon_popup_window, null);

        // set onClick Listeners
        Log.d("TAG", "showIconPopup: "+popupView.getChildCount());
        for(int i = 0; i < popupView.getChildCount(); i++) {
            ImageView child = (ImageView) popupView.getChildAt(i);
            child.setOnClickListener(v -> {
                Resources resources = getContext().getResources();
                int resourceId = resources.getIdentifier(String.valueOf(v.getTag()), "drawable", getContext().getPackageName());
                pin_drop_view.setImageResource(resourceId);
                pin_drop_view.setTag(v.getTag());
                Log.d("TAG", "showIconPopup: "+v.getTag());
            });
            Log.d("TAG", "showIconPopup: ");
            // do stuff with child view
        }

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }
        // show the popup window
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
    }

    public void showColorPopup() {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        GridLayout popupView = (GridLayout) inflater.inflate(R.layout.color_popup_window, null);

        // set onClick Listeners
        for(int i = 0; i < popupView.getChildCount(); i++) {
            ImageView child = (ImageView) popupView.getChildAt(i);
            child.setOnClickListener(v -> {
                Resources resources = getContext().getResources();
                colorId = resources.getIdentifier(String.valueOf(v.getTag()), "color", getContext().getPackageName());
                pin_drop_view.setColorFilter(ContextCompat.getColor(getContext(), colorId));
            });
        }

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }
        // show the popup window
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
    }

    //Gets Image from the gallery and sets it to the map image
    private final ActivityResultLauncher<String> mGetImageContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                setImg(uri);
                SharedPreferences.Editor editor = map_pref.edit();
                editor.putString(PREF_IMG_URI, uri.toString());
                editor.apply();

            }
    );

    private void setImg(Uri uri) {
        try {
            ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), uri);
            Bitmap bitmap = ImageDecoder.decodeBitmap(source);
            map_image_view.setImageBitmap(bitmap);
        } catch (IOException e) {

        }
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
//                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            if (event.getAction() == DragEvent.ACTION_DROP) {
                addPin(event);
            }
            return true;
        }
    }

    private void addPin(DragEvent event) {
        SharedPreferences.Editor editor = map_pref.edit();
        int id = map_pref.getInt("Next_Pin_Id", 0);
        String pin_id = "pin"+id;
        editor.putInt("Next_Pin_Id", id+1);
        Set<String> newSet = new HashSet<String>(map_pref.getStringSet("Pins", new HashSet<String>()));
        newSet.add(pin_id);
        editor.putStringSet("Pins", newSet);
        editor.putInt(pin_id+"_marginStart", (int) event.getX()-60);
        editor.putInt(pin_id+"_marginTop", (int) event.getY()-60);
        editor.putInt(pin_id+"_color", colorId);
        editor.putString(pin_id+"_Title", "");
        editor.putString(pin_id+"_Description", "");
        editor.putString(pin_id+"_Image_Name", String.valueOf(pin_drop_view.getTag()));
        Log.d("TAG", "addPin: "+pin_drop_view.getTag());
        editor.apply();
        loadPins();

    }

    private void loadPins() {
        Set<String> pins = map_pref.getStringSet("Pins", null);
        for (String pin : pins) {
            parentLayout.removeView(parentLayout.findViewWithTag(pin));
        }
        ConstraintSet conSet = new ConstraintSet();
        for (String pin : pins){

            Log.d("TAG", "onCreateView: "+pin);
            ImageView newPin = new ImageView(getContext());
            newPin.setId(View.generateViewId());
            //Drawable
            Resources resources = getContext().getResources();
            String pin_img_name = map_pref.getString(pin+"_Image_Name", "pin_place");
            int resourceId = resources.getIdentifier(pin_img_name, "drawable", getContext().getPackageName());
            newPin.setImageResource(resourceId);
            newPin.setColorFilter(ContextCompat.getColor(getContext(), map_pref.getInt(pin+"_color", colorId)));
            newPin.setTag(pin);
            newPin.setElevation(10);

            newPin.setOnClickListener(v -> { onClickPinPopup(v); });

            parentLayout.addView(newPin, 0);

            conSet.clone(parentLayout);

            // connect start and end point of views, in this case top of child to top of parent.
            conSet.connect(newPin.getId(), ConstraintSet.TOP, parentLayout.getId(), ConstraintSet.TOP, map_pref.getInt(pin+"_marginTop", 0));
            conSet.connect(newPin.getId(), ConstraintSet.START, parentLayout.getId(), ConstraintSet.START, map_pref.getInt(pin+"_marginStart", 0));
            conSet.applyTo(parentLayout);
        }

    }
}