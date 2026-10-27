package com.example.campusexpensetrack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.campusexpensetrack.adapter.ExpenseAdapter
import com.example.campusexpensetrack.data.local.AppDatabase
import com.example.campusexpensetrack.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Expense History"

        database = AppDatabase.getDatabase(this)
        setupRecyclerView()
        observeExpenses()
    }

    private fun setupRecyclerView() {
        expenseAdapter = ExpenseAdapter()
        binding.rvExpenses.adapter = expenseAdapter
    }

    private fun observeExpenses() {
        lifecycleScope.launch {
            database.expenseDao().getAllExpenses().collectLatest { expenses ->
                expenseAdapter.submitList(expenses)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
