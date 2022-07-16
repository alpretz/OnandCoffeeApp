package com.tugasakhir.onandcafe.ui.history.detail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugasakhir.onandcafe.R
import com.tugasakhir.onandcafe.base.showToast
import com.tugasakhir.onandcafe.databinding.ActivityHistoryEditBinding
import com.tugasakhir.onandcafe.model.Menu
import com.tugasakhir.onandcafe.model.Order
import com.tugasakhir.onandcafe.ui.ViewModelFactory
import com.tugasakhir.onandcafe.ui.menu.MenuActivity
import com.tugasakhir.onandcafe.ui.order.OrderAdapter
import com.tugasakhir.onandcafe.ui.order.OrderViewModel
import java.util.*
import kotlin.collections.ArrayList

class HistoryEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryEditBinding
    private lateinit var adapter: OrderAdapter
    private var order: Order? = null
    private var menu: ArrayList<Menu>? = null
    private var etCash: EditText? = null

    /// activity ini kurang lebih mirip seperti Order Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        order = intent.getParcelableExtra(EXTRA_ORDER)
        menu = intent.getParcelableArrayListExtra(EXTRA_MENUS)

        val factory = ViewModelFactory(this.application)
        val viewModel = ViewModelProvider(this, factory)[OrderViewModel::class.java]

        etCash = binding.tilCash.editText
        adapter = OrderAdapter(applicationContext) { totalPrice, isAdd ->
            var currentTotalPrice = etCash?.text.toString().toLong()

            if (isAdd) {
                currentTotalPrice += totalPrice
            } else {
                currentTotalPrice -= totalPrice
            }

            etCash?.setText(currentTotalPrice.toString())
        }

        binding.apply {
            rvMenus.adapter = adapter
            rvMenus.layoutManager = LinearLayoutManager(applicationContext)
            menu?.forEach { menuIdx ->
                adapter.addData(menuIdx)
            }
            tilName.editText?.setText(order?.buyerName)
            tilTableNo.editText?.setText(order?.tableNo)
            tilCash.editText?.setText(order?.totalPrice.toString())
            backButton.setOnClickListener {
                onBackPressed()
            }

            btnAddMenu.setOnClickListener {
                val i = Intent(applicationContext, MenuActivity::class.java)
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
                    showToast(
                        applicationContext,
                        getString(R.string.please_fill),
                        Toast.LENGTH_SHORT
                    )
                } else if (adapter.getMenus().isEmpty()) {
                    showToast(
                        applicationContext,
                        getString(R.string.order_empty),
                        Toast.LENGTH_SHORT
                    )
                } else {


                    val orderNew = Order(
                        order?.orderId!!,
                        "",
                        buyerName.toString(),
                        "Tunai",
                        Date(),
                        priceTotal.toString().toLong(),
                        tableNo.toString(),
                        0
                    )

                    /// update order same like insert new order but different using query
                    viewModel.updateOrder(orderNew, applicationContext)
                        .observe(this@HistoryEditActivity) {

                            adapter.getMenus().forEach { menu ->
                                /// looping untuk mengupdate menu
                                /// todo : disini saya masih tidak mengetahui bagaimana query untuk menghapus menu orderan, jadi ketika user ingin menghapus menu, silahkan hapus orderan saja, jangan hapus melalui edit, karena belum berhasil menghapus menu melalui edit order
                                viewModel
                                    .updateOrderWithMenu(
                                        orderNew.orderId,
                                        menu.menuId,
                                        tableNo.toString().toInt(),
                                        order!!.tableNo,
                                        applicationContext,
                                    )
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

    companion object {
        const val EXTRA_ORDER = "order"
        const val EXTRA_MENUS = "menu"
    }
}