package com.tylerjroach.slidingshareselector.ui.screens;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.tylerjroach.slidingshareselector.R;
import com.tylerjroach.slidingshareselector.data.ShareSelectorOption;
import com.tylerjroach.slidingshareselector.storage.DbHelper;

public class SlidingShareSelector extends BottomSheetDialogFragment implements
    LoaderManager.LoaderCallbacks<Cursor>, RecentsRecyclerAdapter.ThumbnailSelectionListener, SelectionRecyclerAdapter.OptionSelectedListener {

  public static final int GALLERY_LOADER_ID = 55;
  private static final String KEY_SHARE_OPTIONS = "SHARE_OPTIONS";
  private static final String KEY_ROUND_ICON_BACKGROUND = "ROUND_ICON_BACKGROUND";
  private static final String KEY_RECENTS_SAVED_STATE = "RECENTS_SAVED_STATE";

  private Context context;
  private ShareSelectorListener listener;

  private SelectionRecyclerAdapter selectionRecyclerAdapter;
  private RecentsRecyclerAdapter recentsRecyclerAdapter;

  private LinearLayoutManager recentsLayoutManager;

  private Parcelable recentsSavedState;

  private ShareSelectorOption[] shareSelectorOptions;
  private boolean roundIconBackground;

  private RecyclerView selectionRecycler;


  @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
  public SlidingShareSelector() {}

  @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)

  public static SlidingShareSelector newInstance(ShareSelectorOption[] shareSelectorOptions, boolean roundIconBackground) {
    SlidingShareSelector slidingShareSelector = new SlidingShareSelector();
    Bundle bundle = new Bundle();
    bundle.putParcelableArray(KEY_SHARE_OPTIONS, shareSelectorOptions);
    bundle.putBoolean(KEY_ROUND_ICON_BACKGROUND, roundIconBackground);
    slidingShareSelector.setArguments(bundle);
    return slidingShareSelector;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      shareSelectorOptions = (ShareSelectorOption[]) getArguments().getParcelableArray(KEY_SHARE_OPTIONS);
      roundIconBackground = getArguments().getBoolean(KEY_ROUND_ICON_BACKGROUND, true);
    }
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);

    //hacky way to fix landscape peek height. Need to set peek height before onShow
    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override public void onShow(DialogInterface dialog) {
        BottomSheetDialog d = (BottomSheetDialog) dialog;
        FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setPeekHeight(bottomSheet.getHeight());

      }
    });

    return dialog;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_selections, container, false);

    if (savedInstanceState != null)
      recentsSavedState = savedInstanceState.getParcelable(KEY_RECENTS_SAVED_STATE);

    recentsRecyclerAdapter = new RecentsRecyclerAdapter(this, null, context);
    recentsLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

    selectionRecycler = (RecyclerView) rootView.findViewById(R.id.selection_recycler);
    selectionRecyclerAdapter =
        new SelectionRecyclerAdapter(
            shareSelectorOptions,
            selectionRecycler,
            recentsRecyclerAdapter,
            recentsLayoutManager,
            roundIconBackground,
            this,
            context
        );

    selectionRecycler.setAdapter(selectionRecyclerAdapter);


    getLoaderManager().restartLoader(GALLERY_LOADER_ID, null, this);
    return rootView;
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    this.context = context;

    if (context instanceof ShareSelectorListener) {
      this.listener = (ShareSelectorListener) context;
    } else throw new IllegalStateException("Context must implement ShareSelectorListener");
  }

  @Override public void onDetach() {
    super.onDetach();
    context = null;
    listener = null;
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (recentsLayoutManager != null) {
      outState.putParcelable(KEY_RECENTS_SAVED_STATE, recentsLayoutManager.onSaveInstanceState());
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

    String[] projection = new String[] {
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATA};

    return new CursorLoader(context,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        MediaStore.Images.Media._ID + " DESC");
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    DbHelper.ThumbnailCursor thumbnailCursor = new DbHelper.ThumbnailCursor(cursor);
    selectionRecyclerAdapter.setRecentsCursor(thumbnailCursor);

    if (recentsSavedState != null)
      recentsLayoutManager.onRestoreInstanceState(recentsSavedState);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {

  }

  @Override public void onThumbnailSelected(Uri imageUri) {
    if (listener != null) {
      listener.onThumbnailClicked(imageUri);
    }
  }

  @Override public void onOptionSelected(int optionId) {
    if (listener != null) {
      listener.onShareOptionClicked(optionId);
    }
  }


  public interface ShareSelectorListener {
    void onThumbnailClicked(Uri imageUri);
    void onShareOptionClicked(int optionId);
  }

}
