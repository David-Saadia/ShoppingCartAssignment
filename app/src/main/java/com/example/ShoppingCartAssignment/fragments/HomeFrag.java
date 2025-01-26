package com.example.ShoppingCartAssignment.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ShoppingCartAssignment.R;
import com.example.ShoppingCartAssignment.models.Adapter;
import com.example.ShoppingCartAssignment.models.ProductAnimal;
import com.example.ShoppingCartAssignment.models.MiniProduct;
import com.example.ShoppingCartAssignment.models.Products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFrag#newInstance} factory method to
 * create an instance of this fragment.
 */

// Implementing Adapter.OnCardClickListener to gain capabilities of the interface listener we wrote in the adapter class.
public class HomeFrag extends Fragment implements Adapter.OnCardClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference("users");

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    public HomeFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFrag newInstance(String param1, String param2) {
        HomeFrag fragment = new HomeFrag();
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
        View view = inflater.inflate(R.layout.homefrag, container, false);

        //Variables we'll need to populate RecyclerView.
        TextView textUserGreeting = view.findViewById(R.id.TextHomeUserGreeting);
        RecyclerView recyclerView = view.findViewById(R.id.recView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ArrayList<ProductAnimal> productList = new ArrayList<>();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //Generating products list for RecycleView Items
        for(int i = 0; i< Products.productTypes.length; i++){
            Log.d("ItemCollection","Collecting items...");
            productList.add(new ProductAnimal(
                    Products.productTypes[i],
                    Products.productImages[i],
                    Products.productDescriptions[i]

            ));
        }
        // Since we implement the listener, we set this as the listener of the adapter.
        Adapter adapter = new Adapter(productList, this);
        recyclerView.setAdapter(adapter);
        Log.d("TEST", "THE EMAIL IS " + currentUser.getEmail());
        textUserGreeting.setText("Hello " + currentUser.getEmail().split("@")[0]);


        return view;
    }
    @Override
    public void onCardButtonClick(MiniProduct product, int value, View cardView) {
        // Getting Firebase reference and user instance
        String uid = currentUser.getUid();
        DatabaseReference myRefUser = myRef.child(uid).child("products");

        // Read all user's products and check if the selected product exists in the list
        myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean productExists = false;
                //Amount textbox
                TextView textAmount = (TextView) cardView.findViewById(R.id.amountTextBox);
                // for every item in user's product list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Bring a value from the products list of the given user.
                    MiniProduct existingProduct = snapshot.getValue(MiniProduct.class);

                    if (existingProduct != null && existingProduct.getaType().equals(product.getaType())) {
                        // Product exists, increment its amount
                        if(!(existingProduct.getAmount()<=0) || !(value == -1)){
                            existingProduct.setAmount(existingProduct.getAmount() + value);
                            textAmount.setText(String.valueOf(existingProduct.getAmount()));
                        }

                        else
                            Toast.makeText(getContext(), "No items stored, cant decrement under zero!", Toast.LENGTH_SHORT).show();


                        snapshot.getRef().setValue(existingProduct);

                        Log.d("Success", "Product amount updated successfully");
                        productExists = true;
                        break;
                    }
                }
                // After checking all the items in the list, we check if we discovered the item or not with the flag product exists.
                // If product does not exist, add it to the list
                if (!productExists) {
                    myRefUser.push().setValue(new MiniProduct(product.getaType(), 0));
                    textAmount.setText("0");

                    Log.d("Success", "New product added successfully");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("tag", "Failed to read value.", error.toException());
            }
        });



        }








}