package com.phoenix.mechat.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.phoenix.mechat.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private final ArrayList<Integer> arrayList;
    private ArrayList<String> arrayListH = new ArrayList<>();
    private ArrayList<String> arrayListC = new ArrayList<>();

    //Fonts
    Typeface typeface;

    //constructor
    public MyAdapter(Context context, ArrayList<Integer> arrayList, ArrayList<String> arrayListSH, ArrayList<String> arrayListSC){
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListH = arrayListSH;
        this.arrayListC = arrayListSC;
    }

    //inflate with the custom layout
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_splash, parent, false);
        return new MyViewHolder(view);
    }

    //add data
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //add content
//        arrayListH.add("Talk to those most special to you");
//        arrayListC.add("Keep the connection with those that matter most in your life");

        holder.imageViewSplash.setImageResource(arrayList.get(position));
        holder.textViewSplashH.setText(arrayListH.get(position));
        holder.textViewSplashC.setText(arrayListC.get(position));
    }

    //size of array returned
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    //initialize xml elements
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        //xml elements
        ImageView imageViewSplash;
        TextView textViewSplashH, textViewSplashC;
        //Fonts
        Typeface typeface, typeface1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewSplash = itemView.findViewById(R.id.imageViewSplash);
            textViewSplashH = itemView.findViewById(R.id.textViewSplashH);
            textViewSplashC = itemView.findViewById(R.id.textViewSplashC);
            //set fonts
            typeface = ResourcesCompat.getFont(itemView.getContext(), R.font.gothic_a1_bold);
            typeface1 = ResourcesCompat.getFont(itemView.getContext(), R.font.gothic_a1_regular);

            //setFonts
            textViewSplashH.setTypeface(typeface);
            textViewSplashC.setTypeface(typeface1);
        }
    }

}
