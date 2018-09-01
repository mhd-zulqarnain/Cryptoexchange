package com.company.redcode.royalcryptoexchange.ui

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.company.redcode.royalcryptoexchange.R


class TradeFragment : Fragment() {

    var view_pager: ViewPager? = null
    var view_pager_tab: TabLayout? = null
    var adapter : MyPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_trade, container, false)


        initView(view)
        return view
    }

    private fun initView(view: View) {
        view_pager = view.findViewById(R.id.view_pager)
        view_pager_tab = view.findViewById(R.id.view_pager_tab)
        view_pager_tab!!.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        adapter = MyPagerAdapter(childFragmentManager)
        view_pager!!.adapter = adapter
    }

    inner class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(pos: Int): Fragment {
            when (pos) {

                0 -> return BuyFragment()
                1 -> return SellFragment()

            }
            return BuyFragment()
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return if (position == 0)
                "Buy "
            else
                "Sell"
        }
    }

}
