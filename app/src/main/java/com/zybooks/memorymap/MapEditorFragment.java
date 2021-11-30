package com.zybooks.memorymap;

import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.io.IOException;


@RequiresApi(api = Build.VERSION_CODES.P)
public class MapEditorFragment extends Fragment {

    private LayoutInflater minflater;

    public MapEditorFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        minflater = inflater;

        View parentView = inflater.inflate(R.layout.fragment_map_editor, container, false);

        Button setMapBtn = parentView.findViewById(R.id.set_map);
        setMapBtn.setOnClickListener(v -> mGetImageContent.launch("image/*"));

        //to be removed later
        ImageView tempPin = parentView.findViewById(R.id.temp);
        tempPin.setOnClickListener(v -> { onButtonShowPopupWindowClick(v); });

        return parentView;
    }


    private final ActivityResultLauncher<String> mGetImageContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
              try {
                    ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), uri);
                    Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                    ImageView imageView = getActivity().findViewById(R.id.map_image);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {

                }
            }
    );

    public void onButtonShowPopupWindowClick(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pin_popup_window, null);
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
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
}