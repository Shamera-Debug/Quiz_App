package com.example.Quiz_App;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Fragment.DialogClickListener
{

    private int totalCorrectAnswer, totalAttempt, numberCorrectAnswer, selectedQuestion = 8, progressCounter = 0, colorNumber, qNumber;
    String str, question;
    ProgressBar bar;
    StorageManager storageManager;
    FragmentManager fm = getSupportFragmentManager();





    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putString("Question", question);
        outState.putInt("QuestionNumber", qNumber);
        outState.putInt("QuestionColorNumber", colorNumber);
        outState.putInt("ProgressCounter", progressCounter);
        outState.putInt("NumberOfQuestions", selectedQuestion);
        outState.putInt("totalNumberAnswer", totalCorrectAnswer);
        outState.putInt("totalQuestion", totalAttempt);
        outState.putInt("NumberOfAnswers", numberCorrectAnswer);
        outState.putSerializable("StorageManager", (Serializable) storageManager);
        outState.putSerializable("ProgressBar", (Serializable) bar);
        outState.putSerializable("FragmentManager", (Serializable) fm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageManager = new StorageManager();

        ArrayList<Integer> tempList;
        tempList = storageManager.readAllValuesFromFile(MainActivity.this);
        if(!tempList.isEmpty())
        {
            totalCorrectAnswer = tempList.get(0);
            totalAttempt = tempList.get(1);
            tempList.clear();
        }else
        {
            totalCorrectAnswer = 0;
            totalAttempt = 0;
        }

        bar = findViewById(R.id.quizBar);
        bar.setMax(selectedQuestion);
        bar.setProgress(progressCounter);

        displayQuestion();

        if (savedInstanceState != null)
        {
            question = savedInstanceState.getString("Question");
            qNumber =savedInstanceState.getInt("QuestionNumber");
            colorNumber = savedInstanceState.getInt("QuestionColorNumber");
            progressCounter = savedInstanceState.getInt("ProgressCounter");
            selectedQuestion = savedInstanceState.getInt("NumberOfQuestions");
            totalCorrectAnswer = savedInstanceState.getInt("totalNumberAnswer");
            totalAttempt = savedInstanceState.getInt("totalQuestion");
            numberCorrectAnswer = savedInstanceState.getInt("NumberOfAnswers");
            storageManager = (StorageManager) savedInstanceState.getSerializable("StorageManager");
            bar = (ProgressBar) savedInstanceState.getSerializable("ProgressBar");
            fm = (FragmentManager) savedInstanceState.getSerializable("FragmentManager");
        }
    }

    private void generateQuestion()
    {
        int tempQNumber = qNumber;
        while(tempQNumber == qNumber)
        {
            qNumber = new Random().nextInt(8)+1;
        }
        str = "question" + qNumber;
        colorNumber = Color.argb(255, new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256));
        question = getString(getApplicationContext().getResources().getIdentifier(str, "string", getPackageName()));
    }


    private void displayQuestion()
    {
        generateQuestion();
        QuestionFragment dialog = QuestionFragment.newInstance(question, colorNumber);
        fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fm.beginTransaction().add(R.id.questionContainer, dialog).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.subMenuAverage:
                storageManager.readAllValuesFromFile(MainActivity.this);
                ArrayList<Integer> tempList;
                tempList = storageManager.readAllValuesFromFile(MainActivity.this);
                totalCorrectAnswer = tempList.get(0);
                totalAttempt = tempList.get(1);
                tempList.clear();

                AlertDialog.Builder avgBuilder = new AlertDialog.Builder(MainActivity.this);
                avgBuilder.setMessage(getString(R.string.displayAverageMessage) + totalCorrectAnswer + "/" + totalAttempt);
                avgBuilder.setPositiveButton(
                        getString(R.string.DialogFragmentButtonSAVE), (dialogInterface, i) ->
                        {
                            Toast.makeText(getApplicationContext(), R.string.saveAverageMessage, Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        });
                avgBuilder.setNegativeButton(
                        getString(R.string.DialogFragmentButtonOK), (dialogInterface, i) -> dialogInterface.dismiss());

                AlertDialog avgAlertDialog = avgBuilder.create();
                avgAlertDialog.show();
                avgAlertDialog.setCancelable(false);
                break;

            case R.id.subMenuNumberOfQuestions:
                Fragment dialog = Fragment.newInstance(getString(R.string.DialogFragmentTextView));
                dialog.show(fm, Fragment.Tag);
                dialog.listener = this;

                dialog.setCancelable(false);
                break;

            case R.id.subMenuReset:
                totalCorrectAnswer = 0;
                totalAttempt = 0;
                storageManager.writeResultToFile(MainActivity.this, totalCorrectAnswer, totalAttempt);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dialogListenerWithQuestion(String nQuestion)
    {
        selectedQuestion = Integer.parseInt(nQuestion);
        bar.setMax(selectedQuestion);
        numberCorrectAnswer = 0;
        progressCounter = 0;
        bar.setProgress(progressCounter);
        Toast.makeText(this, nQuestion+ " " + getString(R.string.numberOfQuestionsSelected), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dialogListenerWithCancel()
    {
        Toast.makeText(this, R.string.numberOfQuestionsNotSelected, Toast.LENGTH_SHORT).show();
    }

    private String getCorrectAnswer()
    {
        return getString(getApplicationContext().getResources().getIdentifier("answer" + qNumber, "string", getPackageName()));
    }

    public void btnClick(View view)
    {

        String result = getCorrectAnswer();
        switch (view.getId())
        {
            case R.id.btnTrue:
                if(result.equals(getString(R.string.correctAnswer)))
                {
                    ++numberCorrectAnswer;
                }
                break;
            case R.id.btnFalse:
                if(result.equals(getString(R.string.inCorrectAnswer)))
                {
                    ++numberCorrectAnswer;
                }
                break;
        }
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        progressCounter++;
        bar.setProgress(progressCounter);

        if(progressCounter == selectedQuestion)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.finalResult);
            builder.setMessage(getString(R.string.finalResultMessage1)+ " " + numberCorrectAnswer + " " + getString(R.string.finalResultMessage2) + " " + selectedQuestion);
            builder.setPositiveButton(
                    getString(R.string.DialogFragmentButtonSAVE), (dialogInterface, i) ->
                    {
                        Toast.makeText(getApplicationContext(), R.string.saveAverageMessage, Toast.LENGTH_SHORT).show();
                        totalCorrectAnswer += numberCorrectAnswer;
                        totalAttempt += selectedQuestion;
                        storageManager.writeResultToFile(MainActivity.this, totalCorrectAnswer, totalAttempt);
                        numberCorrectAnswer = 0;
                        progressCounter = 0;
                        bar.setProgress(progressCounter);
                        dialogInterface.dismiss();
                    });
            builder.setNegativeButton(
                    getString(R.string.DialogFragmentButtonCancel), (dialogInterface, i) ->
                    {
                        numberCorrectAnswer = 0;
                        progressCounter = 0;
                        bar.setProgress(progressCounter);
                        dialogInterface.dismiss();
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
        }

        displayQuestion();
    }

}