// Generated code from Butter Knife. Do not modify!
package za.co.vehmon.application.authenticator;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class BootstrapAuthenticatorActivity$$ViewInjector {
  public static void inject(Finder finder, final za.co.vehmon.application.authenticator.BootstrapAuthenticatorActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230809, "field 'emailText'");
    target.emailText = (android.widget.AutoCompleteTextView) view;
    view = finder.findRequiredView(source, 2131230810, "field 'passwordText'");
    target.passwordText = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131230811, "field 'signInButton'");
    target.signInButton = (android.widget.Button) view;
  }

  public static void reset(za.co.vehmon.application.authenticator.BootstrapAuthenticatorActivity target) {
    target.emailText = null;
    target.passwordText = null;
    target.signInButton = null;
  }
}
