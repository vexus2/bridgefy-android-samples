// Generated code from Butter Knife. Do not modify!
package com.bridgefy.samples.fileshare;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DeviceListActivity$SimpleItemRecyclerViewAdapter$ViewHolder_ViewBinding implements Unbinder {
  private DeviceListActivity.SimpleItemRecyclerViewAdapter.ViewHolder target;

  @UiThread
  public DeviceListActivity$SimpleItemRecyclerViewAdapter$ViewHolder_ViewBinding(DeviceListActivity.SimpleItemRecyclerViewAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.mIdView = Utils.findRequiredViewAsType(source, R.id.id, "field 'mIdView'", TextView.class);
    target.mContentView = Utils.findRequiredViewAsType(source, R.id.content, "field 'mContentView'", TextView.class);
    target.deviceIcon = Utils.findRequiredViewAsType(source, R.id.device_icon, "field 'deviceIcon'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DeviceListActivity.SimpleItemRecyclerViewAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mIdView = null;
    target.mContentView = null;
    target.deviceIcon = null;
  }
}
