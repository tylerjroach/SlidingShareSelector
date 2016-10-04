package com.tylerjroach.slidingshareselector.ui.screens;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tylerjroach.slidingshareselector.R;
import com.tylerjroach.slidingshareselector.data.ShareSelectorOption;
import com.tylerjroach.slidingshareselector.ui.views.ForegroundImageView;

/**
 * Created by @tylerjroach on 10/3/16.
 */
public class SelectionRecyclerAdapter extends RecyclerView.Adapter<SelectionRecyclerAdapter.BaseViewHolder> {

  private final int VIEW_TYPE_RECENTS_SLIDER = 1;
  private final int VIEW_TYPE_GRID_ITEM = 2;

  private Context context;

  private RecyclerView selectionRecycler;
  private RecentsRecyclerAdapter recentsRecyclerAdapter;
  private LinearLayoutManager recentsLayoutManager;
  private OptionSelectedListener listener;

  private ShareSelectorOption[] shareSelectorOptions;
  private boolean roundIconBackground;

  public SelectionRecyclerAdapter(ShareSelectorOption[] shareSelectorOptions,
      RecyclerView selectionRecycler,
      RecentsRecyclerAdapter recentsRecyclerAdapter,
      LinearLayoutManager recentsLayoutManager,
      boolean roundIconBackground,
      OptionSelectedListener optionSelectedListener,
      final Context context) {
    this.shareSelectorOptions = shareSelectorOptions;
    this.selectionRecycler = selectionRecycler;

    final GridLayoutManager selectionRecyclerLayoutManager = (GridLayoutManager) this.selectionRecycler.getLayoutManager();
    selectionRecyclerLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        if (position == 0 && selectionRecyclerLayoutManager != null)
          //position 0 is recents header
          return selectionRecyclerLayoutManager.getSpanCount();
        else
          return 1;
      }
    });

    this.recentsRecyclerAdapter = recentsRecyclerAdapter;
    this.recentsLayoutManager = recentsLayoutManager;
    this.roundIconBackground = roundIconBackground;
    this.listener = optionSelectedListener;
    this.context = context;
  }

  // Create new views (invoked by the layout manager)
  @Override public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if (viewType == VIEW_TYPE_RECENTS_SLIDER) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nested_thumbnail_recycler, parent, false);
      return  new NestedRecyclerViewHolder(v);
    } else if (viewType == VIEW_TYPE_GRID_ITEM) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_option, parent, false);
      return  new OptionViewHolder(v);
    }

    return null;

  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override public void onBindViewHolder(BaseViewHolder holder, final int position) {
    if (holder.getItemViewType() == VIEW_TYPE_RECENTS_SLIDER)
      return;

    int arrayPosition = position - 1;

    final ShareSelectorOption shareSelection = shareSelectorOptions[arrayPosition];
    OptionViewHolder optionViewHolder = (OptionViewHolder) holder;

    optionViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(shareSelection.getDrawableRes()));

    if (roundIconBackground) {
      optionViewHolder.imageView.setBackground(context.getResources().getDrawable(R.drawable.circle_background));
      GradientDrawable bgShape = (GradientDrawable) optionViewHolder.imageView.getBackground();
      bgShape.setColor(ContextCompat.getColor(context, shareSelection.getBackgroundColorRes()));
    } else {
      optionViewHolder.imageView.setBackgroundColor(ContextCompat.getColor(context, shareSelection.getBackgroundColorRes()));
    }

    optionViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (listener != null)
          listener.onOptionSelected(shareSelection.getCallbackId());
      }
    });
  }


  @Override public int getItemViewType(int position) {
    if (position == 0) {
      return VIEW_TYPE_RECENTS_SLIDER;
    }

    return VIEW_TYPE_GRID_ITEM;
  }

  @Override public int getItemCount() {

    int optionsAvailable = 0;

    if (shareSelectorOptions != null)
      optionsAvailable = shareSelectorOptions.length;

    return 1 + optionsAvailable;
  }

  public static class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View v) {
      super(v);
    }
  }

  public class NestedRecyclerViewHolder extends BaseViewHolder {
    RecyclerView nestedRecyclerView;

    public NestedRecyclerViewHolder(View v) {
      super(v);
      nestedRecyclerView = (RecyclerView) v.findViewById(R.id.nested_thumbnail_recycler);
      nestedRecyclerView.setHasFixedSize(true);
      nestedRecyclerView.setNestedScrollingEnabled(false);

      nestedRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
        @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
            RecyclerView.State state) {
          super.getItemOffsets(outRect, view, parent, state);
          int sideMargin = context.getResources().getDimensionPixelSize(R.dimen.slidingshareselector_thumbnail_grid_side_margin);
          int topBottomMargin = context.getResources().getDimensionPixelSize(R.dimen.slidingshareselector_thumbnail_grid_top_bottom_margin);
          outRect.top = topBottomMargin;
          outRect.bottom = topBottomMargin;
          outRect.left = sideMargin;
          outRect.right = sideMargin;
        }
      });

      if (recentsLayoutManager != null)
        nestedRecyclerView.setLayoutManager(recentsLayoutManager);

      if (recentsRecyclerAdapter != null)
        nestedRecyclerView.setAdapter(recentsRecyclerAdapter);
    }
  }

  public class OptionViewHolder extends BaseViewHolder {
    ForegroundImageView imageView;
    public OptionViewHolder(View v) {
      super(v);
      imageView = (ForegroundImageView) v.findViewById(R.id.icon);
    }
  }

  public void setRecentsCursor(Cursor cursor) {
    if (recentsRecyclerAdapter != null)
      recentsRecyclerAdapter.swapCursor(cursor);
  }

  public interface OptionSelectedListener {
    void onOptionSelected(int optionId);
  }
}
