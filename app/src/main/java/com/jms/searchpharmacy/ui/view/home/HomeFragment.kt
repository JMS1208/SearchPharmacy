package com.jms.searchpharmacy.ui.view.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.jms.searchpharmacy.R
import com.jms.searchpharmacy.data.model.server.PharmacyLocation
import com.jms.searchpharmacy.databinding.FragmentHomeBinding
import com.jms.searchpharmacy.databinding.ItemBestSearchBinding
import com.jms.searchpharmacy.databinding.ItemInBriefBinding
import com.jms.searchpharmacy.ui.view.MainActivity
import com.jms.searchpharmacy.ui.viewmodel.MainViewModel
import com.jms.searchpharmacy.util.Constants.DONG_NAME_NAV_ARGS
import com.jms.searchpharmacy.util.Constants.PERMISSION_REQUEST_CODE
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.util.FusedLocationSource
import java.util.jar.Manifest


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        (activity as MainActivity).mainViewModel
    }
    private var myDongName: String? = null


    private inner class Top5Adapter(val PLList: List<PharmacyLocation>) :
        RecyclerView.Adapter<Top5Adapter.Top5ViewHolder>() {

        inner class Top5ViewHolder(val itemBinding: ItemBestSearchBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {

            fun bind(pl: PharmacyLocation) {
                itemBinding.apply {
                    Glide.with(itemView)
                        .load(R.drawable.ic_best)
                        .into(this.icBest)


                    convCntTv.text = getString(R.string.conv_cnt, pl.convenience_count.toString())
                    convPerPharTv.text = getString(
                        R.string.conv_per_phar,
                        String.format("%.2f", pl.convenience_per_pharmacy)
                    )
                    docCntTv.text = getString(R.string.doc_cnt, pl.doctorcount.toString())
                    docPerPharTv.text = getString(
                        R.string.doc_per_phar,
                        String.format("%.2f", pl.doctor_per_pharmacy)
                    )
                    dongTv.text = pl.dong
                    hospCntTv.text = getString(R.string.hosp_cnt, pl.hospital_count.toString())
                    hospPerPharTv.text = getString(
                        R.string.hosp_per_phar,
                        String.format("%.2f", pl.hospital_per_pharmacy)
                    )
                    loadAddrTv.text = getString(R.string.load_addr, pl.load_address)
                    pharCntTv.text = getString(R.string.phar_cnt, pl.pharmacy_count.toString())

                }

                itemBinding.moveThisBtn.setOnClickListener {

                    val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(pl)
                    findNavController().navigate(action)
                }

                itemBinding.shareBtn.setOnClickListener {
                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT, getString(
                                R.string.send_pharLocation,
                                pl.dong,
                                getString(R.string.load_addr, pl.load_address),
                                getString(R.string.hosp_cnt, pl.hospital_count.toString()),
                                getString(R.string.phar_cnt, pl.pharmacy_count.toString()),
                                getString(R.string.doc_cnt, pl.doctorcount.toString()),
                                getString(R.string.conv_cnt, pl.convenience_count.toString()),
                                getString(
                                    R.string.hosp_per_phar,
                                    String.format("%.2f", pl.hospital_per_pharmacy)
                                ),
                                getString(
                                    R.string.doc_per_phar,
                                    String.format("%.2f", pl.doctor_per_pharmacy)
                                ),
                                getString(
                                    R.string.conv_per_phar,
                                    String.format("%.2f", pl.convenience_per_pharmacy)
                                )
                            )
                        )
                    }.also { intent ->
                        val chooserIntent =
                            Intent.createChooser(intent, getString(R.string.send_title))
                        startActivity(chooserIntent)
                    }
                }

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Top5ViewHolder {
            val itemBinding = ItemBestSearchBinding.inflate(layoutInflater, parent, false)
            return Top5ViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: Top5ViewHolder, position: Int) {
            holder.bind(PLList[position])
        }

        override fun getItemCount(): Int = PLList.size
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)


        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeTextInfoSingleLine.isSelected = true
        binding.searchSubway.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSelectSubwayFragment()
            findNavController().navigate(action)
        }
        binding.searchAddr.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToBriefFragment(null)
            findNavController().navigate(action)
        }


        binding.searchMyPlace.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                onCheckPermission()
            } else {


                val currentLocation: Location? = (activity as MainActivity).myLocation


                currentLocation?.let {
                    viewModel.checkInSeoul(it)
                    myDongName?.let { dongName->
                        val bundle = Bundle().apply {
                            putString(DONG_NAME_NAV_ARGS, dongName)
                        }
                        findNavController().navigate(R.id.fragment_brief, bundle)
                    } ?: run {
                        Toast.makeText(requireContext(), "서울 지역만 서비스 가능합니다", Toast.LENGTH_SHORT).show()
                    }



                }

                if (currentLocation == null) {
                    AlertDialog.Builder(requireContext()).setTitle("기록된 마지막 위치 없음")
                        .setMessage("현재 GPS 로 기록된 마지막 위치가 없습니다\n잠시 후에 다시 시도해주세요")
                        .setPositiveButton("확인", null)
                        .show()

                    (activity as MainActivity).createLocationRequest()

                }

            }
        }

        viewModel.checkInSeoulLiveData.observe(viewLifecycleOwner) { dongName ->

            myDongName = dongName

        }

        viewModel.fetchedPLsTop5List.observe(viewLifecycleOwner) {
            binding.top5RecyclerView.adapter = Top5Adapter(it)
            binding.top5RecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.fetchPLsTop5()

    }




    private fun onCheckPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                Toast.makeText(requireContext(), "위치 권한을 허용해야 사용 가능합니다", Toast.LENGTH_SHORT).show()

                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    PERMISSION_REQUEST_CODE
                )


            } else {

                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    PERMISSION_REQUEST_CODE
                )

            }

        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), "위치 권한이 설정되었습니다", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "위치 권한이 취소되었습니다", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {}
        }

    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}