package com.kakaotech.team25M.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kakaotech.team25M.R
import com.kakaotech.team25M.data.network.dto.DaySchedule
import com.kakaotech.team25M.databinding.FragmentEditWorkTimeBinding
import com.kakaotech.team25M.utils.DropdownUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditWorkTimeFragment : Fragment() {
    private var _binding: FragmentEditWorkTimeBinding? = null
    private val binding get() = _binding!!
    private val managerInformationViewModel: ManagerInformationViewModel by activityViewModels()
    private lateinit var dayButtonLayoutPairs: Map<View, View>
    private lateinit var daySchedules: DaySchedule
    private lateinit var autoCompleteTextViews : MutableList<AutoCompleteTextView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditWorkTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initializeAutoCompleteTextViews()
        initializeDayLayoutPairs()

        daySchedules = DaySchedule()
        setupDropdowns()
        activateTimeRange()
        setupNavigation()
    }

    private fun activateTimeRange() {
        dayButtonLayoutPairs.forEach { (button, layout) ->
            button.setOnClickListener {
                if (layout.visibility == View.VISIBLE) {
                    layout.visibility = View.GONE
                    button.setBackgroundResource(R.drawable.before_select_day_of_week)
                    (button as TextView).setTextColor(
                        ContextCompat.getColor(button.context, R.color.blue)
                    )

                    resetDayTime(button.tag as? String)
                } else {
                    layout.visibility = View.VISIBLE
                    button.setBackgroundResource(R.drawable.after_select_day_of_week)
                    (button as TextView).setTextColor(
                        ContextCompat.getColor(button.context, R.color.white)
                    )
                }
            }
        }
    }

    private fun initializeAutoCompleteTextViews() {
        autoCompleteTextViews = mutableListOf(
            binding.mondayStartTimeAutoCompleteTextView,
            binding.mondayEmdTimeAutoCompleteTextView,
            binding.tuesdayStartTimeAutoCompleteTextView,
            binding.tuesEmdTimeAutoCompleteTextView,
            binding.wednesdayStartTimeAutoCompleteTextView,
            binding.wednesdayEmdTimeAutoCompleteTextView,
            binding.thursdayStartTimeAutoCompleteTextView,
            binding.thursdayEmdTimeAutoCompleteTextView,
            binding.fridayStartTimeAutoCompleteTextView,
            binding.fridayEmdTimeAutoCompleteTextView,
            binding.saturdayStartTimeAutoCompleteTextView,
            binding.saturdayEmdTimeAutoCompleteTextView,
            binding.sundayStartTimeAutoCompleteTextView,
            binding.sundayEmdTimeAutoCompleteTextView
        )

        autoCompleteTextViews.forEach { autoCompleteTextView ->
            DropdownUtils.setupDropdown(requireContext(), autoCompleteTextView, R.array.time)
            autoCompleteTextView.setText("00:00")
        }
    }

    private fun initializeDayLayoutPairs() {
        dayButtonLayoutPairs = mapOf(
            binding.mondayBtn.apply { tag = "Monday" } to binding.mondayTimeLayout,
            binding.tuesdayBtn.apply { tag = "Tuesday" } to binding.tuesdayTimeLayout,
            binding.wednesdayBtn.apply { tag = "Wednesday" } to binding.wednesdayTimeLayout,
            binding.thursdayBtn.apply { tag = "Thursday" } to binding.thursdayTimeLayout,
            binding.fridayBtn.apply { tag = "Friday" } to binding.fridayTimeLayout,
            binding.saturdayBtn.apply { tag = "Saturday" } to binding.saturdayTimeLayout,
            binding.sundayBtn.apply { tag = "Sunday" } to binding.sundayTimeLayout
        )
    }

    private fun setupNavigation() {

        binding.nextBtn.setOnClickListener {
            updateSchedules()
            Log.d("EditWorkTimeActivity", "Day Schedules: $daySchedules")
        }
    }
    private fun setupDropdowns() {

        autoCompleteTextViews.forEach { autoCompleteTextView ->
            DropdownUtils.setupDropdown(requireContext(), autoCompleteTextView, R.array.time)
        }
    }

    private fun updateSchedules() {
        dayButtonLayoutPairs.entries.forEach { (button, layout) ->
            when (button.tag as String) {
                "Monday" -> {
                    daySchedules.monStartTime = binding.mondayStartTimeAutoCompleteTextView.text.toString()
                    daySchedules.monEndTime = binding.mondayEmdTimeAutoCompleteTextView.text.toString()
                }
                "Tuesday" -> {
                    daySchedules.tueStartTime = binding.tuesdayStartTimeAutoCompleteTextView.text.toString()
                    daySchedules.tueEndTime = binding.tuesEmdTimeAutoCompleteTextView.text.toString()
                }
                "Wednesday" -> {
                    daySchedules.wedStartTime = binding.wednesdayStartTimeAutoCompleteTextView.text.toString()
                    daySchedules.wedEndTime = binding.wednesdayEmdTimeAutoCompleteTextView.text.toString()
                }
                "Thursday" -> {
                    daySchedules.thuStartTime = binding.thursdayStartTimeAutoCompleteTextView.text.toString()
                    daySchedules.thuEndTime = binding.thursdayEmdTimeAutoCompleteTextView.text.toString()
                }
                "Friday" -> {
                    daySchedules.friStartTime = binding.fridayStartTimeAutoCompleteTextView.text.toString()
                    daySchedules.friEndTime = binding.fridayEmdTimeAutoCompleteTextView.text.toString()
                }
                "Saturday" -> {
                    daySchedules.satStartTime = binding.saturdayStartTimeAutoCompleteTextView.text.toString()
                    daySchedules.satEndTime = binding.saturdayEmdTimeAutoCompleteTextView.text.toString()
                }
                "Sunday" -> {
                    daySchedules.sunStartTime = binding.sundayStartTimeAutoCompleteTextView.text.toString()
                    daySchedules.sunEndTime = binding.sundayEmdTimeAutoCompleteTextView.text.toString()
                }
            }
        }
    }
    private fun resetAutoCompleteTextView(startTimeView: AutoCompleteTextView, endTimeView: AutoCompleteTextView) {

        startTimeView.setText("00:00", false)
        endTimeView.setText("00:00", false)


        DropdownUtils.setupDropdown(requireContext(), startTimeView, R.array.time)
        DropdownUtils.setupDropdown(requireContext(), endTimeView, R.array.time)
    }
    private fun resetDayTime(day: String?) {
        when (day) {
            "Monday" -> {
                daySchedules.monStartTime = "00:00"
                daySchedules.monEndTime = "00:00"
                resetAutoCompleteTextView(binding.mondayStartTimeAutoCompleteTextView, binding.mondayEmdTimeAutoCompleteTextView)
            }
            "Tuesday" -> {
                daySchedules.tueStartTime = "00:00"
                daySchedules.tueEndTime = "00:00"
                resetAutoCompleteTextView(binding.tuesdayStartTimeAutoCompleteTextView, binding.tuesEmdTimeAutoCompleteTextView)
            }
            "Wednesday" -> {
                daySchedules.wedStartTime = "00:00"
                daySchedules.wedEndTime = "00:00"
                resetAutoCompleteTextView(binding.wednesdayStartTimeAutoCompleteTextView, binding.wednesdayEmdTimeAutoCompleteTextView)
            }
            "Thursday" -> {
                daySchedules.thuStartTime = "00:00"
                daySchedules.thuEndTime = "00:00"
                resetAutoCompleteTextView(binding.thursdayStartTimeAutoCompleteTextView, binding.thursdayEmdTimeAutoCompleteTextView)
            }
            "Friday" -> {
                daySchedules.friStartTime = "00:00"
                daySchedules.friEndTime = "00:00"
                resetAutoCompleteTextView(binding.fridayStartTimeAutoCompleteTextView, binding.fridayEmdTimeAutoCompleteTextView)
            }
            "Saturday" -> {
                daySchedules.satStartTime = "00:00"
                daySchedules.satEndTime = "00:00"
                resetAutoCompleteTextView(binding.saturdayStartTimeAutoCompleteTextView, binding.saturdayEmdTimeAutoCompleteTextView)
            }
            "Sunday" -> {
                daySchedules.sunStartTime = "00:00"
                daySchedules.sunEndTime = "00:00"
                resetAutoCompleteTextView(binding.sundayStartTimeAutoCompleteTextView, binding.sundayEmdTimeAutoCompleteTextView)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

