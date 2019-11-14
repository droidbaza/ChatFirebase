package com.droidbaza.chatapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.droidbaza.chatapp.R;
import com.droidbaza.chatapp.message.Message;
import com.droidbaza.chatapp.message.MessageAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ChatFragment extends Fragment  {

    private MessageAdapter adapter;
    private Button sendBut;
    private EditText editMes;
    private FirebaseDatabase fireBase;
    private DatabaseReference baseRef;
    private RecyclerView rv;
    private String userName,groupName;
   // private String newToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {

        sendBut = v.findViewById(R.id.sendMessage);
        editMes = v.findViewById(R.id.editMessage);
        rv = v.findViewById(R.id.messageList);
        getData();
    }

    private void getData() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        userName = sp.getString("KEY_NAME",null);
        groupName = sp.getString("KEY_GROUP",null);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(getContext());
        rv.setAdapter(adapter);

        if(userName!=null&&groupName!=null){

        FirebaseInstanceId
                .getInstance()
                .getInstanceId()
                .addOnSuccessListener(Objects.requireNonNull(getActivity()),  new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {

                        fireBase = FirebaseDatabase.getInstance();

                        //newToken = instanceIdResult.getToken();
                        //messagesRef = database.getReference().child(newToken);
                        baseRef = fireBase.getReference().child(groupName);
                        baseRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                Message message = dataSnapshot.getValue(Message.class);
                                adapter.addMessage(message);
                                rv.scrollToPosition(adapter.getItemCount()-1);
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        editMes.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                if(s.toString().trim().length()>0){
                                    sendBut.setEnabled(true);
                                }else {
                                    sendBut.setEnabled(false);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        editMes.setFilters(new InputFilter[]{
                                new InputFilter.LengthFilter(500)
                        });
                        sendBut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Message message = new Message();
                                message.setMessage(editMes.getText().toString());
                                //message.setName(userName);

                                Date date = Calendar.getInstance().getTime();
                                String today = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
                                message.setTimeStamp(today);
                                message.setName(userName);
                                baseRef.push().setValue(message);
                                editMes.setText("");
                            }
                        });

                    }
                });

    }
    }
}