package com.tugasakhir.onandcafe.ui.menu.add

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.tugasakhir.onandcafe.R
import com.tugasakhir.onandcafe.base.showToast
import com.tugasakhir.onandcafe.base.visible
import com.tugasakhir.onandcafe.databinding.ActivityMenuAddBinding
import com.tugasakhir.onandcafe.model.Menu
import com.tugasakhir.onandcafe.ui.ViewModelFactory
import com.tugasakhir.onandcafe.ui.menu.MenuViewModel

class MenuAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuAddBinding
    private lateinit var viewModel: MenuViewModel

    private var menu: Menu? = null

    private val listMenuHistory = ArrayList<String>()

    private val autoSearchMenuHistoryAdapter by lazy {
        ArrayAdapter(binding.root.context, R.layout.list_item, listMenuHistory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbMenuAdd)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[MenuViewModel::class.java]

        menu = intent.getParcelableExtra("menu")

        if (menu != null) {
            binding.apply {
                tilMenuCategory.editText?.setText(menu!!.category)
                tilMenuName.editText?.setText(menu!!.name)
                tilMenuDesc.editText?.setText(menu!!.description)
                tilMenuPrice.editText?.setText(menu!!.price.toString())
                ckCoffee.isChecked = menu!!.isCoffee

                btnMenuDelete.visible(true)
            }
        }

        binding.etNameMenu.setAdapter(autoSearchMenuHistoryAdapter)
        binding.etNameMenu.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                getHistoryMenuName(it.toString())
            }
        }

        binding.apply {
            btnMenuSave.setOnClickListener { saveData() }
            btnMenuDelete.setOnClickListener {
                if (menu != null) viewModel.delete(menu!!)
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
        }

        return true
    }

    private fun saveData() {
        binding.apply {
            val categoryEt = tilMenuCategory.editText?.text
            val nameEt = etNameMenu.text
            val descEt = tilMenuDesc.editText?.text
            val priceEt = tilMenuPrice.editText?.text
            val coffeeCk = ckCoffee.isChecked

            if (categoryEt.isNullOrEmpty() ||
                nameEt.isNullOrEmpty() ||
                descEt.isNullOrEmpty() ||
                priceEt.isNullOrEmpty()
            ) {
                showToast(this@MenuAddActivity, getString(R.string.please_fill), Toast.LENGTH_SHORT)
            } else {
                val price = priceEt.toString()

                if (price.contains("Rp."))
                    price.removeRange(0..2)

                val id = if (menu != null) menu!!.menuId else 0

                viewModel.insert(
                    Menu(
                        id,
                        categoryEt.toString(),
                        nameEt.toString(),
                        descEt.toString(),
                        price.toLong(),
                        coffeeCk
                    )
                )

                finish()
            }
        }
    }

    private fun getHistoryMenuName(nameMenuQuery: String) {
        viewModel.getListNameMenuHistory(nameMenuQuery)?.observe(this) {
            listMenuHistory.clear()
            autoSearchMenuHistoryAdapter.clear()
            it.forEach { menu ->
                listMenuHistory.add(menu.name)
            }
            autoSearchMenuHistoryAdapter.addAll(listMenuHistory)
            binding.etNameMenu.showDropDown()
        }
    }
}