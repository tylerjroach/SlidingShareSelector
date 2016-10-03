package com.tylerjroach.slidingshareselector.storage;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.provider.MediaStore;
import com.tylerjroach.slidingshareselector.data.Thumbnail;

/**
 * Created by tylerjroach on 9/30/16.
 */

public class DbHelper {

  /**
   * A convenience class to wrap a cursor that returns rows from the "thumbnail" table.
   */
  public static class ThumbnailCursor extends CursorWrapper {

    public ThumbnailCursor(Cursor c) {
      super(c);
    }

    /**
     * Returns a Thumbnail object configured for the current row, or null if the current row is
     * invalid.
     */
    public Thumbnail getThumbnail() {
      if (isBeforeFirst() || isAfterLast()) return null;
      int _id = getInt(getColumnIndex(MediaStore.Images.Media._ID));
      String data = getString(getColumnIndex(MediaStore.Images.Media.DATA));
      Thumbnail thumbnail = new Thumbnail(_id, data);

      return thumbnail;
    }
  }

}
