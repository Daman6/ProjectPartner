package com.example.projectpartner.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projectpartner.registerfragments.FragmentOne
import com.example.projectpartner.registerfragments.FragmentThree
import com.example.projectpartner.registerfragments.FragmentTwo

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
            0->{
                FragmentOne()
            }
            1->{
                FragmentTwo()
            }
            2->{
                FragmentThree()
            }
            else->{
                Fragment()
            }
        }
    }
}