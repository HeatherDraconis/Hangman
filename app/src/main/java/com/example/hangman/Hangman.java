package com.example.hangman;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

public class Hangman extends AppCompatActivity {
    private TextView hiddenWordTextView, falsePriorGuessesTextView, endMessage;
    private EditText guessLetterEditText;
    private ImageView hangedManImageView;
    private String wordString;
    private int falsePriorGuessesNum;
    private int[] hangedManImageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hangedManImageID = new int[]{R.drawable.hangman0, R.drawable.hangman1, R.drawable.hangman2,
                R.drawable.hangman3, R.drawable.hangman4, R.drawable.hangman5, R.drawable.hangman6,
                R.drawable.hangman7, R.drawable.hangman8, R.drawable.hangman9, R.drawable.hangman10, R.drawable.hangman11};

        startGame();
    }

    private void startGame() {
        setContentView(R.layout.hangman_main);

        hiddenWordTextView = findViewById(R.id.hiddenWord);
        falsePriorGuessesTextView = findViewById(R.id.priorGuesses);
        guessLetterEditText = findViewById(R.id.guessLetter);
        Button enterButton = findViewById(R.id.enterButton);
        hangedManImageView = findViewById(R.id.hangedMan);

        wordString = getWord();
        hiddenWordTextView.setText(wordString.replaceAll(" ", "\n").replaceAll("\\w", "-"));
        List<String> allGuessesList = new ArrayList<>();
        List<String> trueGuessesList = new ArrayList<>();
        StringJoiner falsePriorGuessesStringJoiner = new StringJoiner(" ");
        falsePriorGuessesTextView.setText("");
        falsePriorGuessesNum =0;

        guessLetterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (guessLetterEditText.getText().length() > 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(guessLetterEditText.getWindowToken(), 0);
                }
            }
        });

        enterButton.setOnClickListener(view -> {
            String guessLetterString = guessLetterEditText.getText().toString();
            guessLetterEditText.setText("");
            if (allGuessesList.contains(guessLetterString) || guessLetterString.length() > 1) {
                Toast.makeText(getApplicationContext(),"Not a valid input.", Toast.LENGTH_LONG).show();
            } else {
                allGuessesList.add(guessLetterString);
                if (wordString.contains(guessLetterString)) {
                    trueGuessesList.add(guessLetterString);
                    String newHiddenWord = wordString;
                    for (char ch ='A'; ch <= 'Z'; ch++) {
                        if (!trueGuessesList.contains(Character.toString(ch))) {
                            newHiddenWord = newHiddenWord.replaceAll(Character.toString(ch), "-");
                        }
                    }
                    if (wordString.equals(newHiddenWord)) {
                        wonGame();
                    }
                    hiddenWordTextView.setText(newHiddenWord.replaceAll(" ", "\n"));
                } else {
                    falsePriorGuessesNum++;
                    if (falsePriorGuessesNum == 11) {
                        lostGame();
                    }
                    falsePriorGuessesStringJoiner.add(guessLetterString);
                    falsePriorGuessesTextView.setText(falsePriorGuessesStringJoiner.toString());
                    hangedManImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), hangedManImageID[falsePriorGuessesNum], getTheme()));
                }
            }
        });
    }

    private String getWord() {
        String[] hiddenWordArray = getResources().getStringArray(R.array.hidden_words);
        Random random = new Random();
        return hiddenWordArray[random.nextInt(hiddenWordArray.length)];
    }

    private void lostGame() {
        setContentView(R.layout.failure_main);
        endMessage=findViewById(R.id.gameOver);
        endMessage.setText(String.format("Game Over\nThe word was:\n%s", wordString));
        startNewGame();
    }

    private void wonGame() {
        setContentView(R.layout.success_main);
        ImageView endHangedManImageView = findViewById(R.id.hangedManSuccess);
        endHangedManImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), hangedManImageID[falsePriorGuessesNum], getTheme()));
        endMessage = findViewById(R.id.congratulations);
        endMessage.setText(String.format("Well Done!\nThe word was:\n%s", wordString));
        startNewGame();
    }

    private void startNewGame() {
        Button newGameButton = findViewById(R.id.newGame);
        newGameButton.setOnClickListener(view -> startGame());
    }


}