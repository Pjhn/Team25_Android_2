package com.example.team25.ui.status

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.team25.R
import com.example.team25.databinding.ActivityReservationReportBinding
import com.example.team25.domain.model.ReservationInfo


class ReservationReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationReportBinding
    private var reservationInfo: ReservationInfo? = null
    private var reservationId: String = ""
    private val reservationReportViewModel: ReservationReportViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReservationReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigateToPrevious()
        setReservationInfo()
    }

    private fun navigateToPrevious() {
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setReservationInfo() {
        reservationInfo = intent.getParcelableExtra("ReservationInfo")

        reservationInfo?.let { reservationInfo ->
            reservationId = reservationInfo.reservationId
        }
    }

    private fun setClickListener() {
        binding.mealBeforeBtn.setOnClickListener {
            updateMealTimeSelection(true)
        }
        binding.mealAfterBtn.setOnClickListener {
            updateMealTimeSelection(false)
        }

        binding.time30minBtn.setOnClickListener {
            updateTimeSelection(true)
        }
        binding.time1hourBtn.setOnClickListener {
            updateTimeSelection(false)
        }

        binding.morningBtn.setOnClickListener {
            toggleTimeOfDaySelection(binding.morningBtn, "아침")
        }
        binding.lunchBtn.setOnClickListener {
            toggleTimeOfDaySelection(binding.lunchBtn, "점심")
        }
        binding.dinnerBtn.setOnClickListener {
            toggleTimeOfDaySelection(binding.dinnerBtn, "저녁")
        }

        binding.submitReportBtn.setOnClickListener {
            if(isReportInfoValid()){
                reservationReportViewModel.updateDoctorSummary(binding.summaryEditTextView.text.toString())
                reservationReportViewModel.updateFrequency(binding.frequencyEditTextView.text.toString().toInt())
                reservationReportViewModel.postReport(reservationId, reservationReportViewModel.reportDto.value)
            }
        }
    }

    private var isMealBeforeSelected = true
    private fun updateMealTimeSelection(isBefore: Boolean) {
        if (isBefore) {
            binding.mealBeforeBtn.setBackgroundResource(R.drawable.purple_btn_box)
            binding.mealAfterBtn.setBackgroundResource(R.drawable.light_purple_btn_box)
        } else {
            binding.mealBeforeBtn.setBackgroundResource(R.drawable.light_purple_btn_box)
            binding.mealAfterBtn.setBackgroundResource(R.drawable.purple_btn_box)
        }
        updateMedicineTime()
    }

    private var isTime30MinSelected = true
    private fun updateTimeSelection(is30Min: Boolean) {
        isTime30MinSelected = is30Min
        if (is30Min) {
            binding.time30minBtn.setBackgroundResource(R.drawable.purple_btn_box)
            binding.time1hourBtn.setBackgroundResource(R.drawable.light_purple_btn_box)
        } else {
            binding.time30minBtn.setBackgroundResource(R.drawable.light_purple_btn_box)
            binding.time1hourBtn.setBackgroundResource(R.drawable.purple_btn_box)
        }
        updateMedicineTime()
    }

    private fun updateMedicineTime() {
        val mealTime = if (isMealBeforeSelected) "식전" else "식후"
        val time = if (isTime30MinSelected) "30분" else "1시간"
        val medicineTime = "$mealTime $time"
        reservationReportViewModel.updateMedicineTime(medicineTime)
    }

    private val selectedTimeOfDays = mutableSetOf<String>()
    private fun toggleTimeOfDaySelection(button: View, timeOfDay: String) {
        if (selectedTimeOfDays.contains(timeOfDay)) {
            button.setBackgroundResource(R.drawable.light_purple_btn_box)
            selectedTimeOfDays.remove(timeOfDay)
        } else {
            button.setBackgroundResource(R.drawable.purple_btn_box)
            selectedTimeOfDays.add(timeOfDay)
        }
        reservationReportViewModel.updateTimeOfDays(selectedTimeOfDays.joinToString(" "))
    }

    private fun isReportInfoValid(): Boolean{
        val medicineTime = reservationReportViewModel.reportDto.value.medicineTime
        val timeOfDays = reservationReportViewModel.reportDto.value.timeOfDays

        if (medicineTime?.isEmpty() == true) {
            Toast.makeText(this, "복약 시간(식전, 식후)을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (timeOfDays?.isEmpty() == true) {
            Toast.makeText(this, "복약 시간(30분, 1시간)을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        if(binding.summaryEditTextView.text.isEmpty()) {
            Toast.makeText(this, "의사소견을 작성해주세요", Toast.LENGTH_SHORT).show()
            return false
        }

        if(binding.frequencyEditTextView.text.isEmpty()) {
            Toast.makeText(this, "횟수를 설정해주세요", Toast.LENGTH_SHORT).show()
            return false
        }

        val frequencyText = binding.frequencyEditTextView.text.toString()
        val frequency = frequencyText.toIntOrNull()
        if (frequency == null){
            Toast.makeText(this, "횟수를 설정해주세요", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
