package com.tylerjroach.slidingshareselector.data;

/**
 * Created by tylerjroach on 9/30/16.
 */

public class Thumbnail {
  private int _id;
  private String path;

  public Thumbnail(int _id, String data) {
    this._id = _id;
    this.path = "file://" + data;
  }

  public int getId() {
    return _id;
  }

  public String getPath() {
    return path;
  }

}
