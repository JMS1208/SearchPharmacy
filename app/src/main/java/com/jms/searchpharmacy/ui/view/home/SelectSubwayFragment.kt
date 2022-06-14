package com.jms.searchpharmacy.ui.view.home


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jms.searchpharmacy.data.api.server.RetrofitClient
import com.jms.searchpharmacy.data.model.server.Line
import com.jms.searchpharmacy.data.model.server.Station
import com.jms.searchpharmacy.databinding.FragmentSelectSubwayBinding
import com.jms.searchpharmacy.databinding.ItemInDetailSubwayBinding
import com.jms.searchpharmacy.databinding.ItemInSelectSubwayBinding
import com.jms.searchpharmacy.ui.view.MainActivity
import com.jms.searchpharmacy.ui.viewmodel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SelectSubwayFragment : Fragment() {

    private var _binding : FragmentSelectSubwayBinding? = null
    private val binding get() = _binding!!
    private lateinit var lineList: List<Line>

    private val viewModel : MainViewModel by lazy {
        (activity as MainActivity).mainViewModel
    }

    private inner class DetailNameAdapter(stationList: List<Station>)
        : RecyclerView.Adapter<DetailNameAdapter.DetailNameViewHolder>() {

        private var stationList: List<Station> = stationList

        inner class DetailNameViewHolder(val itemInDetailSubwayBinding: ItemInDetailSubwayBinding)
            : RecyclerView.ViewHolder(itemInDetailSubwayBinding.root) {

            fun bind(station: Station) {
                // 역이름 받아서 배치
                itemView.apply {
                    itemInDetailSubwayBinding.stationNameText.text = station.getName()
                    itemInDetailSubwayBinding.stationNameText.setOnClickListener {
                        getDongByStation(station.getName())
                        val action = SelectSubwayFragmentDirections.actionFragmentSelectSubwayToFragmentBrief("흑석동")
                        findNavController().navigate(action)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailNameViewHolder {
            val binding = ItemInDetailSubwayBinding.inflate(layoutInflater, parent, false)
            return DetailNameViewHolder(binding)
        }

        override fun onBindViewHolder(holder: DetailNameViewHolder, position: Int) {
            val station = stationList[position]
            holder.bind(station)

        }

        override fun getItemCount(): Int = stationList.size

        private fun getDongByStation(station_name: String) {
            val call = RetrofitClient.getInstance().serverApi.getDongByStation(station_name)
            call.enqueue(object : Callback<List<Station>> {
                override fun onResponse(
                    call: Call<List<Station>>,
                    response: Response<List<Station>>
                ) {
                    val dongResponse = response.body()!!
                }

                override fun onFailure(call: Call<List<Station>>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "An error has occured",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }

    private inner class BriefNameAdapter(lineList: List<Line>)
        : RecyclerView.Adapter<BriefNameAdapter.BriefNameViewHolder>() {

        private val lineList: List<Line> = lineList

        inner class BriefNameViewHolder(val itemInSelectSubwayBinding: ItemInSelectSubwayBinding)
            : RecyclerView.ViewHolder(itemInSelectSubwayBinding.root) {
            //여기서 어답터 지정
            val toggleButton: MutableLiveData<Boolean> = MutableLiveData(false)
            lateinit var line: Line

            init {
                toggleButton.observe(viewLifecycleOwner){ isClicked ->
                    if(isClicked) {
                        itemInSelectSubwayBinding.detailOfLineRecyclerView.isVisible = true
                        getStationsByLine(line.getName())
                    } else {
                        itemInSelectSubwayBinding.detailOfLineRecyclerView.isVisible = false
                    }
                }
            }
            private

            fun getStationsByLine(line_name: String) {
                val call = RetrofitClient.getInstance().serverApi.getStationByLine(line_name)
                call.enqueue(object : Callback<List<Station>> {
                    override fun onResponse(
                        call: Call<List<Station>>,
                        response: Response<List<Station>>
                    ) {
                        val stationResponse = response.body()!!
                        var stationList = stationResponse
                        itemInSelectSubwayBinding.detailOfLineRecyclerView.adapter = DetailNameAdapter(stationList)
                        itemInSelectSubwayBinding.detailOfLineRecyclerView.layoutManager = GridLayoutManager(requireContext(),3)
                        itemInSelectSubwayBinding.detailOfLineRecyclerView.addItemDecoration(
                            DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL)
                        )

                    }

                    override fun onFailure(call: Call<List<Station>>, t: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            "An error has occured",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }

            fun bind(line: Line) {
                this.line = line

//                    itemInSelectSubwayBinding.colorOfLine.circleBackgroundColor = Color.parseColor(line.color)
                itemInSelectSubwayBinding.lineNumberText.text = line.getName()
                itemInSelectSubwayBinding.lineNumberLayout.setOnClickListener {
                    toggleButton.postValue(!toggleButton.value!!)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BriefNameViewHolder {
            val binding = ItemInSelectSubwayBinding.inflate(layoutInflater, parent, false)
            return BriefNameViewHolder(binding)
        }

        override fun onBindViewHolder(holder: BriefNameViewHolder, position: Int) {
            val line = lineList[position]
            holder.bind(line)
        }

        override fun getItemCount(): Int = lineList.size

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectSubwayBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLines()
    }

    private fun getLines() {
        val call: Call<List<Line>> = RetrofitClient.getInstance().getServerApi().getLines()
        call.enqueue(object : Callback<List<Line>> {
            override fun onResponse(call: Call<List<Line>>, response: Response<List<Line>>) {
                val lineResponse = response.body()!!
                lineList = lineResponse
                binding.allLinesRecyclerView.adapter = BriefNameAdapter(lineList)
                binding.allLinesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            }

            override fun onFailure(call: Call<List<Line>>, t: Throwable) {
                Toast.makeText(requireContext(), "An error has occured", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}