package com.kakaotech.team25M.ui.status.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kakaotech.team25M.databinding.ItemReservationStatusBinding
import com.kakaotech.team25M.domain.model.ReservationInfo
import com.kakaotech.team25M.domain.model.ReservationStatus.*
import com.kakaotech.team25M.ui.status.interfaces.OnCompanionStartClickListener
import com.kakaotech.team25M.ui.status.interfaces.OnShowDetailsClickListener
import java.text.SimpleDateFormat
import java.util.Locale

class ReservationStatusRecyclerViewAdapter(
    private val companionStartClickListener: OnCompanionStartClickListener,
    private val detailsClickListener: OnShowDetailsClickListener,
) : ListAdapter<
    ReservationInfo,
    ReservationStatusRecyclerViewAdapter.ReservationStatusViewHolder,
    >(DiffCallback()) {

    class ReservationStatusViewHolder(
        private val binding: ItemReservationStatusBinding,
        private val companionStartClickListener: OnCompanionStartClickListener,
        private val detailsClickListener: OnShowDetailsClickListener,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReservationInfo) {
            val dateString = item.reservationDate
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
            val outputFormat = SimpleDateFormat("M월 d일 a h시", Locale.KOREAN)

            val date = inputFormat.parse(dateString)?.let { outputFormat.format(it) }

            binding.reservationDateTextView.text = date
            binding.userNameTextView.text = item.patient.patientName

            when (item.reservationStatus) {
                진행중 -> {
                    binding.companionStartBtn.visibility = View.GONE
                    binding.companionCompleteBtn.visibility = View.VISIBLE
                }

                else -> {
                    binding.companionStartBtn.visibility = View.VISIBLE
                    binding.companionCompleteBtn.visibility = View.GONE
                }
            }

            binding.companionStartBtn.setOnClickListener {
                companionStartClickListener.onStartClicked(item)
            }

            binding.companionCompleteBtn.setOnClickListener {
                companionStartClickListener.onCompleteClicked(item)
            }

            binding.showDetailsBtn.setOnClickListener {
                detailsClickListener.onDetailsClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReservationStatusViewHolder {
        val binding =
            ItemReservationStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationStatusViewHolder(
            binding,
            companionStartClickListener,
            detailsClickListener,
        )
    }

    override fun onBindViewHolder(
        holder: ReservationStatusViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DiffCallback : DiffUtil.ItemCallback<ReservationInfo>() {
        override fun areItemsTheSame(
            oldItem: ReservationInfo,
            newItem: ReservationInfo,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ReservationInfo,
            newItem: ReservationInfo,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
