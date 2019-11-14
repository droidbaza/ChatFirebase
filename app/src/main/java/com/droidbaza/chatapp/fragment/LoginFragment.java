package com.droidbaza.chatapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.droidbaza.chatapp.R;
import java.util.Objects;

public class LoginFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        Button button = v.findViewById(R.id.button_login);
        final EditText name,group;
        name = v.findViewById(R.id.user_name);
        group = v.findViewById(R.id.group_name);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String groupName = group.getText().toString();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("KEY_NAME", userName);
                editor.putString("KEY_GROUP",groupName);
                editor.apply();

                if(userName.length()!=0&&groupName.length()!=0){
                    loadFragment();
                }
            }
        });

    }

    private void loadFragment() {
        final FragmentTransaction ft = Objects.requireNonNull(getActivity())
                .getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.cont, new ChatFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

}
