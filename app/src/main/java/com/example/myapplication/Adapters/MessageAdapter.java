package com.example.myapplication.Adapters;

import static android.text.format.DateUtils.formatDateTime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.GenerativeAI.ChatMessage;
import com.example.myapplication.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    // Adapter context and data
    private Context context;
    private List<ChatMessage> messageList;

    public MessageAdapter(Context context, List<ChatMessage> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_view_user, parent, false); // Correct layout for user message
            return new UserMessageViewHolder(view); // Correct ViewHolder for user message
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_view_model, parent, false); // Correct layout for model message
            return new ModelMessageViewHolder(view); // Correct ViewHolder for model message
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);

        if (holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).bindUser(message);
        } else if (holder instanceof ModelMessageViewHolder) {
            ((ModelMessageViewHolder) holder).bindModel(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messageList.get(position);
        if (message.getRole().equals("user")) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    public class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView userMessage, userTime;
        ImageView userImg;
        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            userMessage = itemView.findViewById(R.id.user_message);
            userTime = itemView.findViewById(R.id.time_user);
            userImg=itemView.findViewById(R.id.imageView36);
        }

        public void bindUser(ChatMessage message) {
            userMessage.setText(message.getContent());
            userTime.setText(message.getCreatedAt().format(DateTimeFormatter.ofPattern("hh:mm")));
            userImg.setImageResource(R.drawable.profile_icn);
        }
    }

    public class ModelMessageViewHolder extends RecyclerView.ViewHolder {
        TextView modelMessage, modelTime;
        ImageView modelImg;

        public ModelMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            modelMessage = itemView.findViewById(R.id.model_message);
            modelTime = itemView.findViewById(R.id.time_model);
            modelImg = itemView.findViewById(R.id.imageView37);

        }

        public void bindModel(ChatMessage message) {
            modelMessage.setText(message.getContent());
            modelTime.setText(message.getCreatedAt().format(DateTimeFormatter.ofPattern("hh:mm")));
            modelImg.setImageResource(R.drawable.google_gemini_icon);
        }
    }
}
