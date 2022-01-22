package com.example.backapp

import android.app.PendingIntent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import com.example.backapp.databinding.FragmentSecondBinding
import androidx.core.app.ActivityCompat.requestPermissions
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.telephony.SmsManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    lateinit var smses:List<sms>;
    lateinit var smsadapter: smsAdapter;

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second,container,false)
        //val listView: ListView = view.findViewById(R.id.messageList)


        //listView.adapter = aa
        /**
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root*/
        return view


    }

    lateinit var layoutMenager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel by activityViewModels<VM>();

        layoutMenager=LinearLayoutManager(context)
        layoutMenager.setReverseLayout(true)
        smses=viewModel.getSMS(context,viewModel.currentChat);
        smsadapter = smsAdapter(smses)

        view.findViewById<RecyclerView>(R.id.messageList).apply {
            adapter=smsadapter
            layoutManager=layoutMenager
        }

        view.findViewById<ImageButton>(R.id.return_btn).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        view.findViewById<TextView>(R.id.toolbarText).text=viewModel.currentChat;
    }

    fun smsTo(message: String){
        val viewModel by activityViewModels<VM>();
        sendSMS(viewModel.currentChat,message);
        smses=viewModel.getSMS(context,viewModel.currentChat);
        smsadapter.notifyDataSetChanged();
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        val sentPI: PendingIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"), 0)
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, sentPI, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}