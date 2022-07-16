package com.tugasakhir.onandcafe.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.tugasakhir.onandcafe.db.daos.*
import com.tugasakhir.onandcafe.model.*

@Database(
    entities = [User::class, Table::class, Stock::class, Menu::class, Order::class, OrderMenuCrossRef::class],
    version = 2
)
abstract class MainDb : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val tableDao: TableDao
    abstract val stockDao: StockDao
    abstract val menuDao: MenuDao
    abstract val orderWithMenuDao: OrderWithMenuDao

    companion object {
        @Volatile
        private var db: MainDb? = null

        fun getDb(application: Application): MainDb? {
            if (db == null) {
                synchronized(MainDb::class.java) {
                    if (db == null) {
                        db = Room.databaseBuilder(
                            application.applicationContext,
                            MainDb::class.java, "main_db"
                        )
                            .addMigrations(MIGRATION_1_2)
                            .build()
                    }
                }
            }

            return db
        }

        private val MIGRATION_1_2 = Migration(1, 2) {
            it.execSQL("ALTER TABLE 'order' ADD 'isPaid' INTEGER NOT NULL DEFAULT 0")
        }
    }
}