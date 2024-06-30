package com.capstone.trashtotreasure.view.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityRegisSellerBinding
import com.capstone.trashtotreasure.databinding.ActivityUpdateSellerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalPagingApi
class UpdateSellerActivity : AppCompatActivity() {

    private val binding: ActivityUpdateSellerBinding by lazy {
        ActivityUpdateSellerBinding.inflate(layoutInflater)
    }

    private val profileViewModel: ProfileViewModel by viewModels()

    private var token: String = "null"
    private var selectedBank: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.loading.visibility = View.INVISIBLE

        // Set up the Spinner
        val spinner: Spinner = binding.spinnerBank
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.bank_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        // Handle Spinner selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedBank = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedBank = null
            }
        }

        profileViewModel.checkIfTokenAvailable().observe(this) { token ->
            this.token = token ?: "null"
        }

        binding.updateButton.setOnClickListener {
            val noRekening = binding.norekEditText.text.toString().trim()

            // Validate the input
            if (selectedBank.isNullOrEmpty()) {
                Toast.makeText(this, "Please select a bank", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (noRekening.isEmpty()) {
                Toast.makeText(this, "Please enter your account number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val norek = "$selectedBank-$noRekening"

            binding.loading.visibility = View.VISIBLE

            lifecycleScope.launchWhenResumed {
                profileViewModel.updateSeller(token, norek)
                    .observe(this@UpdateSellerActivity) { result ->
                        handleUpdateResult(result)
                    }
            }
        }
    }
    private fun handleUpdateResult(result: Result<Any>) {
        result.onSuccess {
            binding.loading.visibility = View.INVISIBLE
            AlertDialog.Builder(this@UpdateSellerActivity).apply {
                setTitle("Yeah!")
                setMessage("Your account has been successfully updated")
                setPositiveButton("Next") { _, _ -> finish() }
                create()
                show()
            }
        }
        result.onFailure {
            val error = it.message
            Toast.makeText(this@UpdateSellerActivity, error, Toast.LENGTH_SHORT).show()
            binding.loading.visibility = View.INVISIBLE
        }
    }
}