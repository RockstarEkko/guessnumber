package com.example.guessnumber;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tvGuessCount;
    private TextView tvResult;
    private EditText etGuess;
    private Button btnGuess;
    private Button btnReset;
    private int secretNumber;
    private int guessCount;
    private final int MAX_GUESSES = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图组件
        tvGuessCount = findViewById(R.id.tvGuessCount);
        tvResult = findViewById(R.id.tvResult);
        etGuess = findViewById(R.id.etGuess);
        btnGuess = findViewById(R.id.btnGuess);
        btnReset = findViewById(R.id.btnReset);

        // 初始化游戏
        initGame();

        // 设置按钮点击事件
        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeGuess();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initGame();
            }
        });
    }

    private void initGame() {
        // 生成1-100之间的随机数
        Random random = new Random();
        secretNumber = random.nextInt(100) + 1;
        guessCount = 0;
        tvGuessCount.setText(getString(R.string.guess_count, guessCount, MAX_GUESSES));
        tvResult.setText(R.string.initial_hint);
        etGuess.setText("");
        etGuess.setEnabled(true);
        btnGuess.setEnabled(true);
    }

    private void makeGuess() {
        String guessText = etGuess.getText().toString();

        if (guessText.isEmpty()) {
            Toast.makeText(this, R.string.enter_number, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int userGuess = Integer.parseInt(guessText);

            // 检查猜测范围
            if (userGuess < 1 || userGuess > 100) {
                Toast.makeText(this, R.string.number_range_error, Toast.LENGTH_SHORT).show();
                return;
            }

            guessCount++;
            tvGuessCount.setText(getString(R.string.guess_count, guessCount, MAX_GUESSES));

            if (userGuess < secretNumber) {
                tvResult.setText(R.string.too_low);
            } else if (userGuess > secretNumber) {
                tvResult.setText(R.string.too_high);
            } else {
                // 猜对了
                showWinDialog();
                etGuess.setEnabled(false);
                btnGuess.setEnabled(false);
            }

            // 检查是否达到最大猜测次数
            if (guessCount >= MAX_GUESSES) {
                showLoseDialog();
                etGuess.setEnabled(false);
                btnGuess.setEnabled(false);
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.invalid_number, Toast.LENGTH_SHORT).show();
        }
    }

    private void showWinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.congratulations)
                .setMessage(getString(R.string.correct_answer, secretNumber, guessCount))
                .setPositiveButton(R.string.play_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initGame();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void showLoseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.game_over)
                .setMessage(getString(R.string.out_of_guesses, secretNumber))
                .setPositiveButton(R.string.play_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initGame();
                    }
                })
                .setCancelable(false)
                .show();
    }
}