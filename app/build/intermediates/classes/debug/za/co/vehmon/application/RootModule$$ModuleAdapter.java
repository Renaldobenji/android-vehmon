// Code generated by dagger-compiler.  Do not edit.
package za.co.vehmon.application;


import dagger.internal.ModuleAdapter;

/**
 * A manager of modules and provides adapters allowing for proper linking and
 * instance provision of types served by {@code @Provides} methods.
 */
public final class RootModule$$ModuleAdapter extends ModuleAdapter<RootModule> {
  private static final String[] INJECTS = { };
  private static final Class<?>[] STATIC_INJECTIONS = { };
  private static final Class<?>[] INCLUDES = { za.co.vehmon.application.AndroidModule.class, za.co.vehmon.application.BootstrapModule.class, };

  public RootModule$$ModuleAdapter() {
    super(INJECTS, STATIC_INJECTIONS, false /*overrides*/, INCLUDES, true /*complete*/, false /*library*/);
  }

  @Override
  protected RootModule newModule() {
    return new za.co.vehmon.application.RootModule();
  }
}
