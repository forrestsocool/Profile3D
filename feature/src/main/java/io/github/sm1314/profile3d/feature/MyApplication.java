package io.github.sm1314.profile3d.feature;

import android.app.Application;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.github.sm1314.profile3d.feature.model.Employee;


public class MyApplication extends Application {
    public List<Employee> employeeList;
    public List<String> deptList;
    public List<String> posiList;
    public List<String> yearList;
//    public static String BASE_URL = "http://23.25.4.84:8080/api";
//    public static String EMPLOYEE_GET_URL = "http://23.25.4.84:8080/api/empl";
//    public static String BASE_URL = "http://192.168.1.8:8080/api";
//    public static String EMPLOYEE_GET_URL = "http://192.168.1.8:8080/api/empl";
    public static String BASE_URL = "http://172.96.241.34:8080/api";
    public static String EMPLOYEE_GET_URL = "http://172.96.241.34:8080/api/empl";
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        employeeList = new ArrayList<>();
        deptList = new LinkedList<>();
        posiList = new LinkedList<>();
        yearList = new LinkedList<>();
    }
}
