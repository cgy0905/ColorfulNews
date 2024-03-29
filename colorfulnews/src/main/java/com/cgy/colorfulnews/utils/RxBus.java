package com.cgy.colorfulnews.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 13:34
 */
public class RxBus {

    private static volatile RxBus sRxBus;
    //主题
    private final Subject<Object, Object> mBus;

    //PublishSubject只会把在订阅发生时间点之后来自原始Observable的数据发射给观察者
    public RxBus(){
        mBus = new SerializedSubject<>(PublishSubject.create());
    }

    //单例RxBus
    public static RxBus getInstance() {
        if (sRxBus == null) {
            synchronized (RxBus.class) {
                if (sRxBus == null) {
                    sRxBus = new RxBus();
                }
            }
        }
        return sRxBus;
    }

    //提供一个新的事件
    public void post(Object o) {mBus.onNext(o);}

    //根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }



}
