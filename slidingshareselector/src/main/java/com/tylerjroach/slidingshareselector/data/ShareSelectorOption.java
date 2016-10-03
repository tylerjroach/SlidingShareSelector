package com.tylerjroach.slidingshareselector.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tylerjroach on 10/3/16.
 */

public class ShareSelectorOption implements Parcelable {

  private int drawableRes;
  private int backgroundColorRes;
  private int callbackId;

  public ShareSelectorOption(int drawableRes, int backgroundColorRes, int callbackId) {
    this.drawableRes = drawableRes;
    this.backgroundColorRes = backgroundColorRes;
    this.callbackId = callbackId;
  }

  public int getDrawableRes() {
    return drawableRes;
  }

  public void setDrawableRes(int drawableRes) {
    this.drawableRes = drawableRes;
  }

  public int getBackgroundColorRes() {
    return backgroundColorRes;
  }

  public void setBackgroundColorRes(int backgroundColorRes) {
    this.backgroundColorRes = backgroundColorRes;
  }

  public int getCallbackId() {
    return callbackId;
  }

  public void setCallbackId(int callbackId) {
    this.callbackId = callbackId;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.drawableRes);
    dest.writeInt(this.backgroundColorRes);
    dest.writeInt(this.callbackId);
  }

  protected ShareSelectorOption(Parcel in) {
    this.drawableRes = in.readInt();
    this.backgroundColorRes = in.readInt();
    this.callbackId = in.readInt();
  }

  public static final Parcelable.Creator<ShareSelectorOption> CREATOR =
      new Parcelable.Creator<ShareSelectorOption>() {
        @Override public ShareSelectorOption createFromParcel(Parcel source) {
          return new ShareSelectorOption(source);
        }

        @Override public ShareSelectorOption[] newArray(int size) {
          return new ShareSelectorOption[size];
        }
      };
}
