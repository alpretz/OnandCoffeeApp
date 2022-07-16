package com.tugasakhir.onandcafe.ui.order

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugasakhir.onandcafe.R
import com.tugasakhir.onandcafe.base.BaseFragment
import com.tugasakhir.onandcafe.base.showToast
import com.tugasakhir.onandcafe.databinding.FragmentOrderBinding
import com.tugasakhir.onandcafe.model.Menu
import com.tugasakhir.onandcafe.model.Order
import com.tugasakhir.onandcafe.ui.ViewModelFactory
import com.tugasakhir.onandcafe.ui.home.HomeActivity
import com.tugasakhir.onandcafe.ui.menu.MenuActivity
import java.util.*

class OrderFragment : BaseFragment<FragmentOrderBinding>() {
    private lateinit var adapter: OrderAdapter
    private var etCash: EditText? = null

    override fun getViewBinding(): FragmentOrderBinding =
        FragmentOrderBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory(requireActivity().application)
        val viewModel = ViewModelProvider(this, factory)[OrderViewModel::class.java]

        etCash = binding.tilCash.editText
        adapter = OrderAdapter(requireContext()) { totalPrice, isAdd ->
            var currentTotalPrice = etCash?.text.toString().toLong()

            if (isAdd) {
                currentTotalPrice += totalPrice
            } else {
                currentTotalPrice -= totalPrice
            }

            etCash?.setText(currentTotalPrice.toString())
        }

        viewModel.getOnError().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) showToast(requireContext(), it, Toast.LENGTH_SHORT)
        }
        viewModel.getIsSuccess().observe(viewLifecycleOwner) {
            if (it != null) {
                val activity = activity as HomeActivity
                activity.changeFragment(OrderFragment(), R.string.menu_order)
            }
        }

        binding.apply {
            rvMenus.adapter = adapter
            rvMenus.layoutManager = LinearLayoutManager(requireContext())

            btnAddMenu.setOnClickListener {
                val i = Intent(requireContext(), MenuActivity::class.java)
                result.launch(i)
            }

            btnOrder.setOnClickListener {
                val buyerName = tilName.editText?.text
                val priceTotal = tilCash.editText?.text
                val tableNo = tilTableNo.editText?.text

                if (buyerName.isNullOrEmpty() ||
                    priceTotal.isNullOrEmpty() ||
                    tableNo.isNullOrEmpty()
                ) {
                    showToast(requireContext(), getString(R.string.please_fill), Toast.LENGTH_SHORT)
                } else if (adapter.getMenus().isEmpty()) {
                    showToast(requireContext(), getString(R.string.order_empty), Toast.LENGTH_SHORT)
                } else {

                    val order = Order(
                        0,
                        "",
                        buyerName.toString(),
                        "Tunai",
                        Date(),
                        priceTotal.toString().toLong(),
                        tableNo.toString(),
                        0
                    )

                    viewModel.insertOrder(order).observe(viewLifecycleOwner) {
                        adapter.getMenus().forEach { menu ->
                            viewModel
                                .insertOrderWithMenu(it, menu.menuId, tableNo.toString().toInt())
                        }
                    }
                }
            }
        }
    }

    private val result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val menus = it.data?.getParcelableArrayExtra("menus")

                menus?.forEach { menu ->
                    menu as Menu

                    val datas = adapter.getIds()

                    if (!datas.contains(menu.menuId)) {
                        adapter.addData(menu)

                        if (etCash?.text.isNullOrEmpty()) {
                            etCash?.setText(menu.price.toString())
                        } else {
                            var currentValue = etCash?.text.toString().toLong()
                            currentValue += menu.price
                            etCash?.setText(currentValue.toString())
                        }
                    }
                }
            }
        }

}