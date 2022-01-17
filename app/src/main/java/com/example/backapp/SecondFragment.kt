package com.example.backapp

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

import android.content.pm.PackageManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second,container,false)
        val listView: ListView = view.findViewById(R.id.messageList)

        val aa = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_expandable_list_item_1, (activity as MainActivity).getSMS())
        listView.adapter = aa
        /**
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root*/
        return view


    }

    lateinit var lectureslayoutMenager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<RecyclerView>(R.id.messageList).apply {
            adapter=smsAdapter(smses)
            layoutManager=lectureslayoutMenager
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}