package com.kyle.mycar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.View.ProgressButton;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView ivHead;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.et_acc)
    EditText etAcc;
    @BindView(R.id.til_account)
    TextInputLayout tilAccount;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.til_psw)
    TextInputLayout tilPsw;
    @BindView(R.id.pb_btn)
    ProgressButton pbBtn;
    @BindView(R.id.root)
    ConstraintLayout root;
    @BindView(R.id.tv_login_reg)
    TextView tvLoginReg;
    @BindView(R.id.tv_login_skip)
    TextView tvLoginSkip;
    @BindView(R.id.tv_forget_psw)
    TextView tvForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//            getWindow().setExitTransition(new Fade().setDuration(500));
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams
                    .FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                    .SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

        pbBtn.setBgColor(Color.RED);
        pbBtn.setTextColor(Color.WHITE);
        pbBtn.setProColor(Color.WHITE);
        pbBtn.setButtonText(getString(R.string.login));

        ivHead.post(new Runnable() {
            @Override
            public void run() {
                int width = ivHead.getWidth();
                int bottom = ivHead.getBottom();
                //中心点y坐标，7.5度角度
                bottom = bottom - (int) (Math.tan(Math.toRadians(7.5)) * width / 2);

                int ivWidth = (int) (width * 0.28);
                CircleImageView iv = new CircleImageView(LoginActivity.this);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ivWidth, ivWidth);
                layoutParams.setMargins((width - ivWidth) / 2, bottom - ivWidth / 2, 0, 0);
                iv.setBorderWidth(5);
                iv.setBorderColor(Color.WHITE);
                rl.addView(iv, layoutParams);

                Glide.with(LoginActivity.this).load(R.drawable.head).into(iv);
            }
        });

        tilPsw.setPasswordVisibilityToggleEnabled(true);
        tilAccount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean mobileNO = isMobileNO(s.toString());
                if (mobileNO) {
                    tilAccount.setErrorEnabled(false);
                } else {
                    tilAccount.setError(getResources().getString(R.string.err_account));
                }
            }
        });

        etPsw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    onViewClicked();
                    return true;
                }
                return false;
            }
        });


    }


    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、147(TD)、157(TD)、158、159、178、187、188
    联通：130、131、132、152、155、156、176、185、186
    电信：133、153、177、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        //"[1]"代表第1位为数字1，"[4578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }

    @OnClick(R.id.pb_btn)
    public void onViewClicked() {
        pbBtn.startAnim();

        //请求网络数据
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    EventBus.getDefault().post(new MessageEvent());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    @OnClick({R.id.tv_login_reg, R.id.tv_login_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_reg:
                TransitionSet set = new TransitionSet();
                set.addTransition(new Fade()).addTransition(new Scale(0.5f)).setInterpolator(new
                        LinearOutSlowInInterpolator()).excludeTarget(pbBtn,true);
                TransitionManager.beginDelayedTransition(root, set);
                tvLoginReg.setVisibility(View.GONE);
                tvLoginSkip.setVisibility(View.GONE);
                tvForget.setVisibility(View.GONE);

                pbBtn.setButtonText(getString(R.string.reg));
                break;
            case R.id.tv_login_skip:
                //跳转主页
                startActivity(new Intent(LoginActivity.this, MainActivity.class), ActivityOptionsCompat
                        .makeSceneTransitionAnimation(LoginActivity.this).toBundle());

                colseDelay();

                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent msg) {

        if (MyConstant.COLSE_LOGIN == msg.flag) finish();
        else {
            pbBtn.stopAnim(new ProgressButton.OnStopAnim() {
                @Override
                public void Stop() {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class), ActivityOptionsCompat
                            .makeSceneTransitionAnimation(LoginActivity.this).toBundle());

                    colseDelay();
                }
            });
        }
    }

    private void colseDelay() {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(500);
                EventBus.getDefault().post(new MessageEvent(MyConstant.COLSE_LOGIN));
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

}
