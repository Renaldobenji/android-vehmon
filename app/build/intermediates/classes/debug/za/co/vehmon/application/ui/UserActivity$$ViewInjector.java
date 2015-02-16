// Generated code from Butter Knife. Do not modify!
package za.co.vehmon.application.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class UserActivity$$ViewInjector {
  public static void inject(Finder finder, final za.co.vehmon.application.ui.UserActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230829, "field 'avatar'");
    target.avatar = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131230798, "field 'name'");
    target.name = (android.widget.TextView) view;
  }

  public static void reset(za.co.vehmon.application.ui.UserActivity target) {
    target.avatar = null;
    target.name = null;
  }
}
