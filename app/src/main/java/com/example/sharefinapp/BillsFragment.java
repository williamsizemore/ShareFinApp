package com.example.sharefinapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BillsFragment extends Fragment {
    private FirestoreRecyclerAdapter<Bill, BillViewHolder> billAdapter;
    private final ArrayList<Group> associatedGroups = new ArrayList<>();
    private  ViewGroup viewGroup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<Group> groups = new ArrayList<>();

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_bills, container, false);




        getUsers(new BillFragmentCallback() {
            @Override
            public void onCallBack(ArrayList<Group> groupList)
            {
                Log.v("groupList",groupList.toString());
              for (int i =0; i < groupList.size(); i++) {
                  groups.add(groupList.get(i));
              }
              populateBills();

            }
        });


        return viewGroup;
    }
    private void populateBills() {
        RecyclerView recyclerView = viewGroup.findViewById(R.id.bill_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<String> associatedGroupIDs = new ArrayList<>();
        for (int i = 0; i < associatedGroups.size(); i++)
            associatedGroupIDs.add(associatedGroups.get(i).getGroupID());

        Query query = FirebaseFirestore.getInstance().collection("bills").whereIn("groupID", associatedGroupIDs); //todo update query to include just the users stuff
        FirestoreRecyclerOptions<Bill> options = new FirestoreRecyclerOptions.Builder<Bill>().setQuery(query, Bill.class).build();

        billAdapter = new FirestoreRecyclerAdapter<Bill, BillViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BillViewHolder billViewHolder, int i, @NonNull Bill bill) {
                billViewHolder.bind(bill);
            }

            @NonNull
            @Override
            public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item_layout, parent, false);
                return new BillViewHolder(view);
            }
        };
        recyclerView.setAdapter(billAdapter);
    }
    private void getUsers(BillFragmentCallback billFragmentCallback)
    {
        FirebaseFirestore.getInstance().collection("groups").whereArrayContains("groupUserIDs", DBManager.getInstance().getCurrentUserID()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot ds : task.getResult())
                    associatedGroups.add(ds.toObject(Group.class));
                billFragmentCallback.onCallBack(associatedGroups);
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        billAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (billAdapter != null)
            billAdapter.stopListening();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
//        DBManager.getInstance().getDb().collection("bills").whereEqualTo()
    }


    private class BillViewHolder extends RecyclerView.ViewHolder {
        private final View view;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        @SuppressLint("SetTextI18n")
        void bind(Bill bill) {
            TextView billName, amount, userOwes, groupName;

            billName = view.findViewById(R.id.bill_item_name);
            amount = view.findViewById(R.id.bill_item_amount);
            userOwes = view.findViewById(R.id.bill_item_user_owes);
            groupName = view.findViewById(R.id.bill_item_group_name);

            billName.setText(bill.getName());
            amount.setText("$" + bill.getAmountDue());

            groupName.setText(bill.getGroupID());

            userOwes.setText("You owe $" + bill.getBillSplit().get(DBManager.getInstance().getCurrentUserEmail()));
        }
    }


    private interface BillFragmentCallback {
        void onCallBack(ArrayList<Group> groupList);
    }
}