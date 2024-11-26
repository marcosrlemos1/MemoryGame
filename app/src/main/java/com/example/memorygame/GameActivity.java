package com.example.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private long timeRemaining; // Configurado em cada fase
    private int currentScore = 0;
    private int pairsFound = 0;
    private int errors = 0; // Contagem de erros
    private ImageView[] cardViews;
    private int[] cardImages;
    private boolean[] cardFlipped;
    private int firstCardIndex = -1;
    private boolean isClickable = true; // Controle de clique nas cartas
    private int level = 1; // Nível atual do jogo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Obter o nível da intent
        level = getIntent().getIntExtra("level", 1); // Padrão para nível 1

        TextView timerText = findViewById(R.id.timerTextView);
        TextView scoreText = findViewById(R.id.scoreTextView);

        // Inicializar o grid de cartas
        GridLayout cardGrid = findViewById(R.id.cardGrid);
        setupCards(cardGrid);

        // Temporizador regressivo
        countDownTimer = new CountDownTimer(timeRemaining, 1000) {
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                timerText.setText("Tempo: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                Intent intent = new Intent(GameActivity.this, GamerOverActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    // Função para configurar as cartas no GridLayout
    private void setupCards(GridLayout cardGrid) {
        // Definir o número de cartas e o tempo com base no nível
        switch (level) {
            case 1:
                cardImages = new int[]{
                        R.drawable.card_front_1,
                        R.drawable.card_front_2,
                        R.drawable.card_front_1,
                        R.drawable.card_front_2
                };
                timeRemaining = 60000; // 60 segundos
                break;
            case 2:
                cardImages = new int[]{
                        R.drawable.card_front_1,
                        R.drawable.card_front_2,
                        R.drawable.card_front_3, // Novo par de carta
                        R.drawable.card_front_1,
                        R.drawable.card_front_2,
                        R.drawable.card_front_3 // Par da nova carta
                };
                timeRemaining = 45000; // 45 segundos para o nível 2
                break;
            case 3:
                cardImages = new int[]{
                        R.drawable.card_front_1,
                        R.drawable.card_front_2,
                        R.drawable.card_front_3,
                        R.drawable.card_front_4, // Novo par de carta
                        R.drawable.card_front_1,
                        R.drawable.card_front_2,
                        R.drawable.card_front_3,
                        R.drawable.card_front_4 // Par da nova carta
                };
                timeRemaining = 30000; // 30 segundos para o nível 3
                break;
            // Adicione mais níveis conforme necessário
        }

        int numCards = cardImages.length;
        cardGrid.setColumnCount(2);
        cardGrid.setRowCount(numCards / 2);

        cardViews = new ImageView[numCards];
        cardFlipped = new boolean[numCards];

        shuffleCards();

        for (int i = 0; i < numCards; i++) {
            final int cardIndex = i;
            cardFlipped[i] = false;

            ImageView card = new ImageView(this);
            card.setImageResource(R.drawable.card_back);
            card.setPadding(10, 10, 10, 10);

            card.setOnClickListener(view -> {
                if (isClickable && !cardFlipped[cardIndex]) {
                    card.setImageResource(cardImages[cardIndex]);
                    cardFlipped[cardIndex] = true;

                    if (firstCardIndex == -1) {
                        firstCardIndex = cardIndex; // Primeira carta virada
                    } else {
                        isClickable = false; // Desabilitar cliques enquanto compara
                        checkForMatch(cardIndex); // Segunda carta virada
                    }
                }
            });

            cardGrid.addView(card);
            cardViews[i] = card;
        }
    }

    // Função para verificar se as cartas viradas correspondem
    private void checkForMatch(int secondCardIndex) {
        if (cardImages[firstCardIndex] == cardImages[secondCardIndex]) {
            currentScore += 10; // Pontuação base por par encontrado
            pairsFound++;

            // Atualizar pontuação na tela
            TextView scoreText = findViewById(R.id.scoreTextView);
            scoreText.setText("Pontos: " + currentScore);

            // Verificar se o jogo foi ganho
            if (pairsFound == cardImages.length / 2) {
                // Cálculo da pontuação total
                long timeLeft = timeRemaining / 1000; // Segundos restantes
                currentScore += timeLeft; // Adicionar pontos pela velocidade
                Intent intent = new Intent(GameActivity.this, WinActivity.class);
                intent.putExtra("score", currentScore);
                intent.putExtra("level", level); // Passar o nível atual
                intent.putExtra("errors", errors); // Passar o número de erros
                startActivity(intent);
                finish();
            }

            firstCardIndex = -1; // Resetar índice da primeira carta
            isClickable = true; // Habilitar cliques novamente
        } else {
            errors++; // Incrementar contagem de erros
            cardViews[firstCardIndex].postDelayed(() -> {
                cardViews[firstCardIndex].setImageResource(R.drawable.card_back);
                cardViews[secondCardIndex].setImageResource(R.drawable.card_back);
                cardFlipped[firstCardIndex] = false;
                cardFlipped[secondCardIndex] = false;
                firstCardIndex = -1; // Resetar índice da primeira carta
                isClickable = true; // Habilitar cliques novamente
            }, 1000);
        }
    }

    private void shuffleCards() {
        Random random = new Random();
        for (int i = cardImages.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = cardImages[i];
            cardImages[i] = cardImages[j];
            cardImages[j] = temp;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
