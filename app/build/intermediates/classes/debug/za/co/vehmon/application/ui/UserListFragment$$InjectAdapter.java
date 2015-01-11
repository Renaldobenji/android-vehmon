// Code generated by dagger-compiler.  Do not edit.
package za.co.vehmon.application.ui;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<UserListFragment>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code UserListFragment} and its
 * dependencies.
 * 
 * Being a {@code Provider<UserListFragment>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<UserListFragment>} and handling injection
 * of annotated fields.
 */
public final class UserListFragment$$InjectAdapter extends Binding<UserListFragment>
    implements Provider<UserListFragment>, MembersInjector<UserListFragment> {
  private Binding<za.co.vehmon.application.BootstrapServiceProvider> serviceProvider;
  private Binding<za.co.vehmon.application.authenticator.LogoutService> logoutService;
  private Binding<ItemListFragment> supertype;

  public UserListFragment$$InjectAdapter() {
    super("za.co.vehmon.application.ui.UserListFragment", "members/za.co.vehmon.application.ui.UserListFragment", NOT_SINGLETON, UserListFragment.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    serviceProvider = (Binding<za.co.vehmon.application.BootstrapServiceProvider>) linker.requestBinding("za.co.vehmon.application.BootstrapServiceProvider", UserListFragment.class);
    logoutService = (Binding<za.co.vehmon.application.authenticator.LogoutService>) linker.requestBinding("za.co.vehmon.application.authenticator.LogoutService", UserListFragment.class);
    supertype = (Binding<ItemListFragment>) linker.requestBinding("members/za.co.vehmon.application.ui.ItemListFragment", UserListFragment.class, false, true);
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
   * {@code Provider<UserListFragment>}.
   */
  @Override
  public UserListFragment get() {
    UserListFragment result = new UserListFragment();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<UserListFragment>}.
   */
  @Override
  public void injectMembers(UserListFragment object) {
    object.serviceProvider = serviceProvider.get();
    object.logoutService = logoutService.get();
    supertype.injectMembers(object);
  }
}
