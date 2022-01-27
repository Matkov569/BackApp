package com.example.backapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.extensions.style

class contactsAdapter(val contacts:List<String>, val vm:VM): RecyclerView.Adapter<contactsAdapter.Holder>() {

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        val Name: TextView
        var Image: ImageView
        init{
            Name = itemView.findViewById<TextView>(R.id.contactName)
            Image = itemView.findViewById<ImageView>(R.id.contactImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact,parent,false) as View

        return  Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.Name.text=contacts[position];
        holder.Name.setOnClickListener {
            vm.currentChat=contacts[position];
            it.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        holder.Image.setOnClickListener {
            vm.currentChat=contacts[position];
            it.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun getItemCount()=contacts.count()
}
