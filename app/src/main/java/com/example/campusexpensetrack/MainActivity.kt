package com.example.campusexpensetrack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.campusexpensetrack.data.local.AppDatabase
import com.example.campusexpensetrack.data.local.ExpenseEntity
import com.example.campusexpensetrack.data.local.UserProfileEntity
import com.example.campusexpensetrack.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AppDatabase
    private val studentId = "2025621102"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        initData()
        setupObservers()
        setupListeners()
    }

    private fun initData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = database.expenseDao()
            val profile = dao.getProfile(studentId)
            
            if (profile == null) {
                // Insert default profile for Rajib Shrestha
                dao.insertProfile(
                    UserProfileEntity(
                        studentId = studentId,
                        fullName = "Rajib Shrestha",
                        monthlyBudget = 500.0
                    )
                )
                
                // Insert initial expense
                dao.insertExpense(
                    ExpenseEntity(
                        studentId = studentId,
                        title = "Welcome Coffee",
                        category = "Food",
                        amount = 5.50,
                        date = "2025-05-01"
                    )
                )
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            val dao = database.expenseDao()
            
            // Observe Profile
            val profile = withContext(Dispatchers.IO) { dao.getProfile(studentId) }
            profile?.let { user ->
                binding.tvBudget.text = String.format(Locale.getDefault(), "Monthly Budget: $%.2f", user.monthlyBudget)
                
                // Observe total expenses reactively
                dao.getTotalSpent(studentId).collectLatest { totalSpent ->
                    val spent = totalSpent ?: 0.0
                    val remaining = user.monthlyBudget - spent
                    binding.tvRemaining.text = String.format(Locale.getDefault(), "Remaining: $%.2f", remaining)
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnAddExpense.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        binding.btnViewHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }
}
