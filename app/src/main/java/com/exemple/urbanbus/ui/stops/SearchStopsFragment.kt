package com.exemple.urbanbus.ui.stops

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.exemple.urbanbus.adapters.BusStopAdapter
import com.exemple.urbanbus.databinding.FragmentSearchStopsBinding
import com.exemple.urbanbus.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import com.exemple.urbanbus.R

@AndroidEntryPoint
class SearchStopsFragment : Fragment() {
    private val viewModel: StopsViewModel by viewModels()
    private var _binding: FragmentSearchStopsBinding? = null
    private val binding get() = _binding!!

    private val stopAdapter = BusStopAdapter {
        val bundle = Bundle()
        bundle.putParcelable("stopBusData", it)
        findNavController().navigate(R.id.action_searchStopsFragment_to_stopFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchStopsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        binding.stopsSearchInput.setEndIconOnClickListener {
            viewModel.getStops(binding.stopsSearchInput.editText?.text.toString())
        }

        binding.stopsList.adapter = stopAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStops(binding.stopsSearchInput.editText?.text.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observer() {
        viewModel.stops.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.loading.root.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    binding.loading.root.visibility = View.GONE
                    stopAdapter.setStopList(state.data)
                }

                is UiState.Failure -> {
                    binding.loading.root.visibility = View.GONE
                }
            }

        }
    }
}