package alvi17.colormaniac;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import alvi17.colormaniac.util.BetterColor;

public class EasyGameActivity extends MainGameActivity {

    private Button topBtn, bottomBtn;

    AdView adView;
    AdRequest adRequest;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_game);
        setupProgressView();

        POINT_INCREMENT = 2;
        TIMER_BUMP = 2;

        gameMode = GameMode.EASY;

        topBtn = (Button) findViewById(R.id.top_button);
        bottomBtn = (Button) findViewById(R.id.bottom_button);
        topBtn.setOnClickListener(this);
        bottomBtn.setOnClickListener(this);

        // bootstrap game
        resetGame();
        setupGameLoop();
        startGame();

        linearLayout=(LinearLayout)findViewById(R.id.adsLayouteasy);
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
    public void onClick(View view) {
        if (!gameStart) return;
        calculatePoints(view);
        setColorsOnButtons();
    }

    protected void setColorsOnButtons() {
        String c=BetterColor.getColor();
        Log.e("Color",c);
        int color = Color.parseColor(c);

        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        int alpha1, alpha2;
        if (Math.random() > 0.5) {
            alpha1 = 255;
            alpha2 = 185;
        } else {
            alpha1 = 185;
            alpha2 = 255;
        }

        topBtn.setBackgroundColor(Color.argb(alpha1, red, green, blue));
        bottomBtn.setBackgroundColor(Color.argb(alpha2, red, green, blue));
    }

    protected void calculatePoints(View clickedView) {
        View unclickedView = clickedView == topBtn ? bottomBtn : topBtn;
        ColorDrawable clickedColor = (ColorDrawable) clickedView.getBackground();
        ColorDrawable unClickedColor = (ColorDrawable) unclickedView.getBackground();

        int alpha1 = Color.alpha(clickedColor.getColor());
        int alpha2 = Color.alpha(unClickedColor.getColor());

        // correct guess
        if (alpha1 < alpha2) {
            updatePoints();
        } else { // incorrect guess
            endGame();
        }
    }

}
