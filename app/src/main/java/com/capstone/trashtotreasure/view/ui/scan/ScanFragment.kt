package com.capstone.trashtotreasure.view.ui.scan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.capstone.trashtotreasure.databinding.FragmentScanBinding
import com.capstone.trashtotreasure.view.ui.cloth.ClothSnapActivity
import com.capstone.trashtotreasure.view.ui.glass.GlassSnapActivity
import com.capstone.trashtotreasure.view.ui.metal.MetalSnapActivity
import com.capstone.trashtotreasure.view.ui.plastic.PlasticSnapActivity

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.scanPlastik.setOnClickListener {
            val intent = Intent(requireContext(), PlasticSnapActivity::class.java)
            startActivity(intent)
        }

        binding.scanMetal.setOnClickListener {
            val intent = Intent(requireContext(), MetalSnapActivity::class.java)
            startActivity(intent)
        }

        binding.scanKaca.setOnClickListener {
            val intent = Intent(requireContext(), GlassSnapActivity::class.java)
            startActivity(intent)
        }
        binding.scanCloth.setOnClickListener {
            val intent = Intent(requireContext(), ClothSnapActivity::class.java)
            startActivity(intent)
        }
    }
}