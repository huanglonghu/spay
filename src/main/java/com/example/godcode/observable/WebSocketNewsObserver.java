package com.example.godcode.observable;

public interface WebSocketNewsObserver<T> {

    void onUpdate(WebSocketNewsObservable<T> observable, T data);
}
