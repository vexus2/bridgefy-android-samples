// Generated code from Butter Knife. Do not modify!
package com.bridgefy.samples.fileshare;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.AutoCompleteTextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target, View source) {
    this.target = target;

    target.userName = Utils.findRequiredViewAsType(source, R.id.user_name, "field 'userName'", AutoCompleteTextView.class);
    target.mProgressView = Utils.findRequiredView(source, R.id.login_progress, "field 'mProgressView'");
    target.mLoginFormView = Utils.findRequiredView(source, R.id.login_form, "field 'mLoginFormView'");
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.userName = null;
    target.mProgressView = null;
    target.mLoginFormView = null;
  }
}
