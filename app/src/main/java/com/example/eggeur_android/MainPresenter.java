package com.example.eggeur_android;

import android.os.CountDownTimer;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for controlling timers
 * Includes View Contract for use in Activities.
 *
 * This presenter allows timer countdown options, and allows commands to be run on Timer Tick and Finished.
 * Utilizing Command pattern with ICommand interface.
 *
 * @author BTO
 * @version 1.0
 * @since 1.0
 */
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

    /**
     * Constructor for Presenter
     * Takes view which implements View contract.
     *
     * @param view A view implementing View contract interface
     *
     */
    public MainPresenter(View view){
        this.view = view;

        // Using Command Pattern - Utilizing ICommand interface

        // Set on tick commands
        onTickCommands.add(new UpdateCountDownCommand(this));

        // Set on finished commands
        onFinishedCommands.add(new ShowSplashMessageCommand(this));
        onFinishedCommands.add(new ResetStartButtonCommand(this));
    }

    /**
     * Public - Selects the allowed timer option from TimerOption Enum.
     * Starts a new EffTimer object, and allows the start button to be active.
     *
     * @param optionSelected TimerOption selected - which holds countdown time.
     *
     */
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


    /**
     * Public - Begins the countdown timer.
     * Deactivates start button and alters text to relect this.
     *
     */
    public void BeginTimer(){
        if(this._eggTimer == null){
            throw new NullPointerException();
        }

        _eggTimer.BeginTimer();
        view.SetButtonStage(false);
        view.SetButtonText("Timer kører");
    }

    /**
     * Internal - Updates the timer text on View.
     *
     * @param currentTime takes the current time, used to update the view.
     */
    private void UpdateTimerText(long currentTime){
        String timeLeftFormatted = FormatTimerText(currentTime); // Gets time formatted mm:ss
        view.SetTimerText(timeLeftFormatted); // Set timerText TextView object text on view
    }

    /**
     * Internal - Takes currentTime long as time in milliseconds.
     * Returns formatted string as mm:ss
     *
     * @param currentTime Takes Long param to be formatted.
     *
     * @return String formatted as mm:ss
     */
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

    /**
     * Internal - Takes TimerOption to be converted to Long value.
     *
     * @param optionSelected Takes Long param to be formatted.
     *
     * @return time as Long in ms
     */
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


    /**
     * ICommand to be run on timer Tick or Finished.
     * UpdateCountDownCommand updates countdown on view.
     */
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

    /**
     * ICommand to be run on timer Tick or Finished.
     * ShowSplashMessageCommand shows splah message on view.
     */
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

    /**
     * ICommand to be run on timer Tick or Finished.
     * ResetStartButtonCommand resets the start button.
     */
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
