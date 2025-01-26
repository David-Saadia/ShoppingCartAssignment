package com.example.ShoppingCartAssignment.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ShoppingCartAssignment.R;


import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private final ArrayList<ProductAnimal> productList;
    private final OnCardClickListener listener;

    // This interface serves as onclick listener to define the onclick function to the buttons after inflating them in a different fragment.
    public interface OnCardClickListener {
        void onCardButtonClick(MiniProduct product, int value, View view);
    }



    public Adapter(ArrayList<ProductAnimal> productList, OnCardClickListener listener){
        this.productList=productList;
        //Setting the onclickListener for the button within the card.
        this.listener= listener;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textaType;
        TextView textaDesc;
        ImageView aImage;
        Button addBtn;
        Button removeBtn;
        TextView textAmount;

        //Setting variables to each card item holder (hence ViewHolder).
        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.CardLayoutRes);
            textaType = itemView.findViewById(R.id.textProductType);
            textaDesc = itemView.findViewById(R.id.textProductDescription);
            textAmount = itemView.findViewById(R.id.amountTextBox);
            aImage = itemView.findViewById(R.id.ProductImage);
            addBtn = itemView.findViewById(R.id.btnAddItem);
            removeBtn = itemView.findViewById(R.id.btnRemoveItem);

        }


    }
    @NonNull
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
        return new MyViewHolder(view);

    }

    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, int position){
        //Retrieve all objects we need to edit at bind of the holder.
        TextView textViewCName = holder.textaType;
        TextView textViewCDesc = holder.textaDesc;
        ImageView imageCharacterImage = holder.aImage;
        Button addBtn = holder.addBtn;
        Button removeBtn = holder.removeBtn;

        //Setting the objects (components) with their respective information from the dummy data list.
        textViewCName.setText(productList.get(position).getaType());
        textViewCDesc.setText(productList.get(position).getaDesc());
        imageCharacterImage.setImageResource(productList.get(position).getaImage());

        //Setting the button listener to an arrow function to trigger our listener function.
        //Additionally while we have the information, we send it to our function as well.
        // plus button...
        addBtn.setOnClickListener(v -> {
            if (listener != null){
                MiniProduct miniProduct = new MiniProduct(productList.get(position).getaType(), 1);
                listener.onCardButtonClick(miniProduct, 1, holder.itemView);
            }
        });
        // minus button..
        removeBtn.setOnClickListener(v -> {
            if (listener != null){
                MiniProduct miniProduct = new MiniProduct(productList.get(position).getaType(), 1);
                listener.onCardButtonClick(miniProduct, -1, holder.itemView);

            }
        });

        //Initializing our view with the amount of products already saved on the server by the user.
        //This is to make sure that the application remembers your shopping cart amounts from last session.
        MiniProduct miniProduct = new MiniProduct(productList.get(position).getaType(), 0);
        listener.onCardButtonClick(miniProduct, 0, holder.itemView);


   }

    @Override
    public int getItemCount() {return productList.size();}


}
