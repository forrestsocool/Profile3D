package io.github.sm1314.profile3d.feature.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/5/4.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initView();
        initConfig();
        initData();
        initEvent();
    }
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initView();
        initConfig();
        initData();
        initEvent();
    }

    protected void initView(){
        quitFullScreen();
        setFullSreen(this);
    }

    //初始化适配相关
    protected void initConfig(){

    }
    protected void initData(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void initEvent(){

    }

    public static  void setFullSreen(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置透明状态栏,这样才能让 ContentView 向上
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void quitFullScreen(){
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 生成一个和状态栏大小相同的矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    public static View createStatusView(Activity activity, int color) {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);
        return statusView;
    }

    public void setStatusBar(int colorID) {
        // 设置状态栏透明
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 生成一个状态栏大小的矩形
        View statusView = createStatusView(this, getResources().getColor(colorID));
        // 添加 statusView 到布局中
        ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
        decorView.addView(statusView);
        // 设置根布局的参数
        ViewGroup rootView = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }
}
