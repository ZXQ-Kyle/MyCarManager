package com.kyle.mycar.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.kyle.mycar.LoginActivity;
import com.kyle.mycar.R;
import com.labo.kaji.fragmentanimations.CubeAnimation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPswFragment extends Fragment{


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_phone_forget)
    EditText etPhoneForget;
    @BindView(R.id.et_code_forget)
    EditText etCodeForget;
    @BindView(R.id.reg_btn)
    Button regBtn;
    @BindView(R.id.btn_next)
    Button btnNext;
    Unbinder unbinder;

    private int time;
    public Handler handler;
    private Runnable runnable;
    //    public EventHandler eventHandler;

    public ForgetPswFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_psw, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setTitle("重置密码");
        return view;
    }



    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return CubeAnimation.create(CubeAnimation.LEFT,enter, 500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null!=runnable){
            handler.removeCallbacks(runnable);
        }
        unbinder.unbind();
    }

    @OnClick({R.id.reg_btn,R.id.btn_next})
    public void onViewClicked(View view) {
        int id = view.getId();
        String acc =null;
        switch (id){
            case R.id.reg_btn:
                acc = etPhoneForget.getText().toString().trim();
                if (LoginActivity.isMobileNO(acc)) {
                    regBtn.setClickable(false);
                    regBtn.setTextColor(getResources().getColor(R.color.LineGray));
                    time = 60;

                    runnable = new Runnable() {
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
                    };
                    handler.postDelayed(runnable, 1000);

                    SMSSDK.getVerificationCode("86", acc);
                } else {
                    Toast.makeText(getActivity(), R.string.err_account, Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.btn_next:
                acc = etPhoneForget.getText().toString().trim();
                if (LoginActivity.isMobileNO(acc)){
                    SMSSDK.submitVerificationCode("86", acc,etCodeForget.getText().toString().trim());
                }else {
                    Toast.makeText(getActivity(), R.string.err_account, Toast.LENGTH_SHORT).show();
                }

                break;
        }


    }

}
