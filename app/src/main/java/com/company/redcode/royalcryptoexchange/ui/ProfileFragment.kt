package com.company.redcode.royalcryptoexchange.ui


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.adapter.UserBankAdapater
import com.company.redcode.royalcryptoexchange.models.Bank

class ProfileFragment : Fragment() {

    var btn_add_bank: ImageButton? = null
    var btn_add_img: ImageButton? = null
    var bank_recycler_view: RecyclerView? = null
    val SELECT_PICTURE: Int = 4
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_profile, container, false)

        initView(view)
        return view
    }

    private fun initView(view: View?) {

        btn_add_bank = view!!.findViewById(R.id.btn_add_bank)
        btn_add_img = view!!.findViewById(R.id.btn_add_img)

        btn_add_bank!!.setOnClickListener {
            showTradeDialog()
        }

        btn_add_img!!.setOnClickListener {
            var intent = Intent()
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), SELECT_PICTURE)

        }

    }


    private fun showTradeDialog() {

        val view: View = LayoutInflater.from(activity!!).inflate(R.layout.dialog_new_bank, null)
        val alertBox = android.support.v7.app.AlertDialog.Builder(activity!!)
        alertBox.setView(view)
        alertBox.setCancelable(false)
        val dialog = alertBox.create()
        val btnCancel: Button = view.findViewById(R.id.btn_cancel)
        val btnSave: Button = view.findViewById(R.id.btn_save)
        bank_recycler_view = view!!.findViewById(R.id.bank_recycler_view)



         var list = ArrayList<Bank>()
         list.add(Bank("ahmed", "Bank Transfer"))
         list.add(Bank("ahmed", "Bank Transfer"))
         list.add(Bank("ahmed", "Bank Transfer"))


         var adapter = UserBankAdapater(activity!!, list) { position ->
             showTradeDialog()
         }
         var layout = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
         bank_recycler_view!!.layoutManager = layout
         bank_recycler_view!!.adapter = adapter


        btnSave.setOnClickListener {
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {

            dialog.dismiss()
        }
        dialog.show()
    }

}
