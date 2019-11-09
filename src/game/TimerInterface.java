package game;

import javafx.animation.Timeline;

/**
 * Handle the Timer management
 */
public interface TimerInterface {

    /**
     * Return if the timer of the player is over or not
     * @return boolean
     */
    boolean isTimeOver();

    /**
     * Return the timeline of the timer
     * @return Timeline
     */
    Timeline getTimeline();

    /**
     * Reset all timers for a new game
     */
    void reset();

}
