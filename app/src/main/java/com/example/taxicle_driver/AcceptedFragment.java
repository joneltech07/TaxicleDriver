package com.example.taxicle_driver;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taxicle_driver.Model.AdvanceBooking;
import com.example.taxicle_driver.adapter.AcceptedAdvanceBookingAdapter;
import com.example.taxicle_driver.adapter.AdvanceBookingAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcceptedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcceptedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AcceptedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AcceptedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AcceptedFragment newInstance(String param1, String param2) {
        AcceptedFragment fragment = new AcceptedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    FirebaseAuth auth;
    FirebaseUser user;

    RecyclerView recyclerView;
    AcceptedAdvanceBookingAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advance_booking, container, false);

        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize adapter and set it to the RecyclerView
        FirebaseRecyclerOptions<AdvanceBooking> options =
                new FirebaseRecyclerOptions.Builder<AdvanceBooking>()
                        .setQuery(
                                FirebaseDatabase.getInstance()
                                        .getReference("AcceptedAdvanceBooking")
                                        .child(user.getUid()),
                                AdvanceBooking.class
                        )
                        .build();

        adapter = new AcceptedAdvanceBookingAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();



        return view;
    }
}