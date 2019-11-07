package com.company.p9simple.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.company.p9simple.R;
import com.company.p9simple.model.User;
import com.company.p9simple.viewmodel.LoginViewModel;
import com.company.p9simple.viewmodel.RegistrationViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;


public class RegisterFragment extends Fragment {

    LoginViewModel loginViewModel;
    RegistrationViewModel registrationViewModel;

    EditText usernameEditText, passwordEditText, bioEditText;


    public RegisterFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);
        registrationViewModel = ViewModelProviders.of(requireActivity()).get(RegistrationViewModel.class);

        usernameEditText = view.findViewById(R.id.username);
        passwordEditText = view.findViewById(R.id.password);
        bioEditText = view.findViewById(R.id.bio);

        view.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrationViewModel.createAccountAndLogin(usernameEditText.getText().toString(), passwordEditText.getText().toString(), bioEditText.getText().toString());
            }
        });

        registrationViewModel.registrationState.observe(getViewLifecycleOwner(), new Observer<RegistrationViewModel.RegistrationState>() {
            @Override
            public void onChanged(RegistrationViewModel.RegistrationState registrationState) {
                if(registrationState == RegistrationViewModel.RegistrationState.REGISTRATION_COMPLETED){
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

                    registrationViewModel.registrationState.setValue(RegistrationViewModel.RegistrationState.COLLECT_USER_DATA);
                } else if(registrationState == RegistrationViewModel.RegistrationState.USERNAME_NOT_AVAILABLE){
                    Toast.makeText(getContext(), "USERNAME NOT AVAILABLE", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
