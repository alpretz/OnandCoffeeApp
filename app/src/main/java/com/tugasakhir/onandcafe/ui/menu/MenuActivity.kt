package com.tugasakhir.onandcafe.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugasakhir.onandcafe.R
import com.tugasakhir.onandcafe.databinding.ActivityMenuBinding
import com.tugasakhir.onandcafe.ui.ViewModelFactory
import com.tugasakhir.onandcafe.ui.menu.add.MenuAddActivity

class   MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var viewModel: MenuViewModel
    private lateinit var adapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tbMenu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = MenuAdapter(this@MenuActivity) {
            val i = Intent(this, MenuAddActivity::class.java)
            i.putExtra("menu", it)
            startActivity(i)
        }

        val factory = ViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[MenuViewModel::class.java]
        getData(true)

        binding.apply {
            rvMenu.adapter = adapter
            rvMenu.layoutManager = LinearLayoutManager(this@MenuActivity)

            btnCoffee.setBackgroundColor(ContextCompat.getColor(this@MenuActivity, R.color.ash))

            btnCoffee.setOnClickListener {
                btnCoffee.setBackgroundColor(ContextCompat.getColor(this@MenuActivity, R.color.ash))
                btnNonCoffee.setBackgroundColor(
                    ContextCompat.getColor(
                        this@MenuActivity,
                        R.color.purple_500
                    )
                )

                getData(true)
            }

            btnNonCoffee.setOnClickListener {
                btnCoffee.setBackgroundColor(
                    ContextCompat.getColor(
                        this@MenuActivity,
                        R.color.purple_500
                    )
                )
                btnNonCoffee.setBackgroundColor(
                    ContextCompat.getColor(
                        this@MenuActivity,
                        R.color.ash
                    )
                )

                getData(false)
            }

            fabMenuAdd.setOnClickListener {
                val i = Intent()
                i.putExtra("menus", adapter.getSelectedData())
                setResult(RESULT_OK, i)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        if (viewModel.getIsAdmin())
            menuInflater.inflate(R.menu.menu_toolbar, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            val i = Intent(this, MenuAddActivity::class.java)
            startActivity(i)
        } else if (item.itemId == android.R.id.home) {
            finish()
        }

        return true
    }

    private fun getData(isCoffee: Boolean) {

        if (isCoffee) viewModel.getAll()?.observe(this) {
            adapter.setData(it)
        } else {
            viewModel.getAllNonCoffee()?.observe(this) {
                adapter.setData(it)
            }
        }
    }
}