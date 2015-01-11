// Code generated by dagger-compiler.  Do not edit.
package za.co.vehmon.application.ui;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<BootstrapTimerActivity>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code BootstrapTimerActivity} and its
 * dependencies.
 * 
 * Being a {@code Provider<BootstrapTimerActivity>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<BootstrapTimerActivity>} and handling injection
 * of annotated fields.
 */
public final class BootstrapTimerActivity$$InjectAdapter extends Binding<BootstrapTimerActivity>
    implements Provider<BootstrapTimerActivity>, MembersInjector<BootstrapTimerActivity> {
  private Binding<com.squareup.otto.Bus> eventBus;
  private Binding<BootstrapFragmentActivity> supertype;

  public BootstrapTimerActivity$$InjectAdapter() {
    super("za.co.vehmon.application.ui.BootstrapTimerActivity", "members/za.co.vehmon.application.ui.BootstrapTimerActivity", NOT_SINGLETON, BootstrapTimerActivity.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    eventBus = (Binding<com.squareup.otto.Bus>) linker.requestBinding("com.squareup.otto.Bus", BootstrapTimerActivity.class);
    supertype = (Binding<BootstrapFragmentActivity>) linker.requestBinding("members/za.co.vehmon.application.ui.BootstrapFragmentActivity", BootstrapTimerActivity.class, false, true);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(eventBus);
    injectMembersBindings.add(supertype);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<BootstrapTimerActivity>}.
   */
  @Override
  public BootstrapTimerActivity get() {
    BootstrapTimerActivity result = new BootstrapTimerActivity();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<BootstrapTimerActivity>}.
   */
  @Override
  public void injectMembers(BootstrapTimerActivity object) {
    object.eventBus = eventBus.get();
    supertype.injectMembers(object);
  }
}
