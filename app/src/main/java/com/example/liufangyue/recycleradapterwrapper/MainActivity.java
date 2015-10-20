package com.example.liufangyue.recycleradapterwrapper;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.liufangyue.recycleradapterwrapper.adapter.RecyclerAdapterWrapper;
import com.example.liufangyue.recycleradapterwrapper.bean.Person;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);
        adapter = new RecyclerAdapterWrapper<Person>(new ArrayList<Person>(), R.layout.recycler_item) {

            @Override
            public void initData(RecyclerAdapterWrapper.ViewHolderWrapper holderWrapper, Person person, int position) {
                TextView leftView = (TextView)holderWrapper.findViewById(R.id.left);
                TextView rightView = (TextView)holderWrapper.findViewById(R.id.right);
                leftView.setText(person.name);
                rightView.setText(person.phone);
            }
        };
        adapter.setHeadView(LayoutInflater.from(this).inflate(R.layout.head_view, recyclerView, false));
        //adapter.setFootView(LayoutInflater.from(this).inflate(R.layout.foot_view, recyclerView, false));
        adapter.setOnRecyclerViewClickListener(new RecyclerAdapterWrapper.OnRecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                adapter.remove(position);
                //adapter.insert(position, new Person("aaa", "bbb"));
            }
        });
        init();
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_red_light, android.R.color.holo_orange_light);
        refreshLayout.setOnRefreshListener(this);
    }
    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
                refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    private void init() {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            persons.add(new Person("xingming:" + i, "phone:" + i));
        }
        adapter.clear();
        adapter.addAll(persons);
        recyclerView.setAdapter(adapter);
    }

    private RecyclerView recyclerView;
    private RecyclerAdapterWrapper adapter;
    private SwipeRefreshLayout refreshLayout;
    private Handler mHandler = new Handler();
}
