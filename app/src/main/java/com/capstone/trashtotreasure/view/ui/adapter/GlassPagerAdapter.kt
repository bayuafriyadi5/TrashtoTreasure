package com.capstone.trashtotreasure.view.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.trashtotreasure.view.ui.glass.result.GlassDescFragment
import com.capstone.trashtotreasure.view.ui.glass.result.GlassRecommendFragment
import com.capstone.trashtotreasure.view.ui.metal.result.MetalDescFragment
import com.capstone.trashtotreasure.view.ui.metal.result.MetalRecommendFragment

class GlassPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = GlassDescFragment()
            1 -> fragment = GlassRecommendFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}