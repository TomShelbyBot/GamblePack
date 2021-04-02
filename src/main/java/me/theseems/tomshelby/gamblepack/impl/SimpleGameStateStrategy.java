package me.theseems.tomshelby.gamblepack.impl;

import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.api.GameStateStrategy;
import me.theseems.tomshelby.gamblepack.api.StateHandler;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleGameStateStrategy implements GameStateStrategy {

  private static class Entry<T extends GameState>
      implements Comparable<Entry<? extends GameState>> {
    private final T state;
    private final List<StateHandler<?>> handlers;

    public Entry(T state, List<StateHandler<T>> handlers) {
      this.state = state;
      this.handlers = new ArrayList<>();
      if (handlers != null) this.handlers.addAll(handlers);
    }

    @Override
    public int compareTo(@NotNull Entry<?> o) {
      return state.getName().compareTo(o.state.getName());
    }
  }

  private static class EntryStorage {
    private final TreeSet<SimpleGameStateStrategy.Entry<?>> entries;

    public EntryStorage() {
      entries = new TreeSet<>(Comparator.naturalOrder());
    }

    private <V extends GameState> SimpleGameStateStrategy.Entry<?> getEntry(V state) {
      return entries.floor(new SimpleGameStateStrategy.Entry<>(state, null));
    }

    public <V extends GameState> Collection<StateHandler<V>> getHandlers(V state) {
      SimpleGameStateStrategy.Entry<?> entry = getEntry(state);
      if (entry == null) return Collections.emptyList();

      List<StateHandler<V>> stateHandlers = new ArrayList<>();
      for (StateHandler<?> handler : entry.handlers) {
        //noinspection unchecked
        stateHandlers.add((StateHandler<V>) handler);
      }

      return stateHandlers;
    }

    public <V extends GameState> void addHandler(StateHandler<?> stateHandler, V state) {
      SimpleGameStateStrategy.Entry<?> entry = getEntry(state);

      if (entry == null || entry.state != state) {
        entry = new SimpleGameStateStrategy.Entry<>(state, new ArrayList<>());
        entry.handlers.add(stateHandler);
        entries.add(entry);
      } else entry.handlers.add(stateHandler);
    }

    public <V extends GameState> void removeHandler(StateHandler<?> stateHandler, V state) {
      SimpleGameStateStrategy.Entry<?> entry = getEntry(state);
      if (entry == null) return;

      entry.handlers.remove(stateHandler);
    }
  }

  private final EntryStorage stateHandlers;

  public SimpleGameStateStrategy() {
    stateHandlers = new EntryStorage();
  }

  /**
   * Get all state handlers there are for specific state
   *
   * @param state to get handlers of
   * @return state handlers
   */
  @Override
  public <T extends GameState> Collection<StateHandler<T>> getStateHandlers(T state) {
    return stateHandlers.getHandlers(state);
  }

  /**
   * Add state handler
   *
   * @param stateHandler to add
   */
  @Override
  public <T extends GameState> void addStateHandler(StateHandler<T> stateHandler, GameState state) {
    stateHandlers.addHandler(stateHandler, state);
  }

  /**
   * Remove state handler from handlers
   *
   * @param stateHandler to remove
   */
  @Override
  public <T extends GameState> void removeStateHandler(
      StateHandler<T> stateHandler, GameState state) {
    stateHandlers.removeHandler(stateHandler, state);
  }

  /**
   * Get all states there are for the strategy
   *
   * @return all possible states
   */
  @Override
  public Collection<GameState> getStates() {
    return stateHandlers.entries.stream()
        .map((Function<Entry<?>, GameState>) entry -> entry.state)
        .collect(Collectors.toList());
  }
}
