package com.app.tasks;

import java.util.Timer;
import java.util.TimerTask;

public class TaskTimer {

    private Timer timer;
    private long delay = 120000;
    private TimerTask task;

    public TaskTimer(TimerTask task) {
        this.task = task;
    }

    public TaskTimer(long delay, TimerTask task) {
        this.delay = delay;
        this.task = task;
    }

    /**
     * Creates a new timer object and schedules a task with a delay.
     * This will only run once, the timer will then close on itself
     */
    public void startTimer() {
        this.timer = new Timer();
        timer.schedule(task, delay);
    }

    /**
     * Creates a new timer object and schedules a reoccurring task that will start after the set delay,
     * and run repetitively after the given rate.
     * @param rate The rate at which the task should be repeated in milliseconds
     */
    public void startRepeatingTimer(long rate) {
        this.timer = new Timer();
        timer.scheduleAtFixedRate(task, delay, rate);
    }


    /**
     * Cancels the current timer, any tasks that are currently executing will not be affected.
     */
    public void cancelTimer() {
        if(timer != null) {
            timer.cancel();
        }
    }

    /**
     * Sets a new task to be run at the set delay, will then cancel the current timer and start a new one.
     * The new timer will be start with the given task.
     * @param task The task to be set on the new timer.
     */
    public void resetTimer(TimerTask task) {
        // Cannot schedule the same task, require a new instance of task
        cancelTimer();
        this.task = task;
        startTimer();
    }

    /**
     * Updates the delay time of the TaskTimer
     * @param delay delay time
     */
    public void setDelay(long delay) {
        this.delay = delay;
    }

    /**
     * Sets a new task that can be used in another timer object.
     * @param task The task to be run by a new timer object
     */
    public void setTask(TimerTask task) {
        this.task = task;
    }
}
