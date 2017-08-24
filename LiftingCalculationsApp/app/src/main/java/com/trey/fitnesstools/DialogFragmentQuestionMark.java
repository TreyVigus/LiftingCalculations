package com.trey.fitnesstools;


import android.app.Dialog;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

//dialog fragment launched when ? is pressed in the toolbar
public class DialogFragmentQuestionMark extends DialogFragment
{
    private TextView messageTextView;

    public static final String KEY_TEXT = "key_text";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_question_mark,null);

        //get the message from the arguments bundle and use it to set the text view.
        String message = getArguments().getString(KEY_TEXT);
        messageTextView = v.findViewById(R.id.questionMarkMessage);
        messageTextView.setText(message);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok,null)
                .create();
    }

    //instantiate the dialog, passing it the text to display
    public static DialogFragmentQuestionMark newInstance(String text)
    {
        Bundle args = new Bundle();
        args.putString(KEY_TEXT,text);
        DialogFragmentQuestionMark dialog = new DialogFragmentQuestionMark();
        dialog.setArguments(args);
        return dialog;
    }

}
