package com.jms.searchpharmacy.ui.view.home

import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator

import com.jms.a20220602_navermap.data.model.Addresse
import com.jms.searchpharmacy.R
import com.jms.searchpharmacy.data.model.server.PharmacyLocation
import com.jms.searchpharmacy.databinding.FragmentDetailBinding

import com.jms.searchpharmacy.ui.view.MainActivity
import com.jms.searchpharmacy.ui.viewmodel.MainViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.processNextEventInCurrentThread
import kotlin.properties.Delegates


class DetailFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()

    private val viewModel: MainViewModel by lazy {
        (activity as MainActivity).mainViewModel
    }

    private val detailFragmentList =
        arrayOf(DetailHospFragment(), DetailPharFragment(), DetailConvFragment())

    private val isExpandedLiveData: MutableLiveData<Boolean> = MutableLiveData(true)

    private var isExpanded: Boolean = true

    private var paddingHeightExpanded: Int = 0
    private var paddingHeight: Int = 0

    private lateinit var naverMap: NaverMap

    private var isFavoritePL: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment

        activity?.let {
            val fm = childFragmentManager
            val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction()
                        .add(R.id.map, it)
                        .addToBackStack(null)
                        .commit()
                }
            mapFragment.getMapAsync(this)

        }




        return binding.root
    }

    private fun loadDetailList() {
        viewModel.apply {
            fetchConvList(args.pharmacyLocation.index)
            fetchHospList(args.pharmacyLocation.index)
            fetchPharList(args.pharmacyLocation.index)
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadDetailList()


        viewModel.loadPL(args.pharmacyLocation.index)
        viewModel.plLiveData.observe(viewLifecycleOwner) {
            isFavoritePL = it != null
            if (isFavoritePL) {
                binding.addFavoriteBtn.setColorFilter(android.R.color.transparent)
            } else {
                val matrix = ColorMatrix()
                    matrix.setSaturation(0F)
                val filter = ColorMatrixColorFilter(matrix)
                binding.addFavoriteBtn.colorFilter = filter
            }
        }

        binding.addFavoriteBtn.setOnClickListener {

            if (isFavoritePL) {
                //삭제
                viewModel.deletePharLocation(args.pharmacyLocation)

            } else {
                //추가
                viewModel.savePharLocation(args.pharmacyLocation)

            }
        }


        binding.viewPager2WithMap.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = detailFragmentList.size

            override fun createFragment(position: Int): Fragment {
                return detailFragmentList[position]
            }
        }

        TabLayoutMediator(binding.tabLayoutInDetail, binding.viewPager2WithMap) { tab, position ->

            when (position) {
                0 -> { //병원정보 디테일
                    tab.text = "병원"
                }
                1 -> { //약국정보 디테일
                    tab.text = "약국"
                }
                2 -> { //편의점 정보 디테일
                    tab.text = "편의점"
                }
            }

        }.attach()

        binding.toggleButton.setOnClickListener {
            isExpandedLiveData.postValue(!isExpandedLiveData.value!!)

        }

        viewModel.fetchedPharList.observe(viewLifecycleOwner) {

            for (i in 0 until if (it.size > 5) 5 else it.size) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.searchPharLoc(it[i].address)
                }
            }
        }

        viewModel.fetchedHospList.observe(viewLifecycleOwner) {

            for (i in 0 until if (it.size > 5) 5 else it.size) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.searchHospLoc(it[i].address)
                }
            }
        }

        viewModel.fetchedConvList.observe(viewLifecycleOwner) {

            for (i in 0 until if (it.size > 5) 5 else it.size) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.searchConvLoc(it[i].address)
                }
            }
        }


    }


    private fun setupUISettings() {

        paddingHeightExpanded = binding.contentLayoutInDetail.height
        paddingHeight = binding.toggleButton.height + 50
        binding.toggleButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
        binding.toggleButton.setBackgroundResource(android.R.color.transparent)
        naverMap.setContentPadding(0, 0, 0, binding.contentLayoutInDetail.height)

        binding.toggleButton.setOnClickListener {


            isExpanded = !isExpanded
            binding.viewPager2WithMap.isVisible = isExpanded
            if (isExpanded) {
                binding.toggleButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
                binding.toggleButton.setBackgroundResource(android.R.color.transparent)
                naverMap.setContentPadding(0, 0, 0, paddingHeightExpanded)
            } else {
                binding.toggleButton.setImageResource(android.R.drawable.ic_menu_add)
                binding.toggleButton.setBackgroundResource(android.R.color.transparent)
                naverMap.setContentPadding(0, 0, 0, paddingHeight)
            }
        }

        binding.moveMyPlaceBtn.setOnClickListener {
            val myLocation = (activity as MainActivity).myLocation
            myLocation?.let {
                val cameraUpdate = CameraUpdate.scrollAndZoomTo(LatLng(it), 16.0).animate(CameraAnimation.Easing)
                this.naverMap.moveCamera(cameraUpdate)
                val markerPhar = Marker()
                markerPhar.apply {
                    icon = MarkerIcons.BLACK
                    iconTintColor = Color.DKGRAY
                    captionText="내위치"
                    setCaptionAligns(Align.Top)
                    position = LatLng(it)
                    map = naverMap
                }
            }


        }



    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        setupUISettings()

        viewModel.moveThisResult.observe(viewLifecycleOwner) { geoInfo ->

            geoInfo ?: run {
                Toast.makeText(requireContext(), "검색 결과 없음", Toast.LENGTH_SHORT).show()
            }

            geoInfo?.addresses?.get(0)?.let {
                val latLng = LatLng(it.y!!.toDouble(), it.x!!.toDouble())
                val cameraUpdate = CameraUpdate.scrollAndZoomTo(latLng, 17.0).animate(CameraAnimation.Easing)
                this.naverMap.moveCamera(cameraUpdate)
                val marker = Marker()
                marker.apply {
                    icon = MarkerIcons.BLACK
                    iconTintColor = Color.RED
                    position = latLng
                    map = this@DetailFragment.naverMap
                }
            }
        }

        viewModel.searchPhar.observe(viewLifecycleOwner) { response ->
            val addresses: List<Addresse>? = response?.addresses

            addresses?.let {

                for (i in it.indices) {
                    val markerPhar = Marker()
                    markerPhar.apply {
                        captionText="약국"
                        setCaptionAligns(Align.Top)
                        position = LatLng(it[i].y!!.toDouble(), it[i].x!!.toDouble())
                        map = naverMap
                    }
                }

            }
        }
        viewModel.searchConv.observe(viewLifecycleOwner) { response ->
            val addresses: List<Addresse>? = response?.addresses

            addresses?.let {
                for (i in it.indices) {
                    val markerConv = Marker()
                    markerConv.apply {
                        captionText="편의점"
                        setCaptionAligns(Align.Top)
                        position = LatLng(it[i].y!!.toDouble(), it[i].x!!.toDouble())
                        map = naverMap
                    }
                }
            }
        }
        viewModel.searchHosp.observe(viewLifecycleOwner) { response ->
            val addresses: List<Addresse>? = response?.addresses

            addresses?.let { list ->
                for (i in list.indices) {
                    val markerHosp = Marker()
                    markerHosp.apply {
                        captionText="병원"
                        setCaptionAligns(Align.Top)
                        position = LatLng(list[i].y!!.toDouble(), list[i].x!!.toDouble())
                        map = naverMap

                    }

                }
                list[0].x?.let {
                    val cameraUpdate = CameraUpdate.scrollTo(
                        LatLng(
                            list[0].y!!.toDouble(),
                            list[0].x!!.toDouble()
                        )
                    )
                    naverMap.moveCamera(cameraUpdate)
                }

                view?.invalidate()
            }
        }


    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }


}