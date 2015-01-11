// Code generated by dagger-compiler.  Do not edit.
package za.co.vehmon.application.authenticator;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<BootstrapAuthenticatorActivity>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code BootstrapAuthenticatorActivity} and its
 * dependencies.
 * 
 * Being a {@code Provider<BootstrapAuthenticatorActivity>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<BootstrapAuthenticatorActivity>} and handling injection
 * of annotated fields.
 */
public final class BootstrapAuthenticatorActivity$$InjectAdapter extends Binding<BootstrapAuthenticatorActivity>
    implements Provider<BootstrapAuthenticatorActivity>, MembersInjector<BootstrapAuthenticatorActivity> {
  private Binding<za.co.vehmon.application.core.BootstrapService> bootstrapService;
  private Binding<za.co.vehmon.application.core.VehmonService> vehmonService;
  private Binding<com.squareup.otto.Bus> bus;
  private Binding<ActionBarAccountAuthenticatorActivity> supertype;

  public BootstrapAuthenticatorActivity$$InjectAdapter() {
    super("za.co.vehmon.application.authenticator.BootstrapAuthenticatorActivity", "members/za.co.vehmon.application.authenticator.BootstrapAuthenticatorActivity", NOT_SINGLETON, BootstrapAuthenticatorActivity.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    bootstrapService = (Binding<za.co.vehmon.application.core.BootstrapService>) linker.requestBinding("za.co.vehmon.application.core.BootstrapService", BootstrapAuthenticatorActivity.class);
    vehmonService = (Binding<za.co.vehmon.application.core.VehmonService>) linker.requestBinding("za.co.vehmon.application.core.VehmonService", BootstrapAuthenticatorActivity.class);
    bus = (Binding<com.squareup.otto.Bus>) linker.requestBinding("com.squareup.otto.Bus", BootstrapAuthenticatorActivity.class);
    supertype = (Binding<ActionBarAccountAuthenticatorActivity>) linker.requestBinding("members/za.co.vehmon.application.authenticator.ActionBarAccountAuthenticatorActivity", BootstrapAuthenticatorActivity.class, false, true);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(bootstrapService);
    injectMembersBindings.add(vehmonService);
    injectMembersBindings.add(bus);
    injectMembersBindings.add(supertype);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<BootstrapAuthenticatorActivity>}.
   */
  @Override
  public BootstrapAuthenticatorActivity get() {
    BootstrapAuthenticatorActivity result = new BootstrapAuthenticatorActivity();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<BootstrapAuthenticatorActivity>}.
   */
  @Override
  public void injectMembers(BootstrapAuthenticatorActivity object) {
    object.bootstrapService = bootstrapService.get();
    object.vehmonService = vehmonService.get();
    object.bus = bus.get();
    supertype.injectMembers(object);
  }
}
