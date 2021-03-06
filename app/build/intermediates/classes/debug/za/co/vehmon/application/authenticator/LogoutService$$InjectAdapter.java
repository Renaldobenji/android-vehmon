// Code generated by dagger-compiler.  Do not edit.
package za.co.vehmon.application.authenticator;


import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<LogoutService>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code LogoutService} and its
 * dependencies.
 * 
 * Being a {@code Provider<LogoutService>} and handling creation and
 * preparation of object instances.
 */
public final class LogoutService$$InjectAdapter extends Binding<LogoutService>
    implements Provider<LogoutService> {
  private Binding<android.content.Context> context;
  private Binding<android.accounts.AccountManager> accountManager;

  public LogoutService$$InjectAdapter() {
    super("za.co.vehmon.application.authenticator.LogoutService", "members/za.co.vehmon.application.authenticator.LogoutService", NOT_SINGLETON, LogoutService.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    context = (Binding<android.content.Context>) linker.requestBinding("android.content.Context", LogoutService.class);
    accountManager = (Binding<android.accounts.AccountManager>) linker.requestBinding("android.accounts.AccountManager", LogoutService.class);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    getBindings.add(context);
    getBindings.add(accountManager);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<LogoutService>}.
   */
  @Override
  public LogoutService get() {
    LogoutService result = new LogoutService(context.get(), accountManager.get());
    return result;
  }
}
