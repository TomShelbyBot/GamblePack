package me.theseems.tomshelby.gamblepack.games.sapper;


import java.util.Objects;

public class SapperCell {
  public enum Type {
    BLANK, BOMB, WIN
  }

  private final Type type;
  private boolean isHidden;

  private int x, y;

  public SapperCell(Type type, boolean isHidden) {
    this.type = type;
    this.isHidden = isHidden;
  }

  public SapperCell(Type type) {
    this.type = type;
    this.isHidden = true;
  }

  public SapperCell(Type type, boolean isHidden, int x, int y) {
    this.type = type;
    this.isHidden = isHidden;
    this.x = x;
    this.y = y;
  }

  public void setHidden(boolean hidden) {
    isHidden = hidden;
  }

  public Type getType() {
    return type;
  }

  public boolean isHidden() {
    return isHidden;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SapperCell cell = (SapperCell) o;
    return isHidden == cell.isHidden && x == cell.x && y == cell.y && type == cell.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, isHidden, x, y);
  }
}