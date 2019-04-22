package com.example.loadsurvey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

public class MainActivity extends AppCompatActivity {


    private TextView scoreView;
    private TextView aQuestion;

    private Button buttonChoice1, buttonChoice2, buttonChoice3, buttonChoice4;

    private int questionNumber=0;
    private int score=0;

    private Firebase questionRef, choice1Ref, choice2Ref, choice3Ref,  choice4Ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreView = (TextView)findViewById(R.id.score);
        aQuestion = (TextView)findViewById(R.id.question);

        buttonChoice1 = (Button)findViewById(R.id.choice1);
        buttonChoice2 = (Button)findViewById(R.id.choice2);
        buttonChoice3 = (Button)findViewById(R.id.choice3);
        buttonChoice4 = (Button)findViewById(R.id.choice4);

        updateQuestion();

        buttonChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score=score+1;
                updateScore(score);
                updateQuestion();
            }
        });


        buttonChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score=score+1;
                updateScore(score);
                updateQuestion();
            }
        });

        buttonChoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score=score+1;
                updateScore(score);
                updateQuestion();
            }
        });


        buttonChoice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score=score+1;
                updateScore(score);
                updateQuestion();
            }
        });
    }

    private void updateScore(int score1) {
        scoreView.setText("" +score1);

    }

    private void updateQuestion() {
        questionRef= new Firebase("https://pransurvey-90922.firebaseio.com/" +questionNumber + "/question");
        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String question = dataSnapshot.getValue(String.class);
                aQuestion.setText(question);
            }
                 @Override
            public void onCancelled(FirebaseError firebaseError) {

                 }
        });

        choice1Ref= new Firebase("https://pransurvey-90922.firebaseio.com/" + questionNumber + "/choice1");
        choice1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String choice = dataSnapshot.getValue(String.class);
                buttonChoice1.setText(choice);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        choice2Ref= new Firebase("https://pransurvey-90922.firebaseio.com/" + questionNumber + "/choice2");
        choice2Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String choice = dataSnapshot.getValue(String.class);
                buttonChoice2.setText(choice);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        choice3Ref= new Firebase("https://pransurvey-90922.firebaseio.com/" + questionNumber + "/choice3");
        choice3Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String choice = dataSnapshot.getValue(String.class);
                buttonChoice3.setText(choice);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        choice4Ref= new Firebase("https://pransurvey-90922.firebaseio.com/" + questionNumber + "/choice4");
        choice4Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String choice = dataSnapshot.getValue(String.class);
                buttonChoice4.setText(choice);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        questionNumber++;
    }

}
