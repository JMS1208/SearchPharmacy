package com.jms.searchpharmacy.ui.view

import android.Manifest
import android.app.Fragment
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationBarView
import com.jms.searchpharmacy.R
import com.jms.searchpharmacy.data.db.SearchPharDatabase
import com.jms.searchpharmacy.databinding.ActivityMainBinding

import com.jms.searchpharmacy.repository.MainRepository
import com.jms.searchpharmacy.ui.view.home.HomeFragment
import com.jms.searchpharmacy.ui.view.home.HomeFragmentDirections
import com.jms.searchpharmacy.ui.viewmodel.MainViewModel
import com.jms.searchpharmacy.ui.viewmodel.MainViewModelFactory
import com.jms.searchpharmacy.util.Constants
import com.jms.searchpharmacy.util.Constants.DONG_NAME_NAV_ARGS
import com.jms.searchpharmacy.util.Constants.LAST_MY_PLACE
import com.naver.maps.geometry.LatLng


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    val mainViewModel: MainViewModel by lazy {
        val database = SearchPharDatabase.getInstance(this)
        val naverMapSearchRepository = MainRepository(database)
        val factory = MainViewModelFactory(naverMapSearchRepository)
        ViewModelProvider(this, factory)[MainViewModel::class.java]

    }

    private lateinit var navController: NavController


    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    var myLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupJetpackNavigation()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        savedInstanceState ?: createLocationRequest()
        savedInstanceState?.getParcelable<Location>(LAST_MY_PLACE) ?: createLocationRequest()

        savedInstanceState?.getParcelable<Location>(LAST_MY_PLACE)?.let {
            myLocation = it
        }


    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        myLocation?.let {
            outState.putParcelable(LAST_MY_PLACE, it)
        }
    }

    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

        } else {
            //퍼미션 요청

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    Constants.PERMISSION_REQUEST_CODE
                )


            } else {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    Constants.PERMISSION_REQUEST_CODE
                )

            }
        }
    }

    fun createLocationRequest() {

        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 10000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                myLocation = locationResult.lastLocation

            }
        }


        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())


        task.addOnFailureListener {
            requestLocation()
        }
        task.addOnSuccessListener {
            //여기서 위치요청
            requestLocation()
        }
    }

    //현재 위치가 서울인지 파악
    fun onCheckInSeoul(currentLocation: Location) {

        mainViewModel.convertCoordsToAddr(LatLng(currentLocation))

        //mainViewModel.convertCoordsToAddr(LatLng(37.4046, -122.2213))
        mainViewModel.regionLiveData.observe(this) { region ->
            region?.area1?.name?.let { siName ->

                if (siName.contains("서울")) { //서울이 이름에 들어가 있으면
                    region.area3?.name?.let { dongName ->
                        val bundle = Bundle().apply {
                            putString(DONG_NAME_NAV_ARGS, dongName)
                        }
                        navController.navigate(R.id.fragment_brief, bundle)
                    }

                } else {
                    Toast.makeText(this, "현재 서울 지역만 서비스가 가능합니다", Toast.LENGTH_SHORT)
                        .show()

                }
            }
            region ?: run {
                Toast.makeText(this, "현재 서울 지역만 서비스가 가능합니다", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setupJetpackNavigation() {
        val host =
            supportFragmentManager.findFragmentById(R.id.search_pharmacy_host_fragment) as NavHostFragment
        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.fragment_home -> {
                    navController.navigate(R.id.fragment_home)
                    true
                }

                R.id.fragment_favorite -> {
                    navController.navigate(R.id.fragment_favorite)
                    true
                }

                R.id.fragment_settings -> {
                    navController.navigate(R.id.fragment_settings)
                    true
                }

                else -> {

                    false
                }

            }

        }
    }
}