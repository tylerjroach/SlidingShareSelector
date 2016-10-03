package com.tylerjroach.slidingshareselector.example.ui.screens;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.tylerjroach.slidingshareselector.example.R;

public class SelectedImageDialogFragment extends DialogFragment {

  private static final String CONTENT_PATH = "CONTENT_PATH";

  private AppCompatActivity ACA;

  private ImageView imageView;

  private Uri contentUri;

  public SelectedImageDialogFragment() {}

  public static SelectedImageDialogFragment newInstance(String contentPath) {
    SelectedImageDialogFragment fragment = new SelectedImageDialogFragment();
    Bundle args = new Bundle();
    args.putString(CONTENT_PATH, contentPath);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      contentUri = Uri.parse(getArguments().getString(CONTENT_PATH));
    }
    setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_selected_image_dialog, container, false);
    imageView = (ImageView) rootView.findViewById(R.id.image);

    Picasso.with(ACA).load(contentUri).fit().centerCrop().into(imageView);

    return rootView;
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    ACA = (AppCompatActivity) context;
  }

  @Override public void onDetach() {
    super.onDetach();
    ACA = null;
  }
}
