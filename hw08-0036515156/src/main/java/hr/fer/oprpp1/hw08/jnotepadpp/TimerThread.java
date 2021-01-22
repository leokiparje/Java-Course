package hr.fer.oprpp1.hw08.jnotepadpp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * Class TimerThread is a helper class which represents a live timer
 * @author leokiparje
 *
 */

public class TimerThread extends Thread {

	/*
	 * Boolean value if the clock is running or not
	 */
    protected boolean isRunning;

    /*
     * Label which represents date
     */
    protected JLabel dateLabel;
    
    /*
     * Label which represents time
     */
    protected JLabel timeLabel;

    /*
     * Date format
     */
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY/MM/DD");
    
    /*
     * Time format
     */
    protected SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    /*
     * Basic constructor
     */
    public TimerThread(JLabel dateLabel, JLabel timeLabel) {
        this.dateLabel = dateLabel;
        this.timeLabel = timeLabel;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Calendar currentCalendar = Calendar.getInstance();
                    Date currentTime = currentCalendar.getTime();
                    dateLabel.setText(dateFormat.format(currentTime));
                    timeLabel.setText(timeFormat.format(currentTime));
                }
            });

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
            	e.printStackTrace();
            }
        }
    }
    
    /*
     * Helper method to set the clock boolean running to true or false
     */
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
}



































