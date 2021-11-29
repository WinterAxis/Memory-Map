package com.zybooks.memorymap;

<<<<<<< Updated upstream
import android.os.Bundle;

=======
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
>>>>>>> Stashed changes
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< Updated upstream

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapEditorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
=======
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;


@RequiresApi(api = Build.VERSION_CODES.P)
public class MapEditorFragment extends Fragment {

>>>>>>> Stashed changes

    public MapEditorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapEditorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapEditorFragment newInstance(String param1, String param2) {
        MapEditorFragment fragment = new MapEditorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< Updated upstream
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
=======
>>>>>>> Stashed changes
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
<<<<<<< Updated upstream
        return inflater.inflate(R.layout.fragment_map_editor, container, false);
    }
=======
        View parentView = inflater.inflate(R.layout.fragment_map_editor, container, false);

        Button setMapBtn = parentView.findViewById(R.id.set_map);
        setMapBtn.setOnClickListener(v -> setMap(v));
        return parentView;
    }

    private void setMap(View view) {
        mGetImageContent.launch("image/*");
    }

    private final ActivityResultLauncher<String> mGetImageContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
              try {
                    ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), uri);
                    Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                    ImageView imageView = getActivity().findViewById(R.id.map_image1);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {

                }
            }
    );

>>>>>>> Stashed changes
}