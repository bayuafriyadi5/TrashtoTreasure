package com.capstone.trashtotreasure.view.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.trashtotreasure.view.ui.home.HomeFragment
import com.capstone.trashtotreasure.view.ui.plastic.result.PlasticDescFragment
import com.capstone.trashtotreasure.view.ui.plastic.result.PlasticRecommendFragment
import com.capstone.trashtotreasure.view.ui.profile.ProfileFragment

class PlasticPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = PlasticDescFragment()
            1 -> fragment = PlasticRecommendFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}