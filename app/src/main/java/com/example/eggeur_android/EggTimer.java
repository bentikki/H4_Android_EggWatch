package com.example.eggeur_android;

import android.os.CountDownTimer;

import java.util.List;

public class EggTimer {

    // Using command pattern - List of ICommands
    // Commands to execute on timer finished and ticks
    private List<ICommand> _onTimerFinished;
    private List<ICommand> _onTimerTicked;

    private CountDownTimer _countDownTimer;
    private long _timeLeft;
    private boolean _timerIsRunning;

    public EggTimer(List<ICommand> timerTickedCommand, List<ICommand> timerFinishedCommand, long timerCountdown){
        _onTimerTicked = timerTickedCommand;
        _onTimerFinished = timerFinishedCommand;

        _timeLeft = timerCountdown;
    }


    // Runs when start timer button is clicked
    public void BeginTimer(){
        if(_timerIsRunning){
            StopTimer();
        }

        StartTimingPeriod(_timeLeft);
    }

    // Stops current timer
    public void StopTimer(){
        if(_timerIsRunning){
            _countDownTimer.cancel();
            _timerIsRunning = false;
        }
    }

    public long GetCurrentTime(){
        return _timeLeft;
    }

    // Starts internal timer functionality
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

    // Runs every tick on timer
    private void OntimerTick(){

        for (ICommand command : _onTimerTicked) {
            command.Execute();
        }

    }

    // Runs when timer is finished
    private void OnTimerFinished(){
        StopTimer();

        for (ICommand command : _onTimerFinished) {
            command.Execute();
        }
    }


}
