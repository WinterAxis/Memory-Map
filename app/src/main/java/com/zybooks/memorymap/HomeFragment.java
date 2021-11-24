package com.zybooks.memorymap;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.P)
public class HomeFragment extends Fragment {

    private View current;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_home, container, false);

        Button newGameBtn = parentView.findViewById(R.id.new_map_button);
        newGameBtn.setOnClickListener(v -> newMap(v));

        return parentView;
    }

    private void newMap(View view) {
        //Add prompt to select the an image file and then load the new fragment with it passed.
        mGetImageContent.launch("image/*");
        current = view;
    }

    private final ActivityResultLauncher<String> mGetImageContent = registerForActivityResult(
        new ActivityResultContracts.GetContent(),
        uri -> {
            //sets the home background to the image
//            try {
//                ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), uri);
//                Bitmap bitmap = ImageDecoder.decodeBitmap(source);
//                ImageView imageView = getActivity().findViewById(R.id.map_image);
//                imageView.setImageBitmap(bitmap);
//            } catch (IOException e) {
//
//            }
            Bundle args = new Bundle();
            ArrayList<Uri> uris = new ArrayList<Uri>();
            uris.add(uri);
            args.putParcelableArrayList(MapEditorFragment.ARG_MAP_URI, uris);

            //navs to the map editor fragment with the args
            Navigation.findNavController(current).navigate(R.id.navigation_map_editor, args);
        }
    );


}