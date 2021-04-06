package me.theseems.tomshelby.gamblepack.api.board;

public interface Board<T> {

  T get(int x, int y);

  void set(int x, int y, T value);

  int getSize();
}
