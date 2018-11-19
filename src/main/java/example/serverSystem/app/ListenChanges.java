package example.serverSystem.app;

import example.serverSystem.WeatherStation;
import example.serverSystem.WeatherStationController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class ListenChanges implements Runnable {
    private Thread t;
    private String threadName;

    ListenChanges( String name ) {
        threadName = name;
        System.out.println("Creating " +  threadName );
    }

    public void run() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime timeLater = LocalDateTime.now();
//        Duration span = Duration.between(now, timeLater);
//        System.out.println(span);

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Running " +  threadName );
                try {
                    WeatherStationController wc = new WeatherStationController();
                    WeatherStation ws = wc.getWs();
                    System.out.println("------");
                    System.out.println(ws.toString());
                    System.out.println("------");


                } catch ( Exception e ) {
                    System.out.println("Thread " +  threadName + " interrupted.");
                }
                System.out.println("Thread " +  threadName + " exiting.");
            }
        }, 0, 10, TimeUnit.SECONDS);

    }

    public void start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}
