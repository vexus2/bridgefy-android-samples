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

public class DeviceDetailActivity_ViewBinding implements Unbinder {
  private DeviceDetailActivity target;

  @UiThread
  public DeviceDetailActivity_ViewBinding(DeviceDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public DeviceDetailActivity_ViewBinding(DeviceDetailActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.detail_toolbar, "field 'toolbar'", Toolbar.class);
    target.fab = Utils.findRequiredViewAsType(source, R.id.fab, "field 'fab'", FloatingActionButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DeviceDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.fab = null;
  }
}
