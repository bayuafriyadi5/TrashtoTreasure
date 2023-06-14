package com.capstone.trashtotreasure.view.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.trashtotreasure.view.ui.metal.result.MetalDescFragment
import com.capstone.trashtotreasure.view.ui.metal.result.MetalRecommendFragment
import com.capstone.trashtotreasure.view.ui.plastic.result.PlasticDescFragment
import com.capstone.trashtotreasure.view.ui.plastic.result.PlasticRecommendFragment

class MetalPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MetalDescFragment()
            1 -> fragment = MetalRecommendFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}