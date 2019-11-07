package com.company.p9simple.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import com.company.p9simple.db.AppDao;
import com.company.p9simple.db.AppDatabase;
import com.company.p9simple.model.User;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class RegistrationViewModel extends AndroidViewModel {

    public enum RegistrationState {
        COLLECT_USER_DATA,
        USERNAME_NOT_AVAILABLE,
        REGISTRATION_COMPLETED
    }

    AppDao dao;
    public MutableLiveData<RegistrationState> registrationState = new MutableLiveData<>();
    public User registeredUser;

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application).dao();

        registrationState.setValue(RegistrationState.COLLECT_USER_DATA);
    }

    public void createAccountAndLogin(final String username, final String password, final String bio) {
        dao.getUser(username).observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user == null){
                    final User newUser = new User(username, password, bio);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            dao.insertUser(newUser);
                        }
                    });
                    registeredUser = newUser;
                    registrationState.setValue(RegistrationState.REGISTRATION_COMPLETED);
                } else {
                    registrationState.setValue(RegistrationState.USERNAME_NOT_AVAILABLE);
                }
            }
        });
    }
}