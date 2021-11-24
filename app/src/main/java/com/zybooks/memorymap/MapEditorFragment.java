package com.zybooks.memorymap;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;


public class MapEditorFragment extends Fragment {

    private Uri uri = null;
    public static final String ARG_MAP_URI = "map_uri";

    public MapEditorFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            ArrayList<Parcelable> uris = args.getParcelableArrayList(ARG_MAP_URI);
            for (Parcelable p : uris) {
                uri = (Uri) p;
            }
            Log.d("TAG", "onCreate: before changeMapImg");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void changeMapImg(View v,Uri uri) {
        Log.d("TAG", "changeMapImg: enter: "+uri);
        try {
            ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), uri);
            Bitmap bitmap = ImageDecoder.decodeBitmap(source);
            ImageView imageView = (ImageView) v.findViewById(R.id.map_image);

//            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_map_editor, container, false);
        ImageView imageView = (ImageView) root.findViewById(R.id.map_image);
        imageView.setImageURI(uri);
        changeMapImg(root, uri);
        return root;
    }


}