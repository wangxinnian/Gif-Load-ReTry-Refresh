package com.wxystatic.gifloadretryrefresh.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruffian.library.RTextView;
import com.wstatic.toolandstatusbarlibrary.ToolAndStatusBarMagager;
import com.wxystatic.gifloadretryrefresh.R;
import com.wxystatic.loadretrylibrary.LoadReTryRefreshManager;
import com.wxystatic.loadretrylibrary.LoadRetryRefreshListener;
import com.wxystatic.loadretrylibrary.ShowRefreshViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SuccessActivity extends AppCompatActivity  {

    @BindView(R.id.loadretry_tv_retry_success)
    RTextView loadretryTvRetrySuccess;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.linear_back)
    LinearLayout linearBack;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_gf);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ToolAndStatusBarMagager.getInstance().setStatusBarColor(this,R.color.color_toolbar);
        LoadReTryRefreshManager.getInstance().register(this, new LoadRetryRefreshListener() {
            @Override
            public void loadAndRetry() {
                doSomething();
            }

            @Override
            public void showRefreshView() {
                refreshLayout.setRefreshing(true);
            }
        });
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doSomething();
            }
        });
    }


    public void  doSomething() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Thread.sleep(2000);

                emitter.onNext(1);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                Toast.makeText(SuccessActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                tvContent.setText(getResources().getString(R.string.large_text));
                LoadReTryRefreshManager.getInstance().onLoadSuccess(SuccessActivity.this, new ShowRefreshViewListener() {
                    @Override
                    public void colseRefreshView() {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });


    }


    /**
     * 模拟请求成功
     */
    @OnClick(R.id.loadretry_tv_retry_success)
    public void onLoadretryTvRetrySuccessClicked() {
        LoadReTryRefreshManager.getInstance().startLoad(this);
    }


    @OnClick({R.id.linear_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        LoadReTryRefreshManager.getInstance().unRegister(this);
        super.onDestroy();
    }
}
