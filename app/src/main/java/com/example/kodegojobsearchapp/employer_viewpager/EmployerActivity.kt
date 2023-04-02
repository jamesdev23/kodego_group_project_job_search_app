package com.example.kodegojobsearchapp.employer_viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.kodegojobsearchapp.adapter.FragmentAdapter
import com.example.kodegojobsearchapp.adapter.ViewPagerAdapter
import com.example.kodegojobsearchapp.applicant_viewpager.*
import com.example.kodegojobsearchapp.databinding.ActivityEmployerBinding
import com.google.android.material.tabs.TabLayoutMediator

class EmployerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmployerBinding
    private lateinit var fragmentAdapter: FragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployerBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        fragmentAdapter = FragmentAdapter(supportFragmentManager, lifecycle)
//        fragmentAdapter.addFragment(EmployerHomeFragment())
//        fragmentAdapter.addFragment(EmployerJobsFragment())
//        fragmentAdapter.addFragment(EmployerProfileFragment())
//        fragmentAdapter.addFragment(AccountFragment())
//
//        with(binding.viewPager2) {
//            orientation = ViewPager2.ORIENTATION_HORIZONTAL
//            adapter = fragmentAdapter
//        }
//
//        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
//            var text: String = "Unknown"
//            with(fragmentAdapter.fragmentList[position]) {
//                arguments?.let {
//                    text = it.getString(FragmentKeys.TabName) ?: "Unknown"
//                }
//            }
//            tab.text = text
//        }.attach()

        // new adapter for viewpager

        val fragmentList: ArrayList<Fragment> = ArrayList()

        fragmentList.add(EmployerHomeFragment())
        fragmentList.add(EmployerJobsFragment())
        fragmentList.add(EmployerProfileFragment())
        fragmentList.add(AccountFragment())

        val viewPagerAdapter = ViewPagerAdapter(fragmentList,this, this)
        binding.viewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            val tabView = viewPagerAdapter.getTabView(position, binding.viewPager2)
            tab.customView = tabView
        }.attach()
    }
}