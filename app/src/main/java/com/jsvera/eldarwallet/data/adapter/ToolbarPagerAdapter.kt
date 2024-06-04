package com.jsvera.eldarwallet.data.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ToolbarPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()

    public fun getTabTitle(position: Int): String {
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
    fun addFragment(fragment: FragmentActivity, title: String) {
        mFragmentList.add(fragment as Fragment)
        mFragmentTitleList.add(title)
    }
    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun handleAction(position: Int,actionCode:String){
        val fragment = mFragmentList[position]
        (fragment as? TabFragmentHandler)?.handleAction(actionCode)
    }
}