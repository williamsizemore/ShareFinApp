package com.example.sharefinapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/*
    Fragment for the TabLayout in the ActivityFeed screen
 */
public class GroupFragment extends Fragment {
    private RecyclerView groupList;
    private FirestoreRecyclerAdapter<Group, GroupViewHolder> groupAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_group, container, false);
        RecyclerView recyclerView = viewGroup.findViewById(R.id.group_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = FirebaseFirestore.getInstance().collection("groups").whereArrayContains("groupUsers",DBManager.getInstance().getCurrentUserEmail());
//        Query query = FirebaseFirestore.getInstance().collection("groups");

        FirestoreRecyclerOptions<Group> options = new FirestoreRecyclerOptions.Builder<Group>().setQuery(query, Group.class).build();

        groupAdapter = new FirestoreRecyclerAdapter<Group, GroupViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GroupViewHolder groupViewHolder, int i, @NonNull Group group) {
                groupViewHolder.bind(group);
            }

            @NonNull
            @Override
            public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item_layout,parent,false);
                return new GroupViewHolder(view);
            }
        };

        recyclerView.setAdapter(groupAdapter);
        return viewGroup;

    }

    @Override
    public void onStart()
    {
        super.onStart();
        groupAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (groupAdapter != null)
        {
            groupAdapter.stopListening();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        Bundle args = getArguments();
//        groupList.setDBManager.getInstance().getDb().collection("groups").get();
//        Query query = FirebaseFirestore.getInstance().collection("groups").whereArrayContains("groupUsers",DBManager.getInstance().getCurrentUserEmail());

//        FirestoreRecyclerOptions<Group> options = new FirestoreRecyclerOptions.Builder<Group>().setQuery(query, Group.class).build();

    }

    private class GroupViewHolder extends RecyclerView.ViewHolder {
        private final View view;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        void bind(Group group)
        {
            TextView groupName = view.findViewById(R.id.group_item);
            groupName.setText(group.getGroupName());

//            TextView groupUser = view.findViewById(R.id.group_user_item);
//            groupUser.setText(group.getGroupUsers().get(0));
        }

    }

}
