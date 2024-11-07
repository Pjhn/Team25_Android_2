package com.kakaotech.team25M.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.kakaotech.team25M.databinding.FragmentEditCommentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCommentFragment : Fragment() {
    private var _binding: FragmentEditCommentBinding? = null
    private val binding get() = _binding!!
    private val managerInformationViewModel: ManagerInformationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditCommentBinding.inflate(inflater, container, false)
        return binding.root
    }


}
