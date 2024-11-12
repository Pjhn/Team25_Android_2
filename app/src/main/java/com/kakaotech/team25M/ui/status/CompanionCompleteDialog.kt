package com.kakaotech.team25M.ui.status

import android.app.Dialog
import android.content.Intent
import com.kakaotech.team25M.domain.model.ReservationInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.kakaotech.team25M.databinding.ViewCompanionCompleteDialogBinding


class CompanionCompleteDialog :
    DialogFragment() {
    private var _binding: ViewCompanionCompleteDialogBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val RESERVATION_INFO = "reservation_info"

        fun newInstance(reservationInfo: ReservationInfo): CompanionCompleteDialog {
            val fragment = CompanionCompleteDialog()
            val args = Bundle().apply {
                putParcelable(RESERVATION_INFO, reservationInfo)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        _binding = ViewCompanionCompleteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDialogBtnListener()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        isCancelable = true
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setDialogBtnListener() {
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }

        binding.navigateToReportWriteBtn.setOnClickListener {
            val reservationInfo = arguments?.getParcelable<ReservationInfo>(RESERVATION_INFO)
            if (reservationInfo != null) {
                Log.d("CompanionCompleteDialog", "ReservationInfo: $reservationInfo")
                val intent = Intent(requireContext(), ReservationReportActivity::class.java).apply {
                    putExtra("ReservationInfo", reservationInfo)
                }
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Reservation info not available", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
