package com.tugasakhir.onandcafe.ui.stock.add

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tugasakhir.onandcafe.R
import com.tugasakhir.onandcafe.base.enable
import com.tugasakhir.onandcafe.base.showToast
import com.tugasakhir.onandcafe.base.visible
import com.tugasakhir.onandcafe.databinding.ActivityStockAddBinding
import com.tugasakhir.onandcafe.model.Stock
import com.tugasakhir.onandcafe.ui.ViewModelFactory
import com.tugasakhir.onandcafe.ui.stock.StockViewModel

class StockAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockAddBinding
    private lateinit var viewModel: StockViewModel

    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStockAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tbStockAdd)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[StockViewModel::class.java]
        viewModel.getIsSuccess().observe(this) {
            if (it != null) finish()
        }
        viewModel.getOnError().observe(this) {
            if (it.isNotEmpty()) showToast(this, it, Toast.LENGTH_SHORT)
        }

        val stock: Stock? = intent?.getParcelableExtra("stock")
        initUpdateUI(stock)

        binding.apply {
            btnStockSave.setOnClickListener { saveStock() }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
        }

        return true
    }

    private fun initUpdateUI(stock: Stock?) {
        binding.apply {

            if (stock != null) {
                isUpdate = true

                tilStockName.editText?.setText(stock.stockName)
                tilStockId.editText?.let {
                    it.setText(stock.id.toString())
                    it.enable(false)
                }

                tilStockMetric.editText?.setText(stock.stockMetric)
                tilStockInitial.editText?.setText(stock.stockInitialValue.toString())
                tilStockIn.editText?.setText(stock.stockIn.toString())
                tilStockOut.editText?.setText(stock.stockOut.toString())
                tilStockTotal.editText?.setText(stock.stockTotal.toString())

                btnStockDelete.visible(true)
                btnStockDelete.setOnClickListener {
                    viewModel.deleteStock(stock)
                    finish()
                }
            }
        }
    }

    private fun saveStock() {
        binding.apply {
            val stockNameEt = tilStockName.editText?.text
            val stockIdEt = tilStockId.editText?.text
            val stockMetricEt = tilStockMetric.editText?.text
            val stockInitialValueEt = tilStockInitial.editText?.text
            val stockInEt = tilStockIn.editText?.text
            val stockOutEt = tilStockOut.editText?.text
            val stockTotalEt = tilStockTotal.editText?.text

            if (stockNameEt.isNullOrEmpty()
                || stockIdEt.isNullOrEmpty()
                || stockMetricEt.isNullOrEmpty()
                || stockInitialValueEt.isNullOrEmpty()
                || stockInEt.isNullOrEmpty()
                || stockOutEt.isNullOrEmpty()
                || stockTotalEt.isNullOrEmpty()
            )
                showToast(
                    this@StockAddActivity,
                    getString(R.string.please_fill),
                    Toast.LENGTH_SHORT
                )
            else {
                val stock = Stock(
                    stockIdEt.toString().toInt(),
                    stockNameEt.toString(),
                    stockMetricEt.toString(),
                    stockInitialValueEt.toString().toInt(),
                    stockInEt.toString().toInt(),
                    stockOutEt.toString().toInt(),
                    stockTotalEt.toString().toInt()
                )

                if (isUpdate) {
                    viewModel.updateStock(stock)
                    finish()
                } else viewModel.insertStock(stock)
            }
        }
    }
}