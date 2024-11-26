package com.example.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        // Obter a pontuação e os erros passados pela GameActivity
        int score = getIntent().getIntExtra("score", 0);
        int errors = getIntent().getIntExtra("errors", 0);

        // Ajustar a pontuação com base nos erros
        if (errors == 0) {
            score += 20; // Pontos extras se não houver erros
        } else {
            score -= errors * 5; // Reduzir pontos por erro
        }

        // Exibir a pontuação na tela
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("Pontos: " + score);

        // Botão para próxima fase
        Button nextPhaseButton = findViewById(R.id.nextPhaseButton);
        nextPhaseButton.setOnClickListener(view -> {
            Intent intent = new Intent(WinActivity.this, GameActivity.class);
            intent.putExtra("level", getIntent().getIntExtra("level", 1) + 1); // Passar o próximo nível
            startActivity(intent);
            finish();
        });

        // Botão para voltar ao menu
        Button menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(view -> {
            Intent intent = new Intent(WinActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
