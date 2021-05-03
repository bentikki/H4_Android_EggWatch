package com.example.eggeur_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    // Implements MVP pattern - coupling with MainPresenter
    private MainPresenter presenter;

    public MainActivity(){
        // Sets corresponding presenter class - MainPresenter
        this.presenter = new MainPresenter(this);
    }

    // TimerView object - Holds the countdown display timer
    TextView timerCountdownTextView;

    // ImageButton objects - used for selecting Egg type
    ImageButton timerSelect1, timerSelect2, timerSelect3;

    // Main button - Used to Start timer.
    Button timerStartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binds objects to view
        this.timerCountdownTextView = this.findViewById(R.id.timerCountdownTextView);
        this.timerCountdownTextView.setText("00:00");
        this.timerSelect1 = this.findViewById(R.id.timerSelect1);
        this.timerSelect2 = this.findViewById(R.id.timerSelect2);
        this.timerSelect3 = this.findViewById(R.id.timerSelect3);
        this.timerStartBtn = this.findViewById(R.id.timerStartBtn);

        // Setup initials
        this.timerStartBtn.setEnabled(false); // Sets button to disabled to force timer select.
        this.timerStartBtn.setText("Vælg type");

        // Setup events
        this.timerSelect1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.TimerTypeSelected(MainPresenter.TimerOption.TimerOption1); // Selects option 1 timer on presenter.
            }
        });

        this.timerSelect2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.TimerTypeSelected(MainPresenter.TimerOption.TimerOption2); // Selects option 2 timer on presenter.
            }
        });

        this.timerSelect3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.TimerTypeSelected(MainPresenter.TimerOption.TimerOption3); // Selects option 3 timer on presenter.
            }
        });

        timerStartBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.BeginTimer(); // Selects option 3 timer on presenter.
            }
        });
    }

    @Override
    public void SetButtonStage(boolean buttonActive){
        this.timerStartBtn.setEnabled(buttonActive);
    }

    @Override
    public void SetButtonText(String buttonText) {
        timerStartBtn.setText(buttonText);
    }

    @Override
    public void SetTimerText(String timerText) {
        this.timerCountdownTextView.setText(timerText);
    }

    @Override
    public void TimerFinished() {
        Toast toast = Toast.makeText(this, "Æg er færdigt!", Toast.LENGTH_LONG);
        toast.show();
    }

}