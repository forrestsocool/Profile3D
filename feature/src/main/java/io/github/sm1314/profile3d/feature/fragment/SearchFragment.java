package io.github.sm1314.profile3d.feature.fragment;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import io.github.sm1314.profile3d.feature.MainActivity;
import io.github.sm1314.profile3d.feature.MyApplication;
import io.github.sm1314.profile3d.feature.R;
import io.github.sm1314.profile3d.feature.adapter.EmployeeAdapter;
import io.github.sm1314.profile3d.feature.adapter.MySpinnerAdapter;
import io.github.sm1314.profile3d.feature.model.Employee;
import io.github.sm1314.profile3d.feature.model.EmployeeLetterComparator;
import io.github.sm1314.profile3d.feature.view.WaveSideBarView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends DialogFragment {
    MyApplication myApp;
    RecyclerView mRecyclerView;
    WaveSideBarView mSideBarView;
    TextView mNoResult;
    List<Employee> emplList;
    EmployeeAdapter adapter;
    MaterialSpinner.OnItemSelectedListener<String> onItemSelectedListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = ((MyApplication)getActivity().getApplication());
        emplList = new ArrayList<>();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //下拉选中回调
        onItemSelectedListener = new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                MaterialSpinner spArmyday = ((View) view.getParent()).findViewById(R.id.spinner_armyday);
                MaterialSpinner spDept = ((View) view.getParent()).findViewById(R.id.spinner_dept);
                MaterialSpinner spPosi = ((View) view.getParent()).findViewById(R.id.spinner_posi);
                MaterialSpinner spSex = ((View) view.getParent()).findViewById(R.id.spinner_sex);
                MaterialSpinner spLoc = ((View) view.getParent()).findViewById(R.id.spinner_location);
                String year = (String)spArmyday.getItems().get(spArmyday.getSelectedIndex());
                String dept = (String)spDept.getItems().get(spDept.getSelectedIndex());
                String posi = (String)spPosi.getItems().get(spPosi.getSelectedIndex());
                String sex = (String)spSex.getItems().get(spSex.getSelectedIndex());
                String location = (String)spLoc.getItems().get(spLoc.getSelectedIndex());

                emplList.clear();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.CHINA);
                for(Employee e: myApp.employeeList)
                {
                    if((year.trim().equals("") || sdf.format(e.getArmyday()).equals(year))
                        && (sex.trim().equals("") || sex.equals(e.getSex()))
                        && (location.trim().equals("") || e.getLocation().contains(location))
                        && (dept.trim().equals("") || dept.equals(e.getDepartment().getName()))
                        && (posi.trim().equals("") || posi.equals(e.getPosition().getName())))
                    {
                        emplList.add(e);
                    }
                }
                Collections.sort(emplList,new EmployeeLetterComparator());
                adapter = new EmployeeAdapter(getContext(), emplList);
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                updateIfNoResult(emplList);
            }
        };
    }

    @Override public void onStart() {
        super.onStart();
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
//        getDialog().getWindow().setLayout( dm.widthPixels, getDialog().getWindow().getAttributes().height );
    }

    public void updateIfNoResult(List<Employee> list)
    {
        if(list == null || list.size() == 0)
        {
            mNoResult.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mSideBarView.setVisibility(View.INVISIBLE);
        }
        else{
            mNoResult.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mSideBarView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //添加这一行,去掉title方便布局
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, true);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mSideBarView = (WaveSideBarView) rootView.findViewById(R.id.side_view);
        mNoResult = (TextView) rootView.findViewById(R.id.tv_noresult);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        mRecyclerView.addItemDecoration(decoration);

        //emplList = myApp.employeeList;
        emplList.addAll(myApp.employeeList);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //final List<Employee> list = ((MyApplication)getActivity().getApplication()).employeeList;
                Collections.sort( emplList, new EmployeeLetterComparator());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new EmployeeAdapter(getContext(), emplList);
                        mRecyclerView.setAdapter(adapter);
                        updateIfNoResult(emplList);
                    }
                });
            }
        }).start();

        mSideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = adapter.getLetterPosition(letter);

                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });

        rootView.findViewById(R.id.view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment.this.dismiss();
            }
        });

        //设置spinner选中回调
        //((MaterialSpinner)rootView.findViewById(R.id.spinner_amyday)).setOnItemSelectedListener(onItemSelectedListener);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,  myApp.deptList);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((MaterialSpinner) rootView.findViewById(R.id.spinner_armyday)).setAdapter(new MySpinnerAdapter(getContext(),myApp.yearList));//.setItems(myApp.yearList);
        ((MaterialSpinner) rootView.findViewById(R.id.spinner_dept)).setAdapter(new MySpinnerAdapter(getContext(),myApp.deptList));
        ((MaterialSpinner) rootView.findViewById(R.id.spinner_posi)).setAdapter(new MySpinnerAdapter(getContext(),myApp.posiList));
        ((MaterialSpinner) rootView.findViewById(R.id.spinner_sex)).setAdapter(new MySpinnerAdapter(getContext(),getSexOptions()));
        ((MaterialSpinner) rootView.findViewById(R.id.spinner_location)).setAdapter(new MySpinnerAdapter(getContext(),getLocOptions()));

        ((MaterialSpinner) rootView.findViewById(R.id.spinner_armyday)).setOnItemSelectedListener(onItemSelectedListener);
        ((MaterialSpinner) rootView.findViewById(R.id.spinner_dept)).setOnItemSelectedListener(onItemSelectedListener);
        ((MaterialSpinner) rootView.findViewById(R.id.spinner_posi)).setOnItemSelectedListener(onItemSelectedListener);
        ((MaterialSpinner) rootView.findViewById(R.id.spinner_sex)).setOnItemSelectedListener(onItemSelectedListener);
        ((MaterialSpinner) rootView.findViewById(R.id.spinner_location)).setOnItemSelectedListener(onItemSelectedListener);

        return rootView;
    }

    @NonNull
    private List<String> getSexOptions() {
        List<String> sexOptions = new ArrayList<String>();
        sexOptions.add(" ");
        sexOptions.add("男");
        sexOptions.add("女");
        return sexOptions;
    }

    @NonNull
    private List<String> getLocOptions() {
        List<String> locOptions = new ArrayList<String>();
        locOptions.add(" ");
        locOptions.add("北京");
        locOptions.add("天津");
        locOptions.add("河北");
        locOptions.add("山西");
        locOptions.add("内蒙古");
        locOptions.add("辽宁");
        locOptions.add("吉林");
        locOptions.add("黑龙江");
        locOptions.add("上海");
        locOptions.add("江苏");
        locOptions.add("浙江");
        locOptions.add("安徽");
        locOptions.add("福建");
        locOptions.add("江西");
        locOptions.add("山东");
        locOptions.add("河南");
        locOptions.add("湖北");
        locOptions.add("湖南");
        locOptions.add("广东");
        locOptions.add("广西");
        locOptions.add("海南");
        locOptions.add("重庆");
        locOptions.add("四川");
        locOptions.add("贵州");
        locOptions.add("云南");
        locOptions.add("西藏");
        locOptions.add("陕西");
        locOptions.add("甘肃");
        locOptions.add("青海");
        locOptions.add("宁夏");
        locOptions.add("新疆");
        locOptions.add("香港");
        locOptions.add("澳门");
        locOptions.add("台湾");
        return locOptions;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
