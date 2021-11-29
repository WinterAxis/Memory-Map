package com.zybooks.memorymap;

import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;


@RequiresApi(api = Build.VERSION_CODES.P)
public class MapEditorFragment extends Fragment {


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

        View parentView = inflater.inflate(R.layout.fragment_map_editor, container, false);

        Button setMapBtn = parentView.findViewById(R.id.set_map);
        setMapBtn.setOnClickListener(v -> mGetImageContent.launch("image/*"));
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
}