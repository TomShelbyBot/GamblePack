package me.theseems.tomshelby.gamblepack.api;

import java.util.Date;

public interface TimedGame extends Game {
  /**
   * Get game's end date
   * @return until
   */
  Date getUntil();
}
