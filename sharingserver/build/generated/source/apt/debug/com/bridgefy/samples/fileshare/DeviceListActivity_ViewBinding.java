// Generated code from Butter Knife. Do not modify!
package com.bridgefy.samples.fileshare;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DeviceListActivity_ViewBinding implements Unbinder {
  private DeviceListActivity target;

  @UiThread
  public DeviceListActivity_ViewBinding(DeviceListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public DeviceListActivity_ViewBinding(DeviceListActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.fab = Utils.findRequiredViewAsType(source, R.id.fab, "field 'fab'", FloatingActionButton.class);
    target.recyclerView = Utils.findRequiredView(source, R.id.device_list, "field 'recyclerView'");
  }

  @Override
  @CallSuper
  public void unbind() {
    DeviceListActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.fab = null;
    target.recyclerView = null;
  }
}
