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

public class DeviceDetailFragment$Adapter$FileViewHolder_ViewBinding implements Unbinder {
  private DeviceDetailFragment.Adapter.FileViewHolder target;

  @UiThread
  public DeviceDetailFragment$Adapter$FileViewHolder_ViewBinding(DeviceDetailFragment.Adapter.FileViewHolder target,
      View source) {
    this.target = target;

    target.fileName = Utils.findRequiredViewAsType(source, R.id.file_name, "field 'fileName'", TextView.class);
    target.fileSize = Utils.findRequiredViewAsType(source, R.id.file_size, "field 'fileSize'", TextView.class);
    target.action_file = Utils.findRequiredViewAsType(source, R.id.ac_file, "field 'action_file'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DeviceDetailFragment.Adapter.FileViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.fileName = null;
    target.fileSize = null;
    target.action_file = null;
  }
}
