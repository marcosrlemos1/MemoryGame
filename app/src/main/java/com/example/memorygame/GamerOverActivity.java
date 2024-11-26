package com.example.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GamerOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Botão para tentar novamente
        Button retryButton = findViewById(R.id.buttonRetry);
        retryButton.setOnClickListener(v -> {
            Intent intent = new Intent(GamerOverActivity.this, GameActivity.class);
            startActivity(intent);
            finish();
        });

        // Botão para voltar ao menu
        Button backToMenuButton = findViewById(R.id.buttonBackToMenu);
        backToMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(GamerOverActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
