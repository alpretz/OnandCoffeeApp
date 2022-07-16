package com.tugasakhir.onandcafe.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.tugasakhir.onandcafe.R
import com.tugasakhir.onandcafe.base.showToast
import com.tugasakhir.onandcafe.databinding.ActivityHomeBinding
import com.tugasakhir.onandcafe.ui.ViewModelFactory
import com.tugasakhir.onandcafe.ui.history.HistoryFragment
import com.tugasakhir.onandcafe.ui.login.LoginActivity
import com.tugasakhir.onandcafe.ui.order.OrderFragment
import com.tugasakhir.onandcafe.ui.stock.StockFragment
import com.tugasakhir.onandcafe.ui.table.TableFragment

class HomeActivity : AppCompatActivity() {
    private var isAdmin = false
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appbar.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.appbar.toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val factory = ViewModelFactory(application)
        val viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        viewModel.getUserById()
        viewModel.getIsSuccess().observe(this) {
            val username = navView.findViewById<TextView>(R.id.user_email)
            username.text = it.username

            /// todo : jika ingin menjadi admin di awal, silahkan blok baris ke 58 & unblok baris 59
            isAdmin = it.isAdmin
            //isAdmin = true
            navView.menu.clear()

            if (isAdmin) navView.inflateMenu(R.menu.drawer_menu)
            else navView.inflateMenu(R.menu.drawer_menu_barista)

            navView.setCheckedItem(R.id.menu_meja)
        }

        changeFragment(TableFragment(), R.string.meja)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_meja -> changeFragment(TableFragment(), R.string.meja)
                R.id.menu_history -> changeFragment(HistoryFragment(), R.string.history)
                R.id.menu_category -> changeFragment(OrderFragment(), R.string.menu_order)
                R.id.menu_stock -> changeFragment(StockFragment(), R.string.goods_stock)
                R.id.menu_logout -> {
                    viewModel.logout()

                    val i = Intent(this, LoginActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(i)

                    finish()
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        doubleBackToExitPressedOnce = true
        showToast(this, getString(R.string.press_back_to_exit), Toast.LENGTH_SHORT)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                doubleBackToExitPressedOnce = false
            }, 2000
        )
    }

    fun changeFragment(fragment: Fragment, title: Int) {
        supportActionBar?.title = getString(title)
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit()
    }
}