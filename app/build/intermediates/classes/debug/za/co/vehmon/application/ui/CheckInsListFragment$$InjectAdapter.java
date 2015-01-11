// Code generated by dagger-compiler.  Do not edit.
package za.co.vehmon.application.ui;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<CheckInsListFragment>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code CheckInsListFragment} and its
 * dependencies.
 * 
 * Being a {@code Provider<CheckInsListFragment>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<CheckInsListFragment>} and handling injection
 * of annotated fields.
 */
public final class CheckInsListFragment$$InjectAdapter extends Binding<CheckInsListFragment>
    implements Provider<CheckInsListFragment>, MembersInjector<CheckInsListFragment> {
  private Binding<za.co.vehmon.application.BootstrapServiceProvider> serviceProvider;
  private Binding<za.co.vehmon.application.authenticator.LogoutService> logoutService;
  private Binding<ItemListFragment> supertype;

  public CheckInsListFragment$$InjectAdapter() {
    super("za.co.vehmon.application.ui.CheckInsListFragment", "members/za.co.vehmon.application.ui.CheckInsListFragment", NOT_SINGLETON, CheckInsListFragment.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    serviceProvider = (Binding<za.co.vehmon.application.BootstrapServiceProvider>) linker.requestBinding("za.co.vehmon.application.BootstrapServiceProvider", CheckInsListFragment.class);
    logoutService = (Binding<za.co.vehmon.application.authenticator.LogoutService>) linker.requestBinding("za.co.vehmon.application.authenticator.LogoutService", CheckInsListFragment.class);
    supertype = (Binding<ItemListFragment>) linker.requestBinding("members/za.co.vehmon.application.ui.ItemListFragment", CheckInsListFragment.class, false, true);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(serviceProvider);
    injectMembersBindings.add(logoutService);
    injectMembersBindings.add(supertype);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<CheckInsListFragment>}.
   */
  @Override
  public CheckInsListFragment get() {
    CheckInsListFragment result = new CheckInsListFragment();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<CheckInsListFragment>}.
   */
  @Override
  public void injectMembers(CheckInsListFragment object) {
    object.serviceProvider = serviceProvider.get();
    object.logoutService = logoutService.get();
    supertype.injectMembers(object);
  }
}
