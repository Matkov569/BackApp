package com.example.backapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.extensions.style

class smsAdapter(val context: Context?, val vm:VM): RecyclerView.Adapter<smsAdapter.Holder>() {

    private var smses = emptyList<sms>();

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
        if(smses[position].body.length >= 3
            && smses[position].body.substring(0,3)=="@\$!"
        ){
            var key = if(smses[position].sentOrReceived) vm.myPhone else smses[position].sender
            holder.Tresc.text = superUnRoman(smses[position].body, key);
        }
        else
            holder.Tresc.text=smses[position].body;


        if(smses[position].sentOrReceived) {
            holder.Card.style(R.style.cardOut);
            holder.Card.setCardBackgroundColor(Color.parseColor("#8BC34A"));
        }
        else {
            holder.Card.style(R.style.cardIn)
            holder.Card.setCardBackgroundColor(Color.parseColor("#FF9800"));
        }

    }

    fun setData(sms:List<sms>){
        this.smses=sms;
        notifyDataSetChanged();
    }

    fun DataSetChanged(){
        this.smses=vm.getSMS(context,vm.currentChat).value!!;
        notifyDataSetChanged();
    }

    private fun superUnRoman(text: String, keyS: String):String{
        var key:String;
        if(keyS[0]=='+'){
            key=keyS.substring(3);
        }
        else{
            key=keyS;
        }
        var crop = text.substring(3);
        var ret = "";
        var ind=0;
        for(i in crop){
            ret += i + key[ind].toString().toInt();
            ind += 1;
            ind %= key.length;
        }
        return ret;
    }

    override fun getItemCount()=smses.count()

}