package com.example.eggeur_android;

import android.os.CountDownTimer;
import android.view.View;

public class MainPresenter {

    // Implements MVP pattern - Using MainActivity as View
    View view;

    // View contract
    public interface View{
        void SetButtonStage(boolean buttonActive);
        void SetButtonText(String buttonText);
        void SetTimerText(String timerText);
        void TimerFinished();
    }

    // Using enum with timer options
    public enum TimerOption {
        TimerOption1,
        TimerOption2,
        TimerOption3
    }

    private CountDownTimer countDownTimer;
    private long timeLeft;
    private boolean timerIsRunning;

    public MainPresenter(View view){
        this.view = view;
        SetDefaultTime();
    }

    // Selects timer
    public void TimerTypeSelected(TimerOption optionSelected){

         // Sets timeLeft value in milliseconds based on TimerOption argument
        this.timeLeft = GetTimeFromTimerOption(optionSelected);

        // Sets button active on View
        view.SetButtonStage(true);

        // Stops current timer
        StopTimer();

        UpdateTimerText(timeLeft);
        view.SetButtonText("Start timer");
    }

    // Runs when start timer button is clicked
    public void BeginTimer(){
        if(timerIsRunning){
            StopTimer();
        }

        StartTimingPeriod(timeLeft);
        view.SetButtonStage(false);
        view.SetButtonText("Timer kører");
    }

    // Stops current timer
    public void StopTimer(){
        if(timerIsRunning){
            countDownTimer.cancel();
            timerIsRunning = false;
        }
    }

    // Starts internal timer functionality
    private void StartTimingPeriod(long setTimeLeft){

        // Starts countdown timer
        countDownTimer = new CountDownTimer(setTimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Execute per tick - per second.
                timeLeft = millisUntilFinished;
                OntimerTick();
            }

            @Override
            public void onFinish() {
                OnTimerFinished(); // Executes on finished.
            }
        };
        timerIsRunning = true;
        countDownTimer.start();
    }

    // Runs every tick on timer
    private void OntimerTick(){
        UpdateTimerText(timeLeft);
    }

    // Runs when timer is finished
    private void OnTimerFinished(){
        StopTimer();
        SetDefaultTime();

        view.SetButtonStage(false);
        view.SetButtonText("Vælg type");
        view.TimerFinished();
    }

    private void UpdateTimerText(long currentTime){
        String timeLeftFormatted = FormatTimerText(currentTime); // Gets time formatted mm:ss
        view.SetTimerText(timeLeftFormatted); // Set timerText TextView object text on view
    }



    // Takes currentTime int as time in milliseconds - Returns formatted string as mm:ss
    private String FormatTimerText(long currentTime){
        long minutesLeft = currentTime / 60000; // Gets minutes
        long secondsLeft = currentTime % 60000 / 1000; // Gets seconds

        // Set a formatted string
        String timeLeftFormatted = "";
        timeLeftFormatted += minutesLeft < 10 ? "0" +  minutesLeft : minutesLeft;
        timeLeftFormatted += ":";
        timeLeftFormatted += secondsLeft < 10 ? "0" +  secondsLeft : secondsLeft;

        return timeLeftFormatted;
    }

    private long GetTimeFromTimerOption(TimerOption optionSelected){

        long timeSelectedInMillis = 0;

        // Sets timeLeft value in milliseconds based on TimerOption argument
        switch (optionSelected) {
            case TimerOption1:
                timeSelectedInMillis = 60000;
                break;

            case TimerOption2:
                timeSelectedInMillis = 120000;
                break;

            case TimerOption3:
                timeSelectedInMillis = 180000;
                break;
        }

        return timeSelectedInMillis;
    }

    private void SetDefaultTime(){
        this.timeLeft = GetTimeFromTimerOption(TimerOption.TimerOption1); // Sets standard time
    }
}
