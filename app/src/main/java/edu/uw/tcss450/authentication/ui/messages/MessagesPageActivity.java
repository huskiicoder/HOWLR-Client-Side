package edu.uw.tcss450.authentication.ui.messages;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.tcss450.authentication.R;

import java.util.ArrayList;
import java.util.List;

public class MessagesPageActivity extends AppCompatActivity {

    /**
     * The RecyclerView.
     */
    RecyclerView mRecyclerView;

    /**
     * The LinearLayoutManager.
     */
    LinearLayoutManager mLinearLayoutManager;

    /**
     * The user list.
     */
    List<MessageModel> mUserList;

    /**
     * The MessageAdapter.
     */
    MessageAdapter mMessageAdapter;

    /**
     * The onCreate for the layout.
     * @param savedInstanceState The saved instance
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_messages_page);

        initializeData();
        initializeRecyclerView();
    }

    /**
     * Initializes the data in the messages page.
     */
    private void initializeData() {
        mUserList = new ArrayList<>();
        mUserList.add(new MessageModel(R.drawable.shibabone, "Charles Bryan",
                "2:30 pm", "Are you ready for the sprint review"));
        mUserList.add(new MessageModel(R.drawable.shibacoffee, "Amir Almemar",
                "3:30 pm", "Are you ready for the sprint review"));
        mUserList.add(new MessageModel(R.drawable.shibadab, "Daniel Jiang",
                "4:30 pm", "Are you ready for the sprint review"));
        mUserList.add(new MessageModel(R.drawable.shibadance, "Eddie Robinson",
                "5:30 pm", "Are you ready for the sprint review"));
        mUserList.add(new MessageModel(R.drawable.shibaheart, "Justin Aschenbrenner",
                "6:30 pm", "Are you ready for the sprint review"));
        mUserList.add(new MessageModel(R.drawable.shibalaptop, "Natalie Hong",
                "7:30 pm", "Are you ready for the sprint review"));

    }

    /**
     * Initializes the RecyclerView.
     */
    private void initializeRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageAdapter = new MessageAdapter(mUserList);
        mRecyclerView.setAdapter(mMessageAdapter);
        mMessageAdapter.notifyDataSetChanged();
    }
}