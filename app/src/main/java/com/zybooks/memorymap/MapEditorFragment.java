package com.zybooks.memorymap;

import android.content.SharedPreferences;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    private String Map_ID = "Map_0";

    private SharedPreferences map_pref;
    private ImageView map_image_view;

    public MapEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            Map_ID = args.getString(ARG_MAP_ID);
        }
        map_pref = getContext().getSharedPreferences(Map_ID, 0);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_map_editor, container, false);
        ConstraintLayout parentLayout  = (ConstraintLayout) parentView.findViewById(R.id.ConstraintLayout_map_editor);
        ConstraintSet conSet = new ConstraintSet();

        map_image_view = parentView.findViewById(R.id.map_image);

        Button setMapBtn = parentView.findViewById(R.id.set_map);
        setMapBtn.setOnClickListener(v -> mGetImageContent.launch("image/*"));

        //add some pins to test delete later
        SharedPreferences.Editor editor = map_pref.edit();
        Set<String> set = new HashSet<String>();
        set.add("pin1");
        set.add("pin2");
        editor.putStringSet("Pins", set);
        editor.putInt("pin1_marginStart", 200);
        editor.putInt("pin1_marginTop", 300);
        editor.putString("pin1_Title", "Test1");
        editor.putString("pin1_Description", "Should load this");

        editor.putInt("pin2_marginStart", 400);
        editor.putInt("pin2_marginTop", 500);
        editor.putString("pin2_Title", "Test2");
        editor.putString("pin2_Description", "Description");
        
        editor.putInt("Next_Pin_Id", 4);
        editor.apply();

        //Retrieve the values
        Set<String> pins = map_pref.getStringSet("Pins", null);
        String map_uri_str = map_pref.getString(PREF_IMG_URI, null);
        if (map_uri_str != null) {
            setImg(Uri.parse(map_uri_str));
        }

        //Load Pins
        for (String pin : pins){

            Log.d("TAG", "onCreateView: "+pin);
            ImageView newPin = new ImageView(getContext());
            newPin.setId(View.generateViewId());
            newPin.setImageResource(R.drawable.pin_place);
            newPin.setColorFilter(ContextCompat.getColor(getContext(), R.color.blue));
            newPin.setTag(pin);
            newPin.setElevation(10);

            newPin.setOnClickListener(v -> { onButtonShowPopupWindowClick(v); });

            parentLayout.addView(newPin, 0);

            conSet.clone(parentLayout);

            // connect start and end point of views, in this case top of child to top of parent.
            conSet.connect(newPin.getId(), ConstraintSet.TOP, parentLayout.getId(), ConstraintSet.TOP, map_pref.getInt(pin+"_marginTop", 0));
            conSet.connect(newPin.getId(), ConstraintSet.START, parentLayout.getId(), ConstraintSet.START, map_pref.getInt(pin+"_marginStart", 0));
            conSet.applyTo(parentLayout);
        }

        //to be removed later just a pin for testing
//        ImageView tempPin = parentView.findViewById(R.id.temp);
//        tempPin.setOnClickListener(v -> { onButtonShowPopupWindowClick(v); });

        return parentView;
    }

    public void onButtonShowPopupWindowClick(View view) {
        Log.d("TAG", "onButtonShowPopupWindowClick: "+view.getTag());
        String pin = (String) view.getTag();

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pin_popup_window, null);

        // set the windows text
        EditText title = popupView.findViewById(R.id.pin_title);
        title.setText(map_pref.getString(pin+"_Title", "Title"));
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
        dis.setText(map_pref.getString(pin+"_Description", "Description"));
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
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        // dismiss the popup window when touched
//        popupView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                popupWindow.dismiss();
//                return true;
//            }
//        });
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

}