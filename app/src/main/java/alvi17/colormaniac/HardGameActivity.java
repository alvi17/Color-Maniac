package alvi17.colormaniac;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Random;

import alvi17.colormaniac.util.BetterColor;


public class HardGameActivity extends MainGameActivity {

    private ArrayList<Button> buttonList;
    AdView adView;
    AdRequest adRequest;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_game);
        setupProgressView();

        POINT_INCREMENT = 4;
        TIMER_BUMP = 2;

        gameMode = GameMode.HARD;

        // buttons
        Button button_1 = (Button) findViewById(R.id.button_1);
        Button button_2 = (Button) findViewById(R.id.button_2);
        Button button_3 = (Button) findViewById(R.id.button_3);
        Button button_4 = (Button) findViewById(R.id.button_4);

        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);
        button_3.setOnClickListener(this);
        button_4.setOnClickListener(this);

        buttonList = new ArrayList<Button>();
        buttonList.add(button_1);
        buttonList.add(button_2);
        buttonList.add(button_3);
        buttonList.add(button_4);

        // bootstrap game
        resetGame();
        setupGameLoop();
        startGame();

        linearLayout=(LinearLayout)findViewById(R.id.adsLayouthard);
        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-6508526601344465/1373147235");
        adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        linearLayout.addView(adView);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void setColorsOnButtons() {
        int color  = Color.parseColor(BetterColor.getColor());
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int[] alphas = shuffledColors();

        for (int i = 0; i < alphas.length; i++) {
            Button button = buttonList.get(i);
            button.setBackgroundColor(Color.argb(alphas[i], red, green, blue));
        }
    }

    @Override
    protected void calculatePoints(View clickedView) {
        ColorDrawable clickedColor = (ColorDrawable) clickedView.getBackground();
        int clickedAlpha = Color.alpha(clickedColor.getColor());

        int lightestColor = clickedAlpha;
        for (Button button : buttonList) {
            ColorDrawable color = (ColorDrawable) button.getBackground();
            int alpha = Color.alpha(color.getColor());
            if (alpha < lightestColor) {
                lightestColor = alpha;
            }
        }

        // correct guess
        if (lightestColor == clickedAlpha) {
            updatePoints();
        } else {
            // false - hard mode
            endGame();
        }
    }

    @Override
    public void onClick(View view) {
        if (!gameStart) return;
        calculatePoints(view);
        setColorsOnButtons();
    }


    // Fisher Yates shuffling algorithm
    private int[] shuffledColors() {
        Random random = new Random();
        int[] arr = {255, 185, 155, 225 };
        for (int i = arr.length - 1; i >= 1; i--) {
            int j = random.nextInt(i);
            // swap i and j
            int tmp;
            tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        return arr;
    }
}
