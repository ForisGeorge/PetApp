package com.example.petapp2.ViewController;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petapp2.Pets.Pets;
import com.example.petapp2.R;

import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ImageViewHolder> {

    private Context context;
    private List<Pets> pets;
    private OnClickListener listener;

    public CardViewAdapter(Context context, List<Pets> pets){
        this.context=context;
        this.pets=pets;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_view,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Pets curentPet = pets.get(position);
        holder.cardViewName.setText(curentPet.getName());
       Glide.with(context).load(curentPet.getUri()).into(holder.cardViewImage);

    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener {

        public TextView cardViewName;
        public ImageView cardViewImage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            cardViewName = itemView.findViewById(R.id.card_view_name);
            cardViewImage = itemView.findViewById(R.id.card_view_image);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if(listener!=null){
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    listener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem  selectItem = contextMenu.add(Menu.NONE,1,1,"Select this Pet");
            MenuItem deleteItem = contextMenu.add(Menu.NONE,2,2,"Delete Pet");

            selectItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if(listener!=null){
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){

                    switch (menuItem.getItemId()){
                        case 1:
                            listener.onSelectItemClick(position);
                            return true;
                        case 2:
                            listener.onDeleteItemClick(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }


    public interface OnClickListener {
        void onItemClick(int position);

        void onSelectItemClick(int position);

        void onDeleteItemClick(int position);
    }
    public void setOnItemClickListener(OnClickListener listener){
        this.listener = listener;
    }
}
