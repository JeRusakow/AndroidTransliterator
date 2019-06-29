package com.example.Transliterator;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerLanguageFrom, spinnerLanguageTo;
    private EditText inputField;
    private TextView outputField;
    private Button transliterateButton;
    private ImageButton swapButton;

    private Controller controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new Controller();
        try {
            controller.constructLanguagesFromFile(new BufferedReader(new InputStreamReader(this.getAssets().open("languages.xml"))));
        }
        catch (IOException err){
            System.err.println("Error");
            System.err.println(err.getLocalizedMessage());
        }

        String[] languagesAvailable = controller.getAvailableLanguages();
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languagesAvailable);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLanguageFrom = findViewById(R.id.spinnerLangFrom);
        spinnerLanguageTo = findViewById(R.id.spinnerLangTo);
        inputField = findViewById(R.id.inputField);
        outputField = findViewById(R.id.outputField);
        transliterateButton = findViewById(R.id.transliteratebButton);
        swapButton = findViewById(R.id.swap_button);

        spinnerLanguageFrom.setAdapter(spinnerAdapter);

        spinnerLanguageTo.setAdapter(spinnerAdapter);

        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = spinnerLanguageTo.getSelectedItemPosition();
                spinnerLanguageTo.setSelection(spinnerLanguageFrom.getSelectedItemPosition());
                spinnerLanguageFrom.setSelection(temp);
            }
        });

        swapButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                inputField.setText(outputField.getText());
                swapButton.performClick();
                transliterateButton.performClick();

                return true;
            }
        });

        transliterateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = inputField.getText().toString();
                String originLanguage = spinnerLanguageFrom.getSelectedItem().toString();
                String destinationLanguage = spinnerLanguageTo.getSelectedItem().toString();

                inputField.setHint("Text in " + originLanguage);
                outputField.setHint("Text in " + destinationLanguage);

                outputField.clearComposingText();

                outputField.setText(controller.transliterate(text, originLanguage, destinationLanguage));
            }
        });

        //on spinner selection
        AdapterView.OnItemSelectedListener onItemClickListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String originLanguage = spinnerLanguageFrom.getSelectedItem().toString();
                String destinationLanguage = spinnerLanguageTo.getSelectedItem().toString();

                inputField.setHint("Text in " + originLanguage);
                outputField.setHint("Text in " + destinationLanguage);

                transliterateButton.setClickable(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                transliterateButton.setClickable(false);
            }
        };

        spinnerLanguageTo.setOnItemSelectedListener(onItemClickListener);
        spinnerLanguageFrom.setOnItemSelectedListener(onItemClickListener);




    }
}
