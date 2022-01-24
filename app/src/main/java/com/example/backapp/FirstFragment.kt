package com.example.backapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import android.text.InputType.TYPE_CLASS_PHONE
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.backapp.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    lateinit var layoutMenager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            //findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        val viewModel by activityViewModels<VM>();

        layoutMenager=LinearLayoutManager(context)
        var contacts=viewModel.getContacts(context);

        view.findViewById<RecyclerView>(R.id.contactsList).apply {
            adapter=contactsAdapter(contacts, viewModel)
            layoutManager=layoutMenager
        }

        if(viewModel.myPhone == ""){
            val mPhoneNumber = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            viewModel.myPhone = mPhoneNumber.line1Number;
        }


        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            var builder = AlertDialog.Builder(context);
            builder.setTitle("Nowa wiadomość");
            builder.setMessage("Podaj numer telefonu:");
            val input = EditText(context)
            input.inputType=TYPE_CLASS_PHONE;
            builder.setView(input)
            builder.setPositiveButton("Ok") { dialog, which ->
                var number = input.text.toString();
                if(number.length>=9) {
                    viewModel.currentChat = number;
                    findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                }
                else{
                    Toast.makeText(context,"Wprowadź poprawny numer",Toast.LENGTH_LONG).show();
                }
            }
            builder.setNegativeButton("Anuluj"){ dialog, which ->
                //nic
            }
            builder.show()
        }

        view.findViewById<ImageButton>(R.id.return_btn).visibility=View.GONE;

        view.findViewById<TextView>(R.id.toolbarText).text="BackApp";
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}