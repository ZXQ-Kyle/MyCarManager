package com.kyle.mycar.View;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.kyle.mycar.MyUtils.Tint;
import com.kyle.mycar.R;

/**
 * 自定义控件
 * Created by Zhang on 2017/5/4.
 */

public class ImgAndEtView extends LinearLayoutCompat {

    private ImageView iv;
    private EditText et;
    private boolean interceptTouchEvent = false;

    public ImgAndEtView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //取得属性集合
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImgAndEtView);
        ColorStateList colorStateList = typedArray.getColorStateList(R.styleable.ImgAndEtView_drawableColor);
        Drawable drawable = typedArray.getDrawable(R.styleable.ImgAndEtView_iconLeft);
        String hint = typedArray.getString(R.styleable.ImgAndEtView_hint);
        //关闭资源
        typedArray.recycle();
        View view = View.inflate(context, R.layout.img_and_et_view, this);

        iv = (ImageView) findViewById(R.id.iv_my);
        et = (EditText) findViewById(R.id.et_my);

        setDrawableColor(drawable, colorStateList);

        et.setBackground(Tint.tintDrawable(et.getBackground(), colorStateList));
        et.setMaxLines(1);
        et.setHint(hint);
    }

    /**设置imageview的图标颜色
     * @param drawable 设置图标
     * @param color 设置颜色
     */
    public void setDrawableColor(Drawable drawable, ColorStateList color) {
        iv.setImageDrawable(Tint.tintDrawable(drawable, color).mutate());

    }
    public void setText(String s){
        et.setText(s);
    }
    public String getText() {
        return et.getText().toString().trim();
    }
//  设置editText不可编辑
    public void setUnEditable(){
        et.setKeyListener(null);
        interceptTouchEvent=true;
    }

    public void setInputTypeOfNumber(){
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
    }





    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
       if (!interceptTouchEvent){
           return super.onInterceptTouchEvent(ev);
       }else {
           return true;
       }
    }


//    @Override
//    public void setOnClickListener(@Nullable OnClickListener l) {
//        et.setOnClickListener(l);
////        super.setOnClickListener(l);
//    }

}
