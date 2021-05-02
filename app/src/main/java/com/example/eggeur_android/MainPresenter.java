package com.example.eggeur_android;

import android.os.CountDownTimer;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

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


    private EggTimer _eggTimer;

    // Set Command lists
    private List<ICommand> onTickCommands = new ArrayList<>();
    private List<ICommand> onFinishedCommands = new ArrayList<>();


    public MainPresenter(View view){
        this.view = view;

        // Using Command Pattern - Utilizing ICommand interface

        // Set on tick commands
        onTickCommands.add(new UpdateCountDownCommand(this));

        // Set on finished commands
        onFinishedCommands.add(new ShowSplashMessageCommand(this));
        onFinishedCommands.add(new ResetStartButtonCommand(this));
    }

    // Selects timer
    public void TimerTypeSelected(TimerOption optionSelected){

         // Sets timeLeft value in milliseconds based on TimerOption argument
        long countdownTime = GetTimeFromTimerOption(optionSelected);

        // Instanziates EggTimer object.
        // Takes a list of ICommand to run on Tick and Finished, takes countdown time as well
        _eggTimer = new EggTimer(onTickCommands, onFinishedCommands, countdownTime);

        UpdateTimerText(countdownTime);

        // Sets button active on View
        view.SetButtonStage(true);
        view.SetButtonText("Start timer");
    }


    // Runs when start timer button is clicked
    public void BeginTimer(){

        _eggTimer.BeginTimer();
        view.SetButtonStage(false);
        view.SetButtonText("Timer kører");
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


    // Command pattern commands
    public class UpdateCountDownCommand implements ICommand {

        MainPresenter _presenter;

        public UpdateCountDownCommand(MainPresenter presenter){
            _presenter = presenter;
        }

        @Override
        public void Execute() {
            long currentTime = _presenter._eggTimer.GetCurrentTime();
            _presenter.UpdateTimerText(currentTime);
        }
    }

    public class ShowSplashMessageCommand implements ICommand {

        MainPresenter _presenter;

        public ShowSplashMessageCommand(MainPresenter presenter){
            _presenter = presenter;
        }

        @Override
        public void Execute() {
            _presenter.view.TimerFinished();
        }
    }

    public class ResetStartButtonCommand implements ICommand {

        MainPresenter _presenter;

        public ResetStartButtonCommand(MainPresenter presenter){
            _presenter = presenter;
        }

        @Override
        public void Execute() {
            _presenter.view.SetButtonStage(false);
            _presenter.view.SetButtonText("Vælg type");
        }
    }

}
