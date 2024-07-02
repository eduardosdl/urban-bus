package com.exemple.urbanbus.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.exemple.urbanbus.R
import com.exemple.urbanbus.adapters.MarkerIconConverter
import com.exemple.urbanbus.adapters.BusStopAdapter
import com.exemple.urbanbus.data.models.BusStop
import com.exemple.urbanbus.databinding.FragmentHomeBinding
import com.exemple.urbanbus.utils.UiState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), OnMapReadyCallback {
    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap

    private val stopAdapter = BusStopAdapter {
        val bundle = Bundle()
        bundle.putParcelable("stopBusData", it)
        findNavController().navigate(R.id.action_homeFragment_to_stopFragment, bundle)
    }

    companion object {
        private val SAO_PAULO_LOCATION = LatLng(-23.589811, -46.638537)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        customizationBottomSheet()
        viewModel.authenticate()
        viewModel.getAllBusStops()

        binding.stopsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchStopsFragment)
        }

        binding.linesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchLinesFragment)
        }

         binding.stopsLists.adapter = stopAdapter

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun customizationBottomSheet() {
        val standardBottomSheet = binding.bottomSheet
        val standardBottomSheetBehavior = BottomSheetBehavior.from(standardBottomSheet)

        standardBottomSheetBehavior.isHideable = false
        standardBottomSheetBehavior.peekHeight =
            resources.getDimensionPixelSize(R.dimen.bottom_sheet_peek_height)
        standardBottomSheetBehavior.maxHeight =
            resources.getDimensionPixelSize(R.dimen.bottom_sheet_max_height)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SAO_PAULO_LOCATION, 12f))
    }

    private fun addMarkers(busStops: List<BusStop>) {
        for (busStop in busStops) {
            val position = LatLng(busStop.latitude, busStop.longitude)
            val titleToShow = busStop.name.ifBlank {
                busStop.code.toString()
            }
            val icon = MarkerIconConverter(requireContext()).execute(R.drawable.ic_location_on_24dp)
            map.addMarker(MarkerOptions().position(position).title(titleToShow).icon(icon))

        }
    }

    private fun observer() {
        viewModel.stops.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {}
                is UiState.Success -> {
                    addMarkers(state.data)
                    stopAdapter.setStopList(state.data)
                    Log.d("test", "data: ${state.data.size}")
                }

                is UiState.Failure -> {
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}