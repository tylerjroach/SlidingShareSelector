# SlidingShareSelector

An adaptable BottomSheetDialogFragment that shows a scrolling list of recent photos, along with a grid of custimizeable icons for sharing. 

![demo](preview.gif?raw=true "Demo Preview")

## How to use

Create a list of share actions by passing a drawable, background color, and callback id for each option.
```
final ShareSelectorOption[] shareOptions = new ShareSelectorOption[] {
    new ShareSelectorOption(R.drawable.ic_camera_alt_white_24dp, R.color.green, SELECTION_CAMERA),
    new ShareSelectorOption(R.drawable.ic_videocam_white_24dp, R.color.red, SELECTION_VIDEO),
    new ShareSelectorOption(R.drawable.ic_image_white_24dp, R.color.blue, SELECTION_GALLERY),
    new ShareSelectorOption(R.drawable.ic_place_white_24dp, R.color.purple, SELECTION_LOCATION)
};
```
Show the sliding selector by instantiating the fragment and calling show().
```
addMedia = (ImageButton) findViewById(R.id.add_media);
addMedia.setOnClickListener(new View.OnClickListener() {
  @Override public void onClick(View v) {
    if (getSupportFragmentManager().findFragmentByTag(SlidingShareSelector.class.getName()) == null) {
      SlidingShareSelector slidingShareSelector = SlidingShareSelector.newInstance(shareOptions, true);
      slidingShareSelector.show(getSupportFragmentManager(), SlidingShareSelector.class.getName());
    }
  }
});
```
Take action based on the callback received
```
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
```

## Comments
Feel free to suggest any additions or create pull requests. 
This was created in a couple of hours and doesn't have many configuration options. 
Documentation is also non-existant, but the code should be simple to figure out. 

##Future Updates (Time Permitting)
Post to MavenCentral - For now, build aar manually<br>
Fix lazy paddings/margins/itemdecorations to properly follow material design<br>
Add documentation to code<br>
Add more customization options

## License
```
Copyright 2016 Tyler Roach

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
