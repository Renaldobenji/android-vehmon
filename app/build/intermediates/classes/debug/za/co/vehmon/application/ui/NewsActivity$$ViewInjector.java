// Generated code from Butter Knife. Do not modify!
package za.co.vehmon.application.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class NewsActivity$$ViewInjector {
  public static void inject(Finder finder, final za.co.vehmon.application.ui.NewsActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230807, "field 'title'");
    target.title = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131230808, "field 'content'");
    target.content = (android.widget.TextView) view;
  }

  public static void reset(za.co.vehmon.application.ui.NewsActivity target) {
    target.title = null;
    target.content = null;
  }
}
