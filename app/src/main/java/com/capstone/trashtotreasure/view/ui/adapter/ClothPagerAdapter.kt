package com.capstone.trashtotreasure.view.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.trashtotreasure.view.ui.cloth.result.ClothDescFragment
import com.capstone.trashtotreasure.view.ui.cloth.result.ClothRecommendFragment
import com.capstone.trashtotreasure.view.ui.glass.result.GlassDescFragment
import com.capstone.trashtotreasure.view.ui.glass.result.GlassRecommendFragment



class ClothPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ClothDescFragment()
            1 -> fragment = ClothRecommendFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}