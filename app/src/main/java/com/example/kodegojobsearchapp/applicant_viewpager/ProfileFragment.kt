package com.example.kodegojobsearchapp.applicant_viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kodegojobsearchapp.R
import com.example.kodegojobsearchapp.databinding.FragmentHomeBinding
import com.example.kodegojobsearchapp.databinding.FragmentProfileBinding

// TODO: applicant profile implementation. hint: grab info from firebase db
// TODO: #2: add prompt to create/fill up profile info (implementation is up to you) -nico

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    init {
        if(this.arguments == null) {
            getTabInfo()
        }
    }
    private fun getTabInfo(){
        this.arguments = Bundle().apply {
            putString(FragmentKeys.TabName, "Profile")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // code goes here

    }

    override fun onDestroy() {
        super.onDestroy()

    }

}