package com.example.backapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toolbar
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

        view.findViewById<ImageButton>(R.id.return_btn).visibility=View.GONE;

        view.findViewById<TextView>(R.id.toolbarText).text="BackApp";
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}