package com.example.backapp

import android.app.PendingIntent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.backapp.databinding.FragmentSecondBinding
import android.content.Intent
import android.net.Uri
import android.telephony.SmsManager
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.extensions.style
import com.google.android.material.textfield.TextInputEditText

import android.content.ContentResolver

import android.database.ContentObserver
import android.os.Handler
import androidx.lifecycle.Observer


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    lateinit var co:SMSObserver;
    lateinit var contentResolver: ContentResolver;
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

        val viewModel by activityViewModels<VM>();

        val recyclerView = view.findViewById<RecyclerView>(R.id.messageList);
        smsadapter = smsAdapter(context, viewModel);
        recyclerView.adapter = smsadapter
        var layout = LinearLayoutManager(requireContext())
        layout.reverseLayout = true
        recyclerView.layoutManager = layout

        co = SMSObserver(Handler(), smsadapter, context,Uri.parse("content://sms/"));
        contentResolver = context!!.contentResolver
        contentResolver.registerContentObserver(Uri.parse("content://sms/"), true, co)

        viewModel.getSMS(context,viewModel.currentChat).observe(viewLifecycleOwner, Observer { smses ->
            smsadapter.setData(smses)
        })

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel by activityViewModels<VM>();

        hashColor(!viewModel.hashState);

        view.findViewById<ImageButton>(R.id.return_btn).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        view.findViewById<ImageButton>(R.id.sendMessageBtn).setOnClickListener {
            var text = view.findViewById<TextInputEditText>(R.id.textInput).text.toString();
            if(text != "" && text!="\n" && text.length>0){
                view.findViewById<TextInputEditText>(R.id.textInput).clearFocus();
                view.findViewById<TextInputEditText>(R.id.textInput).getText()?.clear();
                smsTo(text);
            }
        }

        view.findViewById<ImageButton>(R.id.hashButton).setOnClickListener {
            hashColor(viewModel.hashState);
            viewModel.hashState=!viewModel.hashState;
        }

        view.findViewById<TextView>(R.id.toolbarText).text=viewModel.currentChat;
    }

    private fun hashColor(actual:Boolean){
        if(actual){
            view?.findViewById<ImageButton>(R.id.hashButton)?.style(R.style.hashOff)
        }
        else{
            view?.findViewById<ImageButton>(R.id.hashButton)?.style(R.style.hashOn)
        }
    }

    private fun superRoman(text: String, phone:String):String{
        var key:String;
        if(phone[0]=='+')
            key=phone.substring(3);
        else
            key=phone;

        var ret = "@\$!";
        var ind=0;
        for(i in text){
            ret += Regex.escapeReplacement((i - key[ind].toString().toInt()).toString());

            ind += 1;
            ind %= key.length;
        }
        return ret;
    }

    fun smsTo(message: String){
        val viewModel by activityViewModels<VM>();
        if(viewModel.hashState)
            sendSMS(viewModel.currentChat,superRoman(message,viewModel.myPhone));
        else
            sendSMS(viewModel.currentChat,message);

    }

    private fun sendSMS(phoneNumber: String, message: String) {
        val sentPI: PendingIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"), 0)
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, sentPI, null)
    }

    override fun onPause() {
        super.onPause()
        contentResolver.unregisterContentObserver(co)
    }

    override fun onResume() {
        super.onResume()
        contentResolver.registerContentObserver(Uri.parse("content://sms/"), true, co)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        contentResolver.unregisterContentObserver(co)
    }
}