// Generated code from Butter Knife. Do not modify!
package za.co.vehmon.application.authenticator;

import android.view.View;
import butterknife.Views.Finder;

public class BootstrapAuthenticatorActivity$$ViewInjector {
  public static void inject(Finder finder, za.co.vehmon.application.authenticator.BootstrapAuthenticatorActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131230795);
    target.emailText = (android.widget.AutoCompleteTextView) view;
    view = finder.findById(source, 2131230796);
    target.passwordText = (android.widget.EditText) view;
    view = finder.findById(source, 2131230797);
    target.signInButton = (android.widget.Button) view;
  }
}
