package com.hanyghazal.myecommerceapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView textViewCategoryName;
    ImageView imageViewCategoryImage;
    ItemClickListener itemClickListener;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewCategoryName = itemView.findViewById(R.id.text_view_category_name);
        imageViewCategoryImage = itemView.findViewById(R.id.image_view_category_image);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onItemClick(v, getAdapterPosition(), false);


    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////        view = parent.getSelectedView();
////        textViewCategoryName = view.findViewById(R.id.text_view_category_name);
////        Commons.selectedCategory = textViewCategoryName.getText().toString();
//
//    }
}
