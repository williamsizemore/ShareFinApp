package com.example.sharefinapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class BillsFragment extends Fragment {
    private FirestoreRecyclerAdapter<Bill, BillViewHolder> billAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_bills, container, false);

        RecyclerView recyclerView = viewGroup.findViewById(R.id.bill_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = FirebaseFirestore.getInstance().collection("bills");  //todo update query to include just the users stuff
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
        return viewGroup;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        Bundle args = getArguments();
//        DBManager.getInstance().getDb().collection("bills").whereEqualTo()
    }



    private class BillViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(Bill bill)
        {
            //enter the formatting of the bill info here
        }
    }


}