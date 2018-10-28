package io.github.sm1314.profile3d.feature;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.github.sm1314.profile3d.feature.model.Employee;

public class MyApplication extends Application {
    public List<Employee> employeeList;
    public List<String> deptList;
    public List<String> posiList;
    public static String BASE_URL = "http://192.168.1.8/api";
    public static String EMPLOYEE_GET_URL = "http://192.168.1.8/api/empl";
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        employeeList = new ArrayList<>();
        deptList = new LinkedList<>();
        posiList = new LinkedList<>();
    }
}
