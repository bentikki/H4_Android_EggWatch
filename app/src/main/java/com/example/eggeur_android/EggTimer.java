package com.example.eggeur_android;

import android.os.CountDownTimer;

import java.util.List;

/**
 * Runs a set countdown execution ICommands on Tick and on Finished
 *
 * @author BTO
 * @version 1.0
 * @since 1.0
 */
public class EggTimer {

    // Using command pattern - List of ICommands
    // Commands to execute on timer finished and ticks
    private List<ICommand> _onTimerFinished;
    private List<ICommand> _onTimerTicked;

    private CountDownTimer _countDownTimer;
    private long _timeLeft;
    private boolean _timerIsRunning;

    /**
     * Contructor - Takes list of ICommands to run on Tick and Finished, as well as a starting countdown time.
     *
     * @param timerTickedCommand List of ICommands to run on timer Ticked
     * @param timerFinishedCommand List of ICommands to run on timer Finished
     * @param timerCountdown Starting time to count down from
     */
    public EggTimer(List<ICommand> timerTickedCommand,
                    List<ICommand> timerFinishedCommand,
                    long timerCountdown){
        _onTimerTicked = timerTickedCommand;
        _onTimerFinished = timerFinishedCommand;

        _timeLeft = timerCountdown;
    }


    /**
     * Public - Start current countdown.
     * Resets current timer if a timer is running.
     *
     */
    public void BeginTimer(){
        if(_timerIsRunning){
            StopTimer();
        }

        StartTimingPeriod(_timeLeft);
    }

    /**
     * Public - Stops current timer.
     * Does not reset current timer, if a current timer is running.
     *
     */
    public void StopTimer(){
        if(_timerIsRunning){
            _countDownTimer.cancel();
            _timerIsRunning = false;
        }
    }

    /**
     * Public - Return current timer value as long.
     *
     * @return Current time as Long
     */
    public long GetCurrentTime(){
        return _timeLeft;
    }

    /**
     * Internal - Starts current timer thread.
     * Takes Long as the countdown start.
     *
     * @param setTimeLeft Time to count down from.
     */
    private void StartTimingPeriod(long setTimeLeft){

        // Starts countdown timer
        _countDownTimer = new CountDownTimer(setTimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Execute per tick - per second.
                _timeLeft = millisUntilFinished;
                OntimerTick();
            }

            @Override
            public void onFinish() {
                OnTimerFinished(); // Executes on finished.
            }
        };
        _timerIsRunning = true;
        _countDownTimer.start();
    }

    /**
     * Internal - Run on Timer tick.
     * Executes every ICommand in _onTimerTicked list
     *
     */
    private void OntimerTick(){

        for (ICommand command : _onTimerTicked) {
            command.Execute();
        }

    }

    /**
     * Internal - Run on Timer finish.
     * Executes every ICommand in _onTimerFinished list
     *
     */
    private void OnTimerFinished(){
        StopTimer();

        for (ICommand command : _onTimerFinished) {
            command.Execute();
        }
    }


}
