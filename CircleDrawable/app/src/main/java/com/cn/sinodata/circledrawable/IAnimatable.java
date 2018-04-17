package com.cn.sinodata.circledrawable;

public interface IAnimatable {

    void start();

    /**
     * Stops the drawable's animation.
     */
    void stop();

    /**
     * Indicates whether the animation is running.
     *
     * @return True if the animation is running, false otherwise.
     */
    boolean isRunning();
}
