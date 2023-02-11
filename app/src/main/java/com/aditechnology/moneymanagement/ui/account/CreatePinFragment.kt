package com.aditechnology.moneymanagement.ui.account

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aditechnology.moneymanagement.MainApplication
import com.aditechnology.moneymanagement.R
import com.aditechnology.moneymanagement.databinding.BottomsheetRemovePinBinding
import com.aditechnology.moneymanagement.databinding.FragmentCreatePinBinding
import com.aditechnology.moneymanagement.utils.SharedPreference
import com.aditechnology.moneymanagement.utils.Utils
import com.aditechnology.moneymanagement.utils.Utils.Companion.IS_PIN_CHECK
import com.aditechnology.moneymanagement.viewmodel.AccountViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class CreatePinFragment : Fragment() {

    private var _binding: FragmentCreatePinBinding? = null
    private val accountViewModel : AccountViewModel by viewModels {
        AccountViewModel.AccountViewModelFactory((requireActivity().application as MainApplication).repository)
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRemovePin.setOnClickListener {
            removePinBottomSheet()
        }
        val sharedPref = requireActivity().getSharedPreferences("My",0)
        val exitingPin = sharedPref?.getString(Utils.SET_PIN, "")

        if (TextUtils.isEmpty(exitingPin)) {
            binding.buttonRemovePin.visibility = View.GONE
        }
        binding.buttonCreate.setOnClickListener {
            when {
                    TextUtils.isEmpty(binding.edittextSetPin.text) -> {
                        binding.edittextSetPin.error = "Please enter new pin"
                    }
                    TextUtils.isEmpty(binding.edittextReverifyPin.text) -> {
                        binding.edittextReverifyPin.error = "Please enter confirm pin"
                    }
                else -> {
                    if (binding.edittextSetPin.text.toString() != binding.edittextReverifyPin.text.toString()) {
                        binding.edittextReverifyPin.error = "New Pin and Confirm Pin not same"
                    } else {
                        if (TextUtils.isEmpty(exitingPin)) {
                            setPin()
                        } else {
                            createPinBottomSheet()
                        }
                    }
                }

            }
        }
    }

    private fun createPinBottomSheet() {
        val dialog = BottomSheetDialog(requireContext(), R.style.BaseBottomSheetDialog)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomsheetRemovePinBinding.inflate(inflater, null, false)
        binding.cardView.setBackgroundResource(R.drawable.bottom_sheet_shape)
        dialog.setCancelable(true)
        binding.buttonRemovePin.text = "Create Pin"
        binding.buttonRemovePin.setOnClickListener {
            if (TextUtils.isEmpty(binding.edittextEnterPin.text)){
                binding.edittextEnterPin.error = "Enter the pin"
            }
            else{
                setPin()
                dialog.dismiss()
            }
        }
        dialog.setContentView(binding.root)
        dialog.show()
    }
    private fun removePinBottomSheet() {
        val sharedPref = requireActivity().getSharedPreferences("My",0)
        val exitingPin = sharedPref?.getString(Utils.SET_PIN, "")

        val dialog = BottomSheetDialog(requireContext(), R.style.BaseBottomSheetDialog)
            val inflater = LayoutInflater.from(requireContext())
            val binding = BottomsheetRemovePinBinding.inflate(inflater, null, false)
            binding.cardView.setBackgroundResource(R.drawable.bottom_sheet_shape)
            dialog.setCancelable(true)

            binding.buttonRemovePin.setOnClickListener {
               if(binding.edittextEnterPin.text.toString() != exitingPin){
                    binding.edittextEnterPin.error = "Pin is invalid"
                } else{

                    val preference = SharedPreference()
                     preference.setPin(requireActivity(),"")
                    Toast.makeText(requireContext(), "Password Removed", Toast.LENGTH_SHORT).show()
                   dialog.dismiss()
                   findNavController().popBackStack()
                }
            }
            dialog.setContentView(binding.root)
            dialog.show()
    }
    private fun setPin() {
            val preference = SharedPreference()
            preference.setPin(requireActivity(),binding.edittextSetPin.text.toString())
            binding.edittextSetPin.setText("")
            binding.edittextReverifyPin.setText("")
            Toast.makeText(requireContext(), "Password Lock Created", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}