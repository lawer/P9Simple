package com.company.p9simple.viewmodel;

import android.app.Application;
import android.util.Log;

import com.company.p9simple.db.AppDao;
import com.company.p9simple.db.AppDatabase;
import com.company.p9simple.model.User;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class LoginViewModel extends AndroidViewModel {

    public enum AuthenticationState {
        UNAUTHENTICATED,
        AUTHENTICATED,
        INVALID_AUTHENTICATION
    }

    Application application;
    AppDao appDao;
    public final MutableLiveData<AuthenticationState> authenticationState = new MutableLiveData<>();
    public User currentUser;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        appDao = AppDatabase.getInstance(application).dao();

        authenticationState.setValue(AuthenticationState.UNAUTHENTICATED);
    }


    public MutableLiveData<AuthenticationState> login(String username, String password) {
        final LiveData<User> authenticate = appDao.authenticate(username, password);

        authenticate.observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    currentUser = user;
                    authenticationState.setValue(AuthenticationState.AUTHENTICATED);
                } else {
                    authenticationState.setValue(AuthenticationState.INVALID_AUTHENTICATION);
                }
                authenticate.removeObserver(this);
            }
        });

        return authenticationState;
    }

    public MutableLiveData<AuthenticationState> logout() {
        currentUser = null;
        authenticationState.setValue(AuthenticationState.UNAUTHENTICATED);

        return authenticationState;
    }
}