package com.example.team25.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.team25.databinding.FragmentEditWorkLocationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditWorkLocationFragment : Fragment() {
    private var _binding: FragmentEditWorkLocationBinding? = null
    private val binding get() = _binding!!
    private val managerInformationViewModel: ManagerInformationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditWorkLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
    }

}
