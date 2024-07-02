package com.exemple.urbanbus.ui.stops

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.exemple.urbanbus.MainActivity
import com.exemple.urbanbus.R
import com.exemple.urbanbus.adapters.BusStopArrivalAdapter
import com.exemple.urbanbus.adapters.MarkerIconConverter
import com.exemple.urbanbus.databinding.FragmentStopBinding
import com.exemple.urbanbus.utils.UiState
import com.exemple.urbanbus.utils.getBusStopTitle
import com.exemple.urbanbus.utils.limitTitleLength
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StopFragment : Fragment(), OnMapReadyCallback {
    private val args: StopFragmentArgs by navArgs()
    private val viewModel: StopsViewModel by viewModels()
    private var _binding: FragmentStopBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private val arrivalAdapter = BusStopArrivalAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStopBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        (activity as MainActivity).updateToolbarTitle(
            getBusStopTitle(args.stopBusData).limitTitleLength(
                15
            )
        )

        manageMap()
        viewModel.getLineArrival("700013950")

        binding.stopLocation.text = args.stopBusData.address
        binding.arrivelList.adapter = arrivalAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val position = LatLng(args.stopBusData.latitude, args.stopBusData.longitude)
        val icon = MarkerIconConverter(requireContext()).execute(R.drawable.ic_location_on_24dp)

        map.addMarker(
            MarkerOptions().position(position).title(getBusStopTitle(args.stopBusData)).icon(icon)
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16f))
    }

    private fun manageMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun observer() {
        viewModel.lineArrival.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {}
                is UiState.Success -> {
                    arrivalAdapter.setBusStopArrivalList(state.data)
                }
                is UiState.Failure -> {}
            }

        }
    }
}