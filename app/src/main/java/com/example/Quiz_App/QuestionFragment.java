package com.example.Quiz_App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class QuestionFragment extends Fragment
{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String question;
    int colorCode;

    public static QuestionFragment newInstance(String question, int colorCode)
    {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, question);
        args.putInt(ARG_PARAM2, colorCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            question = getArguments().getString(ARG_PARAM1);
            colorCode = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_question,container,false);

        ConstraintLayout layout = v.findViewById(R.id.main_fragment_Question_layout);
        TextView textView = v.findViewById(R.id.textViewQuestion);
        textView.setText(question);
        layout.setBackgroundColor(colorCode);
        return v;
    }
}
