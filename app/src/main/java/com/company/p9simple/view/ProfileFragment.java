package com.company.p9simple.view;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.p9simple.R;
import com.company.p9simple.model.User;
import com.company.p9simple.viewmodel.LoginViewModel;


public class ProfileFragment extends Fragment {

    TextView usernameTextView, bioTextView;

    private LoginViewModel loginViewModel;

    public ProfileFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);

        usernameTextView = view.findViewById(R.id.username);
        bioTextView = view.findViewById(R.id.bio);

        loginViewModel.authenticationState.observe(getViewLifecycleOwner(), new Observer<LoginViewModel.AuthenticationState>() {
            @Override
            public void onChanged(LoginViewModel.AuthenticationState authenticationState) {
                switch (authenticationState) {
                    case AUTHENTICATED:
                        showProfile(loginViewModel.currentUser);
                        break;
                    case UNAUTHENTICATED:
                        Navigation.findNavController(view).navigate(R.id.loginFragment);
                        break;
                }
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        Navigation.findNavController(view).popBackStack(R.id.homeFragment, false);
                    }
                });

    }

    private void showProfile(User user) {
        usernameTextView.setText(user.username);
        bioTextView.setText(user.bio);
    }
}
