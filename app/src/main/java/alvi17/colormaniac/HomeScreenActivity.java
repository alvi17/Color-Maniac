package alvi17.colormaniac;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeScreenActivity extends
        Activity implements View.OnClickListener {

    private Button playGameButton;
    private View signInButton, signOutButton;
    private ImageView logoView;
    private TextView taglineTextView1, taglineTextView2, taglineTextView3;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        taglineTextView1 = (TextView) findViewById(R.id.tagline_text);
        taglineTextView2 = (TextView) findViewById(R.id.tagline_text2);
        taglineTextView3 = (TextView) findViewById(R.id.tagline_text3);
        playGameButton = (Button) findViewById(R.id.play_game_btn);
        logoView = (ImageView) findViewById(R.id.logo);

        // setting the typeface
        Typeface avenir_book = Typeface.createFromAsset(getAssets(), "fonts/avenir_book.ttf");
        taglineTextView1.setTypeface(avenir_book);
        taglineTextView2.setTypeface(avenir_book);
        taglineTextView3.setTypeface(avenir_book);


    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            taglineTextView1.setVisibility(View.INVISIBLE);
            taglineTextView2.setVisibility(View.INVISIBLE);
            taglineTextView3.setVisibility(View.INVISIBLE);

            ValueAnimator bounceAnim = getBounceAnimator();
            ValueAnimator fadeAnim = getFadeAnimator();
            bounceAnim.setDuration(800);
            fadeAnim.setDuration(1000);

            bounceAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    taglineTextView1.setVisibility(View.VISIBLE);
                    taglineTextView2.setVisibility(View.VISIBLE);
                    taglineTextView3.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            AnimatorSet homeAnimation = new AnimatorSet();
            homeAnimation.playSequentially(bounceAnim, fadeAnim);
            homeAnimation.start();
        }
    }

    ValueAnimator getFadeAnimator() {
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                taglineTextView1.setAlpha(valueAnimator.getAnimatedFraction());
                taglineTextView2.setAlpha(valueAnimator.getAnimatedFraction());
                taglineTextView3.setAlpha(valueAnimator.getAnimatedFraction());
            }
        });
        return anim;
    }

    ValueAnimator getBounceAnimator() {
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setInterpolator(new BounceInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                logoView.setScaleX(valueAnimator.getAnimatedFraction());
                logoView.setScaleY(valueAnimator.getAnimatedFraction());
                logoView.setAlpha(valueAnimator.getAnimatedFraction());

                playGameButton.setScaleX(valueAnimator.getAnimatedFraction());
                playGameButton.setScaleY(valueAnimator.getAnimatedFraction());
                playGameButton.setAlpha(valueAnimator.getAnimatedFraction());
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
        finishAffinity();
    }



    @Override
    public void onClick(View view) {

    }


    public void showSettings(View view) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.settings_dialog);

        final CheckBox easycheckBox,hardcheckBox,timedCheckbox,untimeCheckbox;
        easycheckBox=(CheckBox)dialog.findViewById(R.id.checkBoxeasy);
        hardcheckBox=(CheckBox)dialog.findViewById(R.id.checkBoxhard);
        if(Util.getInfo(getApplicationContext(),"unlocked").equals("yes"))
        {
            hardcheckBox.setClickable(true);
            hardcheckBox.setEnabled(true);
        }



        hardcheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Util.getInfo(getApplicationContext(),"unlocked").equals("yes"))
                {
                    Toast.makeText(getApplicationContext(),"You nedd to make it to at least level 6 in Easy mode to unlock Hard mode",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


        if(Util.getInfo(getApplicationContext(),"mode").equals("hard"))
        {
            hardcheckBox.setChecked(true);
            easycheckBox.setChecked(false);
        }else
        {
            hardcheckBox.setChecked(false);
            easycheckBox.setChecked(true);
        }
        hardcheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(!b) {

                        easycheckBox.setChecked(true);
                        hardcheckBox.setChecked(false);
                        Util.saveInfo(getApplicationContext(), "mode", "easy");

                }
                else {
                        easycheckBox.setChecked(false);
                        hardcheckBox.setChecked(true);
                        Util.saveInfo(getApplicationContext(), "mode", "hard");
                }
            }
        });


        easycheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b)
                {
                    if(!Util.getInfo(getApplicationContext(),"unlocked").equals("yes"))
                    {
                        Toast.makeText(getApplicationContext(),"You nedd to make it to at least level 6 in Easy mode to unlock Hard mode",
                                Toast.LENGTH_LONG).show();
                        easycheckBox.setChecked(true);
                        hardcheckBox.setChecked(false);
                    }
                    else
                    {
                        easycheckBox.setChecked(false);
                        hardcheckBox.setChecked(true);
                        Util.saveInfo(getApplicationContext(),"mode","hard");
                    }
                }
                else
                {
                    easycheckBox.setChecked(true);
                    hardcheckBox.setChecked(false);
                    Util.saveInfo(getApplicationContext(),"mode","easy");
                }
            }
        });

        timedCheckbox=(CheckBox)dialog.findViewById(R.id.checkBoxtimed);
        untimeCheckbox=(CheckBox)dialog.findViewById(R.id.checkuntimed);


        if(Util.getInfo(getApplicationContext(),"time").equals("yes"))
        {
            timedCheckbox.setChecked(true);
            untimeCheckbox.setChecked(false);
        }
        else if(Util.getInfo(getApplicationContext(),"time").equals("no"))
        {
            timedCheckbox.setChecked(false);
            untimeCheckbox.setChecked(true);
        }

        timedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b)
                {
                    untimeCheckbox.setChecked(true);
                    timedCheckbox.setChecked(false);
                    Util.saveInfo(getApplicationContext(),"time","no");
                }
                else
                {
                    untimeCheckbox.setChecked(false);
                    timedCheckbox.setChecked(true);
                    Util.saveInfo(getApplicationContext(),"time","yes");
                }
            }
        });

        untimeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b)
                {
                    untimeCheckbox.setChecked(false);
                    timedCheckbox.setChecked(true);
                    Util.saveInfo(getApplicationContext(),"time","yes");
                }
                else
                {
                    untimeCheckbox.setChecked(true);
                    timedCheckbox.setChecked(false);
                    Util.saveInfo(getApplicationContext(),"time","no");
                }
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
    }


}
