package com.tylerjroach.slidingshareselector.ui.screens;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.tylerjroach.slidingshareselector.R;
import com.tylerjroach.slidingshareselector.data.Thumbnail;
import com.tylerjroach.slidingshareselector.storage.DbHelper;
import com.tylerjroach.slidingshareselector.ui.adapters.CursorRecyclerAdapter;

/**
 * Created by @tylerjroach on 10/3/16.
 */
public class RecentsRecyclerAdapter
    extends CursorRecyclerAdapter<RecentsRecyclerAdapter.ThumbnailViewHolder> {

  private Context context;
  private ThumbnailSelectionListener listener;

  public RecentsRecyclerAdapter(ThumbnailSelectionListener listener, Cursor cursor, Context context) {
    super(cursor);
    this.context = context;
    this.listener = listener;
  }

  @Override public ThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
    ThumbnailViewHolder vh = new ThumbnailViewHolder(v);
    return vh;
  }

  @Override public void onBindViewHolderCursor(final ThumbnailViewHolder holder, final Cursor cursor, int position) {

    final Thumbnail thumbnail = ((DbHelper.ThumbnailCursor) cursor).getThumbnail();
    final Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, thumbnail.getId());
    Picasso.with(context).load(imageUri).fit().centerCrop().into(holder.thumbnail);

    holder.thumbnail.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (listener != null)
          listener.onThumbnailSelected(imageUri);
      }
    });

  }

  @Override public int getItemViewType(int position) {
    return R.layout.grid_square_thumbnail;
  }

  public static class ThumbnailViewHolder extends RecyclerView.ViewHolder {
    private ImageView thumbnail;

    public ThumbnailViewHolder(View v) {
      super(v);
      thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
    }
  }

  public interface ThumbnailSelectionListener {
    void onThumbnailSelected(Uri imageUri);
  }
}
