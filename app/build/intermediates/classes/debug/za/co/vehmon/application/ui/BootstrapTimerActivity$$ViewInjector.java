// Generated code from Butter Knife. Do not modify!
package za.co.vehmon.application.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class BootstrapTimerActivity$$ViewInjector {
  public static void inject(Finder finder, final za.co.vehmon.application.ui.BootstrapTimerActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230793, "field 'chronometer'");
    target.chronometer = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131230794, "field 'start'");
    target.start = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131230795, "field 'stop'");
    target.stop = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131230796, "field 'pause'");
    target.pause = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131230797, "field 'resume'");
    target.resume = (android.widget.Button) view;
  }

  public static void reset(za.co.vehmon.application.ui.BootstrapTimerActivity target) {
    target.chronometer = null;
    target.start = null;
    target.stop = null;
    target.pause = null;
    target.resume = null;
  }
}
