package com.kakaotech.team25M.ui.status

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kakaotech.team25M.R
import com.kakaotech.team25M.data.util.DateFormatter
import com.kakaotech.team25M.databinding.ActivityReservationRejectBinding
import com.kakaotech.team25M.domain.model.ReservationInfo
import com.kakaotech.team25M.domain.model.ReservationStatus.*

class ReservationRejectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationRejectBinding
    private var reservationInfo: ReservationInfo? = null
    private val reservationRejectViewModel: ReservationRejectViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReservationRejectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigateToPrevious()
        setReservationInfo()
        setRejectReasonDropDown()
        setReservationRejectBtnListener()
    }

    private fun setReservationInfo() {
        reservationInfo = intent.getParcelableExtra("ReservationInfo")

        reservationInfo?.let { reservationInfo ->
            binding.userNameTextView.text = reservationInfo.patient.patientName
            binding.reservationDateTextView.text = DateFormatter.formatDate(reservationInfo.reservationDate)
        }
    }

    private fun setRejectReasonDropDown() {
        val rejectReasonOptions = resources.getStringArray(R.array.reservation_reject_option)

        val arrayAdapter = ArrayAdapter(this, R.layout.item_dropdown, rejectReasonOptions)
        binding.reservationRejectReasonAutoCompleteTextView.setAdapter(arrayAdapter)

        binding.reservationRejectReasonAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val reservationRejectReason = parent.getItemAtPosition(position).toString()
        }
    }

    private fun setReservationRejectBtnListener() {
        binding.reservationRejectBtn.setOnClickListener {
            val rejectReason = binding.reservationRejectReasonAutoCompleteTextView.text

            if (rejectReason.isEmpty()) {
                Toast.makeText(this, "거절 사유를 선택해 주세요(필수)", Toast.LENGTH_SHORT).show()
            } else {
                reservationInfo?.reservationId?.let { reservationId ->
                    reservationRejectViewModel.changeReservation( reservationId, 취소 )
                }
                finish()
            }
        }
    }

    private fun navigateToPrevious() {
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
