package com.company.p9simple.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.company.p9simple.R;
import com.company.p9simple.viewmodel.LoginViewModel;


public class HomeFragment extends Fragment {

    LoginViewModel loginViewModel;

    Button logoutButton;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);

        logoutButton = view.findViewById(R.id.logout_button);


        view.findViewById(R.id.goto_profile_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.profileFragment);
            }
        });

        loginViewModel.authenticationState.observe(getViewLifecycleOwner(), new Observer<LoginViewModel.AuthenticationState>() {
            @Override
            public void onChanged(LoginViewModel.AuthenticationState authenticationState) {
                if(authenticationState == LoginViewModel.AuthenticationState.AUTHENTICATED){
                    logoutButton.setVisibility(View.VISIBLE);
                } else {
                    logoutButton.setVisibility(View.GONE);
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.logout();
            }
        });
    }
}
