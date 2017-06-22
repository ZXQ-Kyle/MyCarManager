package com.kyle.mycar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVSaveOption;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.bumptech.glide.Glide;
import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.Bean.UserInfo;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.MyUtils.SpUtils;
import com.kyle.mycar.View.ProgressButton;
import com.orhanobut.logger.Logger;
import com.transitionseverywhere.AutoTransition;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity implements Handler.Callback, View.OnClickListener {

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
    @BindView(R.id.reg_btn)
    Button regBtn;
    @BindView(R.id.reg_et)
    EditText regEt;

    private boolean ready;
    private int time;
    private Handler handler;

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
        pbBtn.setButtonText("");
        pbBtn.startAnim();
        final String acc = etAcc.getText().toString().trim();
        if (View.GONE == regBtn.getVisibility()) {
            //登录
            final String psw = etPsw.getText().toString().trim();
            if (!TextUtils.isEmpty(psw) && isMobileNO(acc)) {
                // TODO: 2017/6/22 修改
                String id = SpUtils.getString(this, MyConstant.USER_ID);
                if (TextUtils.isEmpty(id)) {
                    AVQuery<UserInfo> query = new AVQuery<>("UserInfo");
                    query.whereEqualTo("phone", acc);
                    query.getFirstInBackground(new GetCallback<UserInfo>() {
                        @Override
                        public void done(UserInfo userInfo, AVException e) {
                            if (null == e) {
                                SpUtils.putSring(LoginActivity.this, MyConstant.USER_ID, userInfo.getObjectId());
                                String phone = userInfo.getPhone();
                                String pass = userInfo.getPsw();
                                if (TextUtils.equals(phone, acc) && TextUtils.equals(pass, psw)) {
                                    stopAnimAndFinish();
                                }
                            } else {
                                com.alibaba.fastjson.JSONObject object = JSON.parseObject(e.getMessage());
                                String error = object.getString("error");

                                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                                pbBtn.initialize(getString(R.string.login));
                            }
                        }
                    });

                } else {
                    AVObject userInfo = AVObject.createWithoutData("UserInfo", id);
                    userInfo.fetchInBackground(new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            if (null == e) {
                                Logger.d(avObject);
                                String phone = avObject.getString("phone");
                                String pass = avObject.getString("psw");
                                if (TextUtils.equals(phone, acc) && TextUtils.equals(pass, psw)) {
                                    stopAnimAndFinish();
                                }else {
                                    Toast.makeText(LoginActivity.this, R.string.err_psw, Toast.LENGTH_SHORT).show();
                                    pbBtn.initialize(getString(R.string.login));
                                }
                            } else {
                                com.alibaba.fastjson.JSONObject object = JSON.parseObject(e.getMessage());
                                String error = object.getString("error");

                                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                                pbBtn.initialize(getString(R.string.login));
                            }
                        }
                    });
                    Logger.d(userInfo);
                }

            } else {
                Toast.makeText(LoginActivity.this, R.string.err_empty_login, Toast.LENGTH_SHORT).show();
                pbBtn.initialize(getString(R.string.login));
            }

        } else {
            //注册
            SMSSDK.submitVerificationCode("86", acc, regEt.getText().toString().trim());
        }
    }

    @OnClick({R.id.tv_login_reg, R.id.tv_login_skip, R.id.tv_forget_psw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_reg:
                TransitionSet set = new TransitionSet();
                set.addTransition(new AutoTransition().addTarget(pbBtn).setDuration(500));
                set.addTransition(new Fade().excludeTarget(pbBtn, true)).addTransition(new Scale(0.5f).excludeTarget
                        (pbBtn, true)).setInterpolator(new LinearOutSlowInInterpolator());
                TransitionManager.beginDelayedTransition(root, set);
                tvLoginReg.setVisibility(View.GONE);
                tvLoginSkip.setVisibility(View.GONE);
                tvForget.setVisibility(View.GONE);
                regBtn.setVisibility(View.VISIBLE);
                regEt.setVisibility(View.VISIBLE);
                regBtn.setOnClickListener(this);
                pbBtn.setButtonText(getString(R.string.reg));
                initSms();
                break;

            case R.id.tv_login_skip:
                //跳转主页
                startActivity(new Intent(LoginActivity.this, MainActivity.class), ActivityOptionsCompat
                        .makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                colseDelay();
                break;

            case R.id.tv_forget_psw:
                startActivity(new Intent(LoginActivity.this, ForgetActivity.class), ActivityOptionsCompat
                        .makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                break;

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent msg) {

        if (MyConstant.COLSE_LOGIN == msg.flag) finish();
        else {
            stopAnimAndFinish();
        }
    }

    private void stopAnimAndFinish() {
        tvLoginSkip.setVisibility(View.INVISIBLE);
        tvLoginReg.setVisibility(View.INVISIBLE);
        tvForget.setVisibility(View.INVISIBLE);
        pbBtn.stopAnim(new ProgressButton.OnStopAnim() {
            @Override
            public void Stop() {
                startActivity(new Intent(LoginActivity.this, MainActivity.class), ActivityOptionsCompat
                        .makeSceneTransitionAnimation(LoginActivity.this).toBundle());

                colseDelay();
            }
        });
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        registerSDK();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        if (ready) {
            // 销毁回调监听接口
            SMSSDK.unregisterAllEventHandler();
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }


    private void initSms() {
        if (Build.VERSION.SDK_INT >= 23) {
            int readPhone = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            int receiveSms = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
            int readSms = checkSelfPermission(Manifest.permission.READ_SMS);
//            int readContacts = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            int readSdcard = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            int requestCode = 0;
            ArrayList<String> permissions = new ArrayList<String>();
            if (readPhone != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 0;
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (receiveSms != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 1;
                permissions.add(Manifest.permission.RECEIVE_SMS);
            }
            if (readSms != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 2;
                permissions.add(Manifest.permission.READ_SMS);
            }
//            if (readContacts != PackageManager.PERMISSION_GRANTED) {
//                requestCode |= 1 << 3;
//                permissions.add(Manifest.permission.READ_CONTACTS);
//            }
            if (readSdcard != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 3;
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (requestCode > 0) {
                String[] permission = new String[permissions.size()];
                this.requestPermissions(permissions.toArray(permission), requestCode);
                return;
            }
        }
        registerSDK();
    }

    private void registerSDK() {

        handler = new Handler(this);
        EventHandler eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg = Message.obtain();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
        ready = true;
    }

    @Override
    public boolean handleMessage(Message msg) {

        int event = msg.arg1;
        int result = msg.arg2;
        Object data = msg.obj;

        if (result == SMSSDK.RESULT_COMPLETE) {
            //回调完成
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                //提交验证码成功
                String phone = (String) ((HashMap) data).get("phone");
                //提交用户，注册
                final UserInfo userInfo = new UserInfo();
                String psw = etPsw.getText().toString().trim();
                userInfo.setPsw(psw);
                userInfo.setPhone(phone);
                AVQuery<UserInfo> query = new AVQuery("UserInfo").whereContains("phone", phone);
                query.countInBackground(new CountCallback() {
                    @Override
                    public void done(int i, AVException e) {
                        if (i == 0) {
                            userInfo.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (null == e) {
                                        // 注册成功
                                        Toast.makeText(LoginActivity.this, R.string.reg_sucess, Toast.LENGTH_SHORT)
                                                .show();
                                        SpUtils.putSring(LoginActivity.this, MyConstant.USER_ID, userInfo.getObjectId
                                                ());
                                        stopAnimAndFinish();
                                    } else {
                                        // 失败的原因可能有多种，常见的是用户名已经存在。
                                        com.alibaba.fastjson.JSONObject object = JSON.parseObject(e.getMessage());
                                        String error = object.getString("error");
                                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                                        pbBtn.initialize(getString(R.string.reg));
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "用户已存在，请登录", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                //获取验证码成功
            } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                //返回支持发送验证码的国家列表
            } else if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
                //提交用户成功
            }
        } else {
            pbBtn.initialize(getString(R.string.reg));
            Throwable throwable = (Throwable) data;
            throwable.printStackTrace();
            com.alibaba.fastjson.JSONObject object = JSON.parseObject(throwable.getMessage());
            String des = object.getString("detail");//错误描述
            int status = object.getInteger("status");//错误代码
            if (status > 0 && !TextUtils.isEmpty(des)) {
                Toast.makeText(this, des, Toast.LENGTH_LONG).show();
                return true;
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reg_btn) {
            String acc = etAcc.getText().toString().trim();
            if (isMobileNO(acc)) {
                v.setClickable(false);
                regBtn.setTextColor(getResources().getColor(R.color.LineGray));
                time = 60;

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        time--;
                        if (0 != time) {
                            regBtn.setText(time + getString(R.string.time));
                            handler.postDelayed(this, 1000);
                        } else {
                            regBtn.setText(R.string.get_code);
                            regBtn.setTextColor(getResources().getColor(R.color.TextGray));
                            regBtn.setClickable(true);
                        }
                    }
                }, 1000);

                SMSSDK.getVerificationCode("86", acc);
            } else {
                Toast.makeText(LoginActivity.this, R.string.err_account, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
