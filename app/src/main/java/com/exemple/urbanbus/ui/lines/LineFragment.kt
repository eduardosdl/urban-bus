package com.exemple.urbanbus.ui.lines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.exemple.urbanbus.MainActivity
import com.exemple.urbanbus.R
import com.exemple.urbanbus.adapters.MarkerIconConverter
import com.exemple.urbanbus.databinding.FragmentLineBinding
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
class LineFragment : Fragment(), OnMapReadyCallback {
    private val args: LineFragmentArgs by navArgs()
    private val viewModel: LinesViewModel by viewModels()
    private var _binding: FragmentLineBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLineBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manageMap()

        val lineSelected = args.busLineData
        val lineName = "${lineSelected.sign}-${lineSelected.signId}"

        (activity as MainActivity).updateToolbarTitle(
            lineName.limitTitleLength(
                15
            )
        )

        if (lineSelected.direction == 1) {
            binding.startTerminal.text = lineSelected.mainTerminal
            binding.endTerminal.text = lineSelected.secondaryTerminal
        } else {
            binding.startTerminal.text = lineSelected.secondaryTerminal
            binding.endTerminal.text = lineSelected.mainTerminal
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val position = LatLng(-23.589811, -46.638537)
        val icon = MarkerIconConverter(requireContext()).execute(R.drawable.ic_location_on_24dp)

        map.addMarker(
            MarkerOptions().position(position).title("testando").icon(icon)
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16f))
    }

    private fun manageMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
}