package com.example.sharefinapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class BillsFragment extends Fragment  {
    private FirestoreRecyclerAdapter<Bill, BillViewHolder> billAdapter;
    private final ArrayList<Group> associatedGroups = new ArrayList<>();
    private  ViewGroup viewGroup;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_bills, container, false);
        progressBar = viewGroup.findViewById(R.id.billFragmentProgressBar);
        getAssociatedGroups();
        return viewGroup;
    }

    /*
         Query to get the groupIDs associated with the current user
     */
    private void getAssociatedGroups()
    {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("groups").whereArrayContains("groupUserIDs", DBManager.getInstance().getCurrentUserID()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot ds : task.getResult())
                    associatedGroups.add(ds.toObject(Group.class));

                populateBills();    //once the groups are loaded, populate the bills using the provided group data
            }
        });

    }

    /*
        query the bills associated with the user by using the groupIDs to filter just the relevant bills
     */
    private void populateBills() {
        RecyclerView recyclerView = viewGroup.findViewById(R.id.bill_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<String> associatedGroupIDs = new ArrayList<>();
        for (int i = 0; i < associatedGroups.size(); i++)
            associatedGroupIDs.add(associatedGroups.get(i).getGroupID());

        Query query;
        FirestoreRecyclerOptions<Bill> options;
        if (!associatedGroupIDs.isEmpty()) {
            query = FirebaseFirestore.getInstance().collection("bills").whereIn("groupID", associatedGroupIDs).orderBy("createDate", Query.Direction.DESCENDING); //todo update query to include just the users stuff

            options = new FirestoreRecyclerOptions.Builder<Bill>().setQuery(query, Bill.class).build();

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
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(billAdapter);
            billAdapter.startListening();

        }
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (billAdapter == null) {
            try {
                billAdapter.wait(100);
                onStart();
            }
            catch (Exception e){
                Log.e(TAG, e.getStackTrace().toString());
            }
        }
        else
            billAdapter.startListening();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (billAdapter == null) {
            try {
                billAdapter.wait(100);
                onResume();
            }
            catch (Exception e){
                Log.e(TAG, e.getStackTrace().toString());
            }
        }
        else
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

    }

    /*
        Bill View holder to display each bill in the recycler view
     */
    private class BillViewHolder extends RecyclerView.ViewHolder  {
        private final View view;
        private Bill bill;
        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        @SuppressLint("SetTextI18n")
        void bind(Bill bill) {
            this.bill = bill;
            TextView billName, amount, userOwes, groupName;

            billName = view.findViewById(R.id.bill_item_name);
            amount = view.findViewById(R.id.bill_item_amount);
            userOwes = view.findViewById(R.id.bill_item_user_owes);
            groupName = view.findViewById(R.id.bill_item_group_name);

            billName.setText(bill.getName());
            amount.setText("$" + bill.getAmountDue());

            String group_name = "";
            for (int i=0;i< associatedGroups.size();i++)
                if (associatedGroups.get(i).getGroupID().equals(bill.getGroupID()))
                    group_name = associatedGroups.get(i).getGroupName();
            groupName.setText("Group: " + group_name);

            userOwes.setText("You owe $" + bill.getBillSplit().get(DBManager.getInstance().getCurrentUserEmail()));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("test Bill Item clicked: ", " opening " + bill.getName());
                    Intent billIntent = new Intent(getContext(),BillView.class);
                    billIntent.putExtra("billID",bill.getBillID());
                    startActivity(billIntent);
                }
            });
        }


    }


}