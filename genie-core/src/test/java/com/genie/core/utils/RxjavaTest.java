package com.genie.core.utils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RxjavaTest {
    private final Logger Log = LoggerFactory.getLogger(RxjavaTest.class);

    @Test
    public void test(){
        Observable.create(new ObservableOnSubscribe<Integer>() { // 第一步：初始化Observable
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                Log.info("Observable emit 1");
                e.onNext(1);
                Log.info("Observable emit 2");
                e.onNext(2);
                Log.info("Observable emit 3");
                e.onNext(3);
                e.onComplete();
                Log.info("Observable emit 4" );
                e.onNext(4);
            }
        }).subscribe(new Observer<Integer>() { // 第三步：订阅

            // 第二步：初始化Observer
            private int i;
            private Disposable mDisposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.info("onSubscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                i++;
                Log.info("onNext integer: {}, i: {}", integer, i);
                if (i == 2) {
                    // 在RxJava 2.x 中，新增的Disposable可以做到切断的操作，让Observer观察者不再接收上游事件
                    mDisposable.dispose();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.info("onError : value : " + e.getMessage() );
            }

            @Override
            public void onComplete() {
                Log.info("onComplete" );
            }
        });

    }
}
