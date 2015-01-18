// Generated code from Butter Knife. Do not modify!
package za.co.vehmon.application.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class CarouselFragment$$ViewInjector {
  public static void inject(Finder finder, final za.co.vehmon.application.ui.CarouselFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230800, "field 'indicator'");
    target.indicator = (com.viewpagerindicator.TitlePageIndicator) view;
    view = finder.findRequiredView(source, 2131230801, "field 'pager'");
    target.pager = (android.support.v4.view.ViewPager) view;
  }

  public static void reset(za.co.vehmon.application.ui.CarouselFragment target) {
    target.indicator = null;
    target.pager = null;
  }
}
