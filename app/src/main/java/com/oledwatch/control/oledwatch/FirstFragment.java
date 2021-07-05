package com.oledwatch.control.oledwatch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment {
    private EditText macAddrEditText;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        macAddrEditText = view.findViewById(R.id.editTextMACAddr);
        view.findViewById(R.id.buttonDiscover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                Context context = getActivity();
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);//check
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.ble_mac_string_key), macAddrEditText.getText().toString());
                editor.apply();
                Toast.makeText(context, macAddrEditText.getText().toString() , Toast.LENGTH_LONG).show();
                //getActivity().startService(new Intent(getActivity(), BackgroundNotificationsFetchService.class));
                //todo
                //tutaj połączenie z BT i testowa transmisja
            }
        });
    }
}