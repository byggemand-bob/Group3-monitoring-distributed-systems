package com.group3.monitorServer.controller;

/*
 * Interface implemented on controllable threads
 */
public interface Controllable extends Runnable {
    /* Starts the thread execution */
    void start();

    /* Terminates the thread execution */
    void stop();

    /* Resumes the thread execution */
    void resume();

    /* Pauses thread execution */
    void pause();
}
