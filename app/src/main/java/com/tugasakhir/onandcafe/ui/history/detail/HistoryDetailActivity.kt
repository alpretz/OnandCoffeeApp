package com.tugasakhir.onandcafe.ui.history.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugasakhir.onandcafe.R
import com.tugasakhir.onandcafe.base.showToast
import com.tugasakhir.onandcafe.base.visible
import com.tugasakhir.onandcafe.databinding.ActivityHistoryDetailBinding
import com.tugasakhir.onandcafe.model.Menu
import com.tugasakhir.onandcafe.model.OrderWithMenu
import com.tugasakhir.onandcafe.ui.ViewModelFactory
import com.tugasakhir.onandcafe.ui.history.HistoryViewModel

class HistoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryDetailBinding
    private lateinit var viewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tbHistoryDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val orderId = intent.getLongExtra("orderId", -1)
        val factory = ViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        if (orderId > -1) {
            viewModel.getOrderByOrderId(orderId).observe(this) {
                if (it != null) initUI(it)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    private fun initUI(orderWithMenu: OrderWithMenu) {
        val order = orderWithMenu.order
        val menus = orderWithMenu.menus

        val adapter = HistoryDetailAdapter(this)
        adapter.setData(menus)

        binding.apply {
            tilName.editText?.setText(order.buyerName)
            tilTableNo.editText?.setText(order.tableNo)
            tilCash.editText?.setText(order.totalPrice.toString())

            rvMenus.adapter = adapter
            rvMenus.layoutManager = LinearLayoutManager(this@HistoryDetailActivity)

            btnDone.setOnClickListener {
                viewModel.setPaymentStatus(1, order.orderId.toString())
                finish()
            }

            /// delete history using order.id
            btnDelete.setOnClickListener {
                viewModel.deleteHistory(order.orderId.toString(), order.tableNo)
                showToast(applicationContext, getString(R.string.successfully_delete), Toast.LENGTH_SHORT)
                finish()
            }

            /// edit history by passing Order and Menu object and user re-order product
            btnEdit.setOnClickListener {
                val menuList = ArrayList<Menu>(menus)
                val intent = Intent(applicationContext, HistoryEditActivity::class.java)
                intent.putExtra(HistoryEditActivity.EXTRA_ORDER, order)
                intent.putExtra(HistoryEditActivity.EXTRA_MENUS, menuList)
                startActivity(intent)
            }

            if (order.isPaid == 1) {
                btnDone.visible(false)
                btnEdit.visible(false)
            }
        }
    }


}