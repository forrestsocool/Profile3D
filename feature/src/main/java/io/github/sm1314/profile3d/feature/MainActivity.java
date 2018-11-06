package io.github.sm1314.profile3d.feature;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.github.sm1314.profile3d.feature.activity.BaseActivity;
import io.github.sm1314.profile3d.feature.adapter.TextTagsAdapter;
import io.github.sm1314.profile3d.feature.adapter.VectorTagsAdapter;
import io.github.sm1314.profile3d.feature.adapter.ViewTagsAdapter;
import io.github.sm1314.profile3d.feature.fragment.SearchFragment;
import io.github.sm1314.profile3d.feature.model.Employee;
import io.github.sm1314.tagcloudlib.view.TagCloudView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements SearchFragment.OnFragmentInteractionListener{
    private TagCloudView tagCloudView;
    private TextTagsAdapter textTagsAdapter;
    private ViewTagsAdapter viewTagsAdapter;
    private VectorTagsAdapter vectorTagsAdapter;
    private boolean isRefresh = false;
    private int showMode = 0; //0 随机 1 2 3 一二三营

    //menu相关
    Menu menuToolbar;
    MenuItem menuToolbarSearch;
    MenuItem menuToolbarRefresh;


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
    }

    @Override
    protected void initView() {
        super.initView();
        //设置彩色statusbar
        setStatusBar(R.color.colorPrimary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.logo2);

        //设置toolbar按钮
        toolbar.inflateMenu(R.menu.toolbar_menu);
        menuToolbar = toolbar.getMenu();
        menuToolbarSearch = menuToolbar.findItem(R.id.menuToolbarSearch);
        menuToolbarRefresh = menuToolbar.findItem(R.id.menuToolbarRefresh);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menuToolbarRefresh) {
                    //Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
                    if(!isRefresh) {
                        isRefresh = true;
                        showMode = 0;
                        uncheckBottomNavigationView();
                        initData();
                        Handler mHandler = new Handler();
                        mHandler.getLooper();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isRefresh = false;
                            }
                        },1000);
                    }
                }
                else if (id == R.id.menuToolbarSearch) {
                    // MyDialogFragment 为自定义的 DialogFragment -------------

                     SearchFragment searchFragment = new SearchFragment();
                     Bundle bundle = new Bundle();
                     bundle.putInt("Data01", 11111);
                     bundle.putInt("Data02", 22222);
                     bundle.putInt("Data03", 33333);
                     searchFragment.setArguments(bundle);
                     searchFragment.show(getSupportFragmentManager(),"searchFragment");
                }
                return false;
            }
        });

        final BottomNavigationView bnv = uncheckBottomNavigationView();
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setCheckable(true);
                item.setChecked(true);
                if(item.getItemId() == R.id.menu_yiying){
                    showMode = 1;
                }
                else if(item.getItemId() == R.id.menu_erying){
                    showMode = 2;
                }
                else if(item.getItemId() == R.id.menu_jishu){
                    showMode = 3;
                }
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshTagView();
                    }
                },200);
                return true;
            }
        });
        //bnv.setOn

        //设置背景图
        Uri imageUri = Uri.parse("res://io.github.sm1314/" + R.drawable.back);
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
        draweeView.setImageURI(imageUri);
    }

    @NonNull
    private BottomNavigationView uncheckBottomNavigationView() {
        BottomNavigationView bnv = (BottomNavigationView)findViewById(R.id.bottom_nav);
        bnv.getMenu().getItem(0).setCheckable(false);
        bnv.getMenu().getItem(1).setCheckable(false);
        bnv.getMenu().getItem(2).setCheckable(false);
        return bnv;
    }

    @Override
    protected void initData() {
        super.initData();

        findViewById(R.id.tv_noresult).setVisibility(View.INVISIBLE);
        findViewById(R.id.spin_kit).setVisibility(View.VISIBLE);
        findViewById(R.id.tag_cloud).setVisibility(View.INVISIBLE);
        //设置数据源
        OkHttpClient okHttpClient = new OkHttpClient();                                          //1.定义一个client
        Request request = new Request.Builder().url(MyApplication.EMPLOYEE_GET_URL).build();    //2.定义一个request
        Call call = okHttpClient.newCall(request);                                               //3.使用client去请求
        call.enqueue(new Callback() {                                                           //4.回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.tv_noresult).setVisibility(View.VISIBLE);
                        findViewById(R.id.spin_kit).setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();//5.获得网络数据
                Employee[] array = new Gson().fromJson(result,Employee[].class);
                ((MyApplication)getApplication()).employeeList = Arrays.asList(array);
                ((MyApplication)getApplication()).deptList.clear();
                ((MyApplication)getApplication()).posiList.clear();
                ((MyApplication)getApplication()).yearList.clear();
                for(Employee e: ((MyApplication)getApplication()).employeeList)
                {
                    if(!((MyApplication)getApplication()).deptList.contains(e.getDepartment().getName()))
                    {
                        ((MyApplication)getApplication()).deptList.add(e.getDepartment().getName());
                    }
                    if(!((MyApplication)getApplication()).posiList.contains(e.getPosition().getName()))
                    {
                        ((MyApplication)getApplication()).posiList.add(e.getPosition().getName());
                    }
                }
                Calendar cal = Calendar.getInstance();
                int currYear = cal.get(Calendar.YEAR);
                for(int y=currYear; y>=1949; y--)
                {
                    ((MyApplication)getApplication()).yearList.add(String.valueOf(y));
                }
                ((MyApplication)getApplication()).deptList.add(0,"");
                ((MyApplication)getApplication()).posiList.add(0,"");
                ((MyApplication)getApplication()).yearList.add(0,"");

                //System.out.println(result);

                refreshTagView();
            }
        });
    }


    private void refreshTagView(){
        viewTagsAdapter = new ViewTagsAdapter(((MyApplication)getApplication()).employeeList, showMode);
        tagCloudView = (TagCloudView) findViewById(R.id.tag_cloud);
        tagCloudView.setAdapter(viewTagsAdapter);
        viewTagsAdapter.notifyDataSetChanged();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(viewTagsAdapter.getCount() == 0){
                    findViewById(R.id.tv_noresult).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.tv_noresult).setVisibility(View.INVISIBLE);
                }
                findViewById(R.id.spin_kit).setVisibility(View.INVISIBLE);
                findViewById(R.id.tag_cloud).setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
