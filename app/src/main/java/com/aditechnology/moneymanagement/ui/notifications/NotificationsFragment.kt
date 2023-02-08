package com.aditechnology.moneymanagement.ui.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aditechnology.moneymanagement.MainActivity
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.databinding.FragmentNotificationsBinding
import com.aditechnology.moneymanagement.models.AccountTable
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null



    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.textExportCsv.setOnClickListener {
            (requireActivity()as MainActivity).exportDatabaseToCSVFile()
        }


        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}