package com.company.p9simple.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.company.p9simple.R;
import com.company.p9simple.model.User;
import com.company.p9simple.viewmodel.LoginViewModel;
import com.company.p9simple.viewmodel.RegistrationViewModel;


public class LoginFragment extends Fragment {

    private EditText usernameEditText, passwordEditText;
    private LoginViewModel loginViewModel;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);

        view.findViewById(R.id.goto_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.registerFragment);
            }
        });

        usernameEditText = view.findViewById(R.id.username);
        passwordEditText = view.findViewById(R.id.password);

        view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LiveData<User> user = loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                user.observe(getViewLifecycleOwner(), new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        LoginViewModel.AuthenticationState authenticationState = loginViewModel.setAuthState(user);

                        if (authenticationState == LoginViewModel.AuthenticationState.AUTHENTICATED) {
                            Navigation.findNavController(view).navigate(R.id.profileFragment);
                        } else if (authenticationState == LoginViewModel.AuthenticationState.INVALID_AUTHENTICATION) {
                            Toast.makeText(getContext(), "INVALID CREDENTIALS", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
