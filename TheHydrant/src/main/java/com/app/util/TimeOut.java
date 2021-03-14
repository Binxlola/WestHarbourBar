package main.java.com.app.util;

import javafx.application.Platform;
import main.java.com.app.App;

import java.util.Timer;
import java.util.TimerTask;

public class TimeOut {

    Timer timer;
    public static App _Main;

    public TimeOut(App application) {
        _Main = application;
    }

    public void cancelTimer() {

        timer.cancel();

    }

    public void resetTimer() {

        timer.cancel();
        startTimer();

    }

    public void startTimer() {

        timer = new Timer();


        timer.schedule(new TimeOutTask(), 30000);

    }

    class TimeOutTask extends TimerTask {

        public void run() {

            timer.cancel(); // Terminate the timer thread

            Platform.runLater(() -> {
                _Main.logout();

            });

        }
    }

}