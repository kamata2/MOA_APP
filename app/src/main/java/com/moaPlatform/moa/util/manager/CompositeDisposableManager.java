package com.moaPlatform.moa.util.manager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CompositeDisposableManager {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static CompositeDisposableManager getInstance() {
        return Singleton.instance;
    }

    public void add(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void dispose() {
        compositeDisposable.dispose();
    }

    private static class Singleton {
        private static CompositeDisposableManager instance = new CompositeDisposableManager();
    }
}
