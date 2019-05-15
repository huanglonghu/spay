package com.example.godcode.observable;

import java.util.ArrayList;
import java.util.List;

public class WebSocketNewsObservable<T> {

    List<WebSocketNewsObserver<T>> mObservers = new ArrayList<WebSocketNewsObserver<T>>();

    public void register(WebSocketNewsObserver observer) {
        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        synchronized (this) {
            if (!mObservers.contains(observer))
                mObservers.add(observer);
        }
    }

    public synchronized void unregister(WebSocketNewsObserver observer) {
        mObservers.remove(observer);
    }

    public void notifyObservers(T data) {
        for (WebSocketNewsObserver<T> observer : mObservers) {
            observer.onUpdate(this, data);
        }
    }
}
