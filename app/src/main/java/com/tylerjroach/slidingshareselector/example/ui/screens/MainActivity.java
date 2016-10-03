package com.tylerjroach.slidingshareselector.example.ui.screens;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.tylerjroach.slidingshareselector.data.ShareSelectorOption;
import com.tylerjroach.slidingshareselector.example.R;
import com.tylerjroach.slidingshareselector.ui.screens.SlidingShareSelector;

public class MainActivity extends AppCompatActivity implements
    SlidingShareSelector.ShareSelectorListener{

  private static final int SELECTION_CAMERA = 0;
  private static final int SELECTION_VIDEO = 1;
  private static final int SELECTION_GALLERY = 2;
  private static final int SELECTION_LOCATION = 3;

  private ImageButton addMedia;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final ShareSelectorOption[] shareOptions = new ShareSelectorOption[] {
        new ShareSelectorOption(R.drawable.ic_camera_alt_white_24dp, R.color.green, SELECTION_CAMERA),
        new ShareSelectorOption(R.drawable.ic_videocam_white_24dp, R.color.red, SELECTION_VIDEO),
        new ShareSelectorOption(R.drawable.ic_image_white_24dp, R.color.blue, SELECTION_GALLERY),
        new ShareSelectorOption(R.drawable.ic_place_white_24dp, R.color.purple, SELECTION_LOCATION)
    };

    addMedia = (ImageButton) findViewById(R.id.add_media);
    addMedia.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (getSupportFragmentManager().findFragmentByTag(SlidingShareSelector.class.getName()) == null) {
          SlidingShareSelector slidingShareSelector = SlidingShareSelector.newInstance(shareOptions, true);
          slidingShareSelector.show(getSupportFragmentManager(), SlidingShareSelector.class.getName());
        }
      }
    });
  }

  @Override public void onThumbnailClicked(Uri thumbUri) {
    dismissShareSelector();
    SelectedImageDialogFragment.newInstance(thumbUri.toString()).show(getSupportFragmentManager(), SelectedImageDialogFragment.class.getName());
  }

  @Override public void onShareOptionClicked(int optionId) {
    String selection = "";
    switch (optionId) {
      case SELECTION_CAMERA:
        selection = "Camera";
        break;
      case SELECTION_VIDEO:
        selection = "Video";
        break;
      case SELECTION_GALLERY:
        selection = "Gallery";
        break;
      case SELECTION_LOCATION:
        selection = "Location";
        break;
    }

    dismissShareSelector();
    Toast.makeText(this, "Selected: " + selection, Toast.LENGTH_SHORT).show();
  }

  private void dismissShareSelector() {
    SlidingShareSelector slidingShareSelector = (SlidingShareSelector) getSupportFragmentManager().findFragmentByTag(SlidingShareSelector.class.getName());
    if (slidingShareSelector != null) {
      slidingShareSelector.dismiss();
    }
  }
}
