package com.example.backapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.extensions.style

class smsAdapter(val smses:List<sms>): RecyclerView.Adapter<smsAdapter.Holder>() {

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        val Tresc: TextView
        var Card: CardView
        init{
            Tresc= itemView.findViewById<TextView>(R.id.messageText)
            Card= itemView.findViewById<CardView>(R.id.messageCard)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sms,parent,false) as View

        return  Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.Tresc.text=smses[position].body;
        if(smses[position].sentOrReceived) {
            holder.Card.style(R.style.cardOut);
            holder.Card.setCardBackgroundColor(Color.parseColor("#8BC34A"));
        }
        else {
            holder.Card.style(R.style.cardIn)
        }

    }

    override fun getItemCount()=smses.count()

}