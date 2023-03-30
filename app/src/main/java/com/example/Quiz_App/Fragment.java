package com.example.Quiz_App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

public class Fragment extends DialogFragment
{

    public DialogClickListener listener;
    public static final String Tag = "tag";
    private static final String Arg = "arg";
    private String dTitle;



    public interface DialogClickListener
    {
        void dialogListenerWithQuestion(String nQuestion);
        void dialogListenerWithCancel();
    }



    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putSerializable("Listener", (Serializable) listener);
        outState.putString("Tag", Tag);
        outState.putString("argumentParam1", Arg);
        outState.putString("Title", dTitle);
        super.onSaveInstanceState(outState);
    }

    

    public static Fragment newInstance(String arg)
    {
        Fragment fragment = new Fragment();
        Bundle args = new Bundle();
        args.putString(Arg, arg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            dTitle = getArguments().getString(Arg);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_fragment, container, false);
        TextView textView = v.findViewById(R.id.dialog_Fragment_Title);
        EditText questionText = v.findViewById(R.id.edtTextNumberOfQuestion);
        Button ok = v.findViewById(R.id.btnFragmentOK);
        Button cancel = v.findViewById(R.id.btnFragmentCANCEL);

        textView.setText(dTitle);

        ok.setOnClickListener(view -> {
            if(!questionText.getText().toString().isEmpty()){
                if(Integer.parseInt(questionText.getText().toString()) > 0 && Integer.parseInt(questionText.getText().toString()) <= 8)
                {
                    listener.dialogListenerWithQuestion(questionText.getText().toString());
                    dismiss();
                }
                else{
                    Toast.makeText(getContext(), R.string.questionSelectionFragmentError1, Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getContext(), R.string.questionSelectionFragmentError2, Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(view -> {
            listener.dialogListenerWithCancel();
            dismiss();
        });

        return v;
    }
}
