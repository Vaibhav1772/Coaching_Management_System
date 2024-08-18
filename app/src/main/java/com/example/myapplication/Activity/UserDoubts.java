package com.example.myapplication.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.Fade;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.MessageAdapter;
import com.example.myapplication.BuildConfig;
import com.example.myapplication.GenerativeAI.ChatHistory;
import com.example.myapplication.GenerativeAI.ChatMessage;
import com.example.myapplication.GenerativeAI.Model;
import com.example.myapplication.R;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserDoubts extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private EditText userMessage;

    private MessageAdapter messageAdapter;
    private List<ChatMessage> messageList;
    private ExecutorService executorService;
    private Handler handler;
    private Model model;

    public UserDoubts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserDoubts.
     */
    // TODO: Rename and change types and number of parameters
    public UserDoubts newInstance(String param1, String param2) {
        UserDoubts fragment = new UserDoubts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.chat_area, container, false);

        setEnterTransition(new Slide());
        setExitTransition(new Fade());

        recyclerView=view.findViewById(R.id.chat_recycler_view);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setItemPrefetchEnabled(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        messageList = ChatHistory.getChatMessages();
        messageAdapter = new MessageAdapter(getContext(), messageList);
        recyclerView.setAdapter(messageAdapter);
        userMessage=view.findViewById(R.id.userText);
        Button send = view.findViewById(R.id.send_btn);
        executorService= Executors.newSingleThreadExecutor();

        handler=new Handler(Looper.getMainLooper());

        model=new Model(BuildConfig.apiKey);
        model.startChat();
        if(messageList.isEmpty())
            handler.postDelayed(this::sendInitialGreeting,600);

        send.setOnClickListener(v -> {
            String messageContent = userMessage.getText().toString();
            if (!messageContent.isEmpty()) {
                ChatMessage chatMessage = new ChatMessage("user", messageContent, LocalTime.now());
                ChatHistory.addMessage(chatMessage);
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);
                userMessage.setText("");

                executorService.submit(()->sendMessageToModel(messageContent));

            }
        });
        return view;
    }
    private void sendInitialGreeting() {
        // Send a predefined greeting message
        executorService.submit(this::showGreetings);
    }

    private void showGreetings() {
        handler.postDelayed(()->{
            ChatMessage greetingMessage = new ChatMessage("model", "Hii there! What can I help you with?", LocalTime.now());
            ChatHistory.addMessage(greetingMessage);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
        },400);
    }
    private void sendMessageToModel(String prompt) {
        model.sendMessage(prompt, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                // Handle successful response
                String modelResponse = result.getText();
                handler.postDelayed(() -> {
                    ChatMessage modelMessage = new ChatMessage("model", modelResponse, LocalTime.now());
                    ChatHistory.addMessage(modelMessage);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                },200);
            }

           @Override
            public void onFailure(@NonNull Throwable t) {
                // Handle failure
                handler.post(() -> showErrorMessage());
            }
        });
    }

    private void showErrorMessage() {
        ChatMessage errorMsg = new ChatMessage("error", "Unexpected error occurred.", LocalTime.now());
        ChatHistory.addMessage(errorMsg);
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
