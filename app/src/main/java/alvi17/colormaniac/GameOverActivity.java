package alvi17.colormaniac;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class GameOverActivity extends Activity {

    private int points, best, level;
    private boolean newScore;
    private boolean shown = false;
    private TextView gameOverText, pointsBox, highScoreText;
    private SharedPreferences sharedPreferences;
    private MainGameActivity.GameMode mode;

    final int REQUEST_LEADERBOARD = 4000;
    final int REQUEST_ACHIEVEMENTS = 5000;

    InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        gameOverText = (TextView) findViewById(R.id.game_over);
        TextView levelIndicator = (TextView) findViewById(R.id.level_indicator);
        pointsBox = (TextView) findViewById(R.id.points_box);
        TextView bestLabel = (TextView) findViewById(R.id.best_label);
        TextView bestBox = (TextView) findViewById(R.id.best_box);
        highScoreText = (TextView) findViewById(R.id.highscore_txt);
        Button replayBtn = (Button) findViewById(R.id.replay_btn);

        // setting up typeface
        Typeface avenir_black = Typeface.createFromAsset(getAssets(), "fonts/avenir_black.ttf");
        Typeface avenir_book = Typeface.createFromAsset(getAssets(), "fonts/avenir_book.ttf");

        gameOverText.setTypeface(avenir_black);
        levelIndicator.setTypeface(avenir_book);
        pointsBox.setTypeface(avenir_black);
        bestBox.setTypeface(avenir_black);
        bestLabel.setTypeface(avenir_book);
        replayBtn.setTypeface(avenir_book);
        highScoreText.setTypeface(avenir_black);


        // set a simple game counter in shared pref
        sharedPreferences = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        int timesPlayed = sharedPreferences.getInt("TIMESPLAYED", 0);
        editor.putInt("TIMESPLAYED", timesPlayed + 1);
        editor.apply();

        // get data
        Bundle bundle = getIntent().getExtras();
        points = bundle.getInt("points");
        level = bundle.getInt("level");
        best = bundle.getInt("best");
        newScore = bundle.getBoolean("newScore");
        mode =  MainGameActivity.GameMode.valueOf(bundle.getString("gameMode"));

        // set data
        pointsBox.setText(String.format("%03d", points));
        bestBox.setText(String.format("%03d", best));
        levelIndicator.setText("Level " + Integer.toString(level));

        // show high score
        if (newScore) {
            highScoreText.setVisibility(View.VISIBLE);
        } else {
            highScoreText.setVisibility(View.INVISIBLE);
        }


        interstitial=new  InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-6508526601344465/2849880436");
        AdRequest aRequest = new AdRequest.Builder().build();

        // Begin loading your interstitial.
        interstitial.loadAd(aRequest);

        interstitial.setAdListener(
                new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        interstitial.show();
                    }
                }
        );
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus && !shown) {
            shown = true;
            ValueAnimator pointsAnim = getCounterAnimator(pointsBox, points);
            pointsAnim.setDuration(1200);

            // animate high score text
            if (newScore) {
                ObjectAnimator highScoreAnim = ObjectAnimator.ofFloat(highScoreText, "alpha", 0f, 1f);
                highScoreAnim.setDuration(600);
                highScoreAnim.start();
            }
            pointsAnim.start();
        }
    }

    ValueAnimator getCounterAnimator(final TextView view, final int maxValue) {
        ValueAnimator anim = ValueAnimator.ofInt(0, 1);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (int) (maxValue * valueAnimator.getAnimatedFraction());
                view.setText(String.format("%03d", val));
            }
        });
        return anim;
    }

    public void playGame(View view) {
        if(Util.getInfo(getApplicationContext(),"mode").equals("hard")) {
            //startActivity(new Intent(this, EasyGameActivity.class));
            startActivity(new Intent(this, HardGameActivity.class));
        }
        else {
            startActivity(new Intent(this, EasyGameActivity.class));
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeScreenActivity.class));
        finish();
    }

    // save high score in shared preferences file
    private void updateHighScore(int score) {
        if (score != best && score > 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("HIGHSCORE", score);
            editor.apply();
        }
    }
}
