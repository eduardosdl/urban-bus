package com.exemple.urbanbus.ui.lines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.exemple.urbanbus.R
import com.exemple.urbanbus.adapters.BusLineAdapter
import com.exemple.urbanbus.databinding.FragmentSearchLinesBinding
import com.exemple.urbanbus.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchLinesFragment : Fragment() {
    private val viewModel: LinesViewModel by viewModels()
    private var _binding: FragmentSearchLinesBinding? = null
    private val binding get() = _binding!!

    private val linesAdapter = BusLineAdapter { busLine ->
        val lineBundle = Bundle()
        lineBundle.putParcelable("busLineData", busLine)
        findNavController().navigate(R.id.action_searchLinesFragment_to_lineFragment, lineBundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchLinesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        binding.linesList.adapter = linesAdapter
        binding.linesSearchInput.setEndIconOnClickListener {
            viewModel.getLines(binding.linesSearchInput.editText?.text.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observer() {
        viewModel.lines.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.loading.root.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    binding.loading.root.visibility = View.GONE
                    linesAdapter.setBusLineList(state.data)
                }

                is UiState.Failure -> {
                    binding.loading.root.visibility = View.GONE
                }
            }
        }
    }

}