package com.tylerjroach.slidingshareselector.ui.adapters;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Matthieu Harlé
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

/**
 * Provide a {@link android.support.v7.widget.RecyclerView.Adapter} implementation with cursor
 * support.
 *
 * Child classes only need to implement {@link #onCreateViewHolder(android.view.ViewGroup, int)}
 * and
 * {@link #onBindViewHolderCursor(android.support.v7.widget.RecyclerView.ViewHolder,
 * Cursor, int)}.
 *
 * This class does not implement deprecated fields and methods from CursorAdapter! Incidentally,
 * only {@link android.widget.CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER} is available, so the
 * flag is implied, and only the Adapter behavior using this flag has been ported.
 *
 * @param <VH> {@inheritDoc}
 * @see android.support.v7.widget.RecyclerView.Adapter
 * @see android.widget.CursorAdapter
 * @see android.widget.Filterable
 */
public abstract class CursorRecyclerAdapter<VH extends android.support.v7.widget.RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {
  protected boolean mDataValid;
  protected int mRowIDColumn;
  protected Cursor mCursor;

  public CursorRecyclerAdapter(Cursor cursor) {
    init(cursor);
  }

  void init(Cursor c) {
    boolean cursorPresent = c != null;
    mCursor = c;
    mDataValid = cursorPresent;
    mRowIDColumn = cursorPresent ? c.getColumnIndexOrThrow("_id") : -1;
    setHasStableIds(true);
  }

  /**
   * This method will move the Cursor to the correct position and call
   * {@link #onBindViewHolderCursor(android.support.v7.widget.RecyclerView.ViewHolder,
   * Cursor, int)}.
   *
   * @param holder {@inheritDoc}
   * @param i {@inheritDoc}
   */
  @Override public void onBindViewHolder(VH holder, int i) {
    if (!mDataValid) {
      throw new IllegalStateException("this should only be called when the cursor is valid");
    }
    if (!mCursor.moveToPosition(i)) {
      throw new IllegalStateException("couldn't move cursor to position " + i);
    }
    onBindViewHolderCursor(holder, mCursor, i);
  }

  /**
   * See {@link android.widget.CursorAdapter#bindView(android.view.View, android.content.Context,
   * Cursor)},
   * {@link #onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder, int)}
   *
   * @param holder View holder.
   * @param cursor The cursor from which to get the data. The cursor is already
   * moved to the correct position.
   */
  public abstract void onBindViewHolderCursor(VH holder, Cursor cursor, int position);

  @Override public int getItemCount() {
    if (mDataValid && mCursor != null) {
      return mCursor.getCount();
    } else {
      return 0;
    }
  }

  /**
   * @see android.widget.ListAdapter#getItemId(int)
   */
  @Override public long getItemId(int position) {
    if (mDataValid && mCursor != null && !mCursor.isClosed()) {
      if (mCursor.moveToPosition(position)) {
        return mCursor.getLong(mRowIDColumn);
      } else {
        return 0;
      }
    } else {
      return 0;
    }
  }

  public Cursor getCursor() {
    return mCursor;
  }

  /**
   * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
   * closed.
   *
   * @param cursor The new cursor to be used
   */
  public void changeCursor(Cursor cursor) {
    Cursor old = swapCursor(cursor);
    if (old != null) {
      old.close();
    }
  }

  /**
   * Swap in a new Cursor, returning the old Cursor.  Unlike
   * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
   * closed.
   *
   * @param newCursor The new cursor to be used.
   * @return Returns the previously set Cursor, or null if there was not one.
   * If the given new Cursor is the same instance is the previously set
   * Cursor, null is also returned.
   */
  public Cursor swapCursor(Cursor newCursor) {
    if (newCursor == mCursor) {
      return null;
    }
    Cursor oldCursor = mCursor;

    mCursor = newCursor;
    if (newCursor != null) {
      mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
      mDataValid = true;
      // notify the observers about the new cursor
      notifyDataSetChanged();
    } else {
      mRowIDColumn = -1;
      mDataValid = false;
      // notify the observers about the lack of a data set
      // notifyDataSetInvalidated();
      notifyItemRangeRemoved(0, getItemCount());
    }
    return oldCursor;
  }

  /**
   * <p>Converts the cursor into a CharSequence. Subclasses should override this
   * method to convert their results. The default implementation returns an
   * empty String for null values or the default String representation of
   * the value.</p>
   *
   * @param cursor the cursor to convert to a CharSequence
   * @return a CharSequence representing the value
   */
  public CharSequence convertToString(Cursor cursor) {
    return cursor == null ? "" : cursor.toString();
  }
}