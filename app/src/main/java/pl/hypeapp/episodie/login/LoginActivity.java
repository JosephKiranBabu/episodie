package pl.hypeapp.episodie.login;

import android.animation.Animator;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.hypeapp.episodie.App;
import pl.hypeapp.episodie.R;
import pl.hypeapp.episodie.base.BaseMVPActivity;
import pl.hypeapp.episodie.signup.SignUpActivity;
import pl.hypeapp.episodie.util.BuildUtil;
import pl.hypeapp.episodie.util.StartActivityUtil;
import pl.hypeapp.episodie.util.image.BlurTransformation;
import pl.hypeapp.episodie.util.image.ColorFilterTransformation;
import pl.hypeapp.episodie.util.image.GrayscaleTransformation;


public class LoginActivity extends
        BaseMVPActivity<LoginMVP.RequiredViewOps, LoginMVP.ProvidedPresenterOps, LoginPresenter>
        implements LoginMVP.RequiredViewOps {

    @BindView(R.id.iv_login_background)
    ImageView loginBackgroundImageView;
    @BindView(R.id.rl_logo)
    ViewGroup logoLayout;
    @BindView(R.id.iv_logo)
    ImageView logoIcon;
    @BindView(R.id.tv_logo)
    TextView logoText;
    @BindView(R.id.center)
    Space space;
    @BindView(R.id.iv_shared_background)
    ImageView sharedBackground;
    @Inject
    FirebaseAuth firebaseAuth;
    ImageView rootLayout;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        super.onCreate(LoginPresenter.class, this);

        rootLayout = loginBackgroundImageView;
        loadImageFromResourcesIntoView(loginBackgroundImageView, R.drawable.breaking_bad_background,
                new GrayscaleTransformation(this), new BlurTransformation(this, 12));
        setTextLogoFont(logoText);
        enterActivityLogoTransition();

        app = (App) getApplication();
        app.getAuthComponent().inject(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void enterActivityLogoTransition() {
        logoIcon.postDelayed(logoTransitionRun(logoIcon, logoText, logoLayout), 680);
    }

    private Runnable logoTransitionRun(final View logoIcon, final View logoText, final ViewGroup logoLayout) {
        return new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(logoLayout);

                RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) logoIcon.getLayoutParams();
                rl.addRule(RelativeLayout.LEFT_OF, space.getId());
                logoIcon.setLayoutParams(rl);

                logoText.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInRight).playOn(logoText);
            }
        };
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void onCompleteLogin() {
    }

//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        super.onTouchEvent(motionEvent);
//        Log.e("logonTouch", "log1");
//        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//            Log.e("logonTouch", "log1");
//            if (view.getId() == R.id.login_button) {
//                Log.e("logonTouch", "log");
//                revealFromCoordinates((int)motionEvent.getRawX(), (int)motionEvent.getRawY());
//            }
//        }
//        return false;
//    }
//    private void animateRevealShow(View viewRoot) {
//        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
//        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
//        int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());
//
//        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
//        viewRoot.setVisibility(View.VISIBLE);
//        anim.setDuration(1000);
//        anim.setInterpolator(new AccelerateInterpolator());
//        anim.start();
//    }
//
//    @OnClick(R.id.login_button)
//    void touch(){
////        revealFromCoordinates(loginBackgroundImageView);
//    }

    private Animator revealFromCoordinates(View viewRoot) {
        Animator anim = null;
        long duration = getResources().getInteger(R.integer.anim_duration_long);
        if (BuildUtil.isMinApi21()) {
            int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
            int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
            int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());
            int colorFilter = Color.argb(35, 56, 147, 35);

            anim = ViewAnimationUtils.createCircularReveal(sharedBackground, cx, cy, 0, finalRadius);
            loadSharedBackground(colorFilter);
            sharedBackground.setVisibility(View.VISIBLE);
            anim.setDuration(duration);
            anim.setInterpolator(new AccelerateInterpolator());
            anim.addListener(createRevealListener());
            anim.start();
        }
        return anim;
    }

    private Animator.AnimatorListener createRevealListener() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                sharedBackground.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };
    }

    private void intentToSignUpActivity() {
        View sharedView = loginBackgroundImageView;
        Pair<View, String> sharedBackground = Pair.create(sharedView, "background");
        StartActivityUtil startActivityUtil = StartActivityUtil.getInstance(getActivity());
        startActivityUtil.runActivityWithTransition(SignUpActivity.class, sharedBackground);
    }

    private void loadSharedBackground(int colorFilter) {
        Glide.with(this).load(Uri.parse("file:///android_asset/thewalkingdead_background.jpg"))
                .bitmapTransform(new ColorFilterTransformation(this, colorFilter), new BlurTransformation(this, 12))
                .into(sharedBackground);
    }

    @OnClick(R.id.login_button)
    void onSignUpStart() {
        intentToSignUpActivity();
    }

}
