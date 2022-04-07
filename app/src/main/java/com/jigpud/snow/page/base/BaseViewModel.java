package com.jigpud.snow.page.base;

import androidx.annotation.CallSuper;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author jigpud
 */
public abstract class BaseViewModel extends ViewModel {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @CallSuper
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    protected void lifecycle(Disposable disposable) {
        compositeDisposable.add(disposable);
    }
}
