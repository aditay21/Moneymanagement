package com.aditechnology.moneymanagement.ui.notifications

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aditechnology.moneymanagement.MainActivity
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.databinding.BottomsheetShareBackupBinding
import com.aditechnology.moneymanagement.databinding.FragmentSettingBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File


class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.textExportCsv.setOnClickListener {
            (requireActivity()as MainActivity).exportDatabaseToCSVFile()
        }
        binding.textCreateBackup.setOnClickListener {

            (requireActivity()as MainActivity).createBackup()
        }
        binding.textRestore.setOnClickListener {
            (requireActivity() as MainActivity).selectBackUpFile()
        }
        binding.textReset.setOnClickListener {
             resetAlertWarning()

        }


        return root
    }

    private fun resetAlertWarning() {
        val dialog = BottomSheetDialog(requireContext(), R.style.BaseBottomSheetDialog)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomsheetShareBackupBinding.inflate(inflater, null, false)
        binding.cardView.setBackgroundResource(R.drawable.bottom_sheet_shape)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        binding.textviewShareInfo.text = "All data will be reset!! We suggest you to make a backup first so that it can be recovered later "
        binding.buttonOk.text = "Cancel"
        binding.buttonShareInfo.text = "Reset"
        binding.buttonOk.setOnClickListener {
            dialog.dismiss()
        }
        binding.buttonShareInfo.setOnClickListener {
            (requireActivity() as MainActivity).resetDb(true)
          //  (requireActivity() as MainActivity).triggerRebirth()
            //dialog.dismiss()


        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}