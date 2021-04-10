package com.example.sharefinapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/*
    Fragment for the TabLayout in the ActivityFeed screen
 */
public class GroupFragment extends Fragment {
    private RecyclerView groupList;
    private FirestoreRecyclerAdapter<Group, GroupViewHolder> groupAdapter;
    private ArrayList<Bill> associatedBills;
    private ArrayList<Group> associatedGroups;
    private ViewGroup viewGroup;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_group, container, false);
        progressBar = viewGroup.findViewById(R.id.groupFragmentProgressBar);
        getAssociatedGroups();



        return viewGroup;

    }
    private void setupRecycler()
    {
        RecyclerView recyclerView = viewGroup.findViewById(R.id.group_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = FirebaseFirestore.getInstance().collection("groups").whereArrayContains("groupUserIDs",DBManager.getInstance().getCurrentUserID()).orderBy("createDate", Query.Direction.DESCENDING);


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
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(groupAdapter);
        progressBar.setVisibility(View.GONE);
        groupAdapter.startListening();
    }
    public void getAssociatedGroups() {
        associatedBills = new ArrayList<>();
        associatedGroups = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        DBManager.getInstance().getDb().collection("groups").whereArrayContains("groupUserIDs",DBManager.getInstance().getCurrentUserID()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot ds: queryDocumentSnapshots) {
                    associatedGroups.add(ds.toObject(Group.class));
                }
//                if (associatedGroups == null)
//                    while (associatedGroups == null)
//                        try {
//                            wait(100);
//                        } catch (Exception e)
//                        {
//                            Log.e(TAG, e.getStackTrace().toString());
//                        }
//                else
                    getAssociatedBills();


            }
        });
    }

    private void getAssociatedBills()
    {
        List<String> groupIDs = new ArrayList<>();
        for (int i=0;i< associatedGroups.size();i++)
            groupIDs.add(associatedGroups.get(i).getGroupID());
            if (!groupIDs.isEmpty()) {
                DBManager.getInstance().getDb().collection("bills").whereIn("groupID", groupIDs).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds : queryDocumentSnapshots) {
                            associatedBills.add(ds.toObject(Bill.class));
                        }
                        setupRecycler();
                    }

                });
            }
            else
                setupRecycler();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (groupAdapter == null) {
            try {
                groupAdapter.wait(100);
                onStart();
            }
            catch (Exception e)
            {
                Log.e(TAG, e.getStackTrace().toString());
            }
        }
        else groupAdapter.startListening();
    }
    @Override
    public void onResume(){
        super.onResume();
        if (groupAdapter == null) {
            try {
                groupAdapter.wait(100);
                onResume();
            }
            catch (Exception e)
            {
                Log.e(TAG, e.getStackTrace().toString());
            }
        }
        else
            getAssociatedGroups();
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

            TextView groupUser = view.findViewById(R.id.group_amount);

            double groupAmount = 0.00;
            for (int i=0; i < associatedBills.size(); i++) {
                if (group.getGroupID().equals(associatedBills.get(i).getGroupID()))
                {
                    groupAmount += associatedBills.get(i).getAmountDue();
                }
            }
            DecimalFormat df = new DecimalFormat("###.##");
            groupUser.setText("Total Amounts: $" + df.format(groupAmount));
        }

    }

}
