package com.jms.searchpharmacy.ui.view.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jms.searchpharmacy.R
import com.jms.searchpharmacy.data.model.server.PharmacyLocation
import com.jms.searchpharmacy.databinding.FragmentBriefBinding
import com.jms.searchpharmacy.databinding.ItemInBriefBinding
import com.jms.searchpharmacy.databinding.ItemInBriefChartBinding
import com.jms.searchpharmacy.databinding.ItemInBriefFieldNameBinding
import com.jms.searchpharmacy.ui.view.MainActivity
import com.jms.searchpharmacy.ui.viewmodel.MainViewModel
import com.jms.searchpharmacy.util.Constants
import java.io.LineNumberReader


private const val ITEM_FIELD_NAME = 0
private const val ITEM_ROW = 1

class BriefFragment : Fragment() {

    private var _binding: FragmentBriefBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<BriefFragmentArgs>()


    private val viewModel: MainViewModel by lazy {
        (activity as MainActivity).mainViewModel
    }
    private var myDongName: String? = null


    private inner class BriefAdapter(val PLList: List<PharmacyLocation>) :
        RecyclerView.Adapter<BriefAdapter.BriefViewHolder>() {

        inner class BriefViewHolder(val itemBinding: ItemInBriefChartBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {


            fun bind(pl: PharmacyLocation) {
                itemBinding.apply {
                    roadNameAddr.text = pl.load_address
                    hospitalCnt.text = pl.hospital_count.toString()
                    pharmacyCnt.text = pl.pharmacy_count.toString()
                    convStoreCnt.text = pl.convenience_count.toString()
                    docCnt.text = pl.doctorcount.toString()

                    val hospPharAnal: String = if (pl.pharmacy_count == 0) {
                        if(pl.hospital_count != 0) {
                            "병원만 있는 지역입니다"
                        } else {
                            "병원이 없는 지역입니다"
                        }

                    } else {
                        if (pl.hospital_count > pl.pharmacy_count) {
                            "약국이 병원보다 ${
                                String.format(
                                    "%.1f",
                                    pl.hospital_count / pl.pharmacy_count.toFloat()
                                )
                            }배 적은 지역입니다"
                        } else if (pl.hospital_count == pl.pharmacy_count) {
                            "약국과 병원의 수가 같은 지역입니다"
                        } else {
                            if(pl.hospital_count != 0){
                                "약국이 병원보다 ${
                                    String.format(
                                        "%.1f",
                                        pl.pharmacy_count / pl.hospital_count.toFloat()
                                    )
                                }배 많은 지역입니다"
                            } else {
                                "병원이 없는 지역입니다"
                            }

                        }

                    }

                    val convPharAnal: String = if (pl.pharmacy_count == 0) {
                        if(pl.convenience_count != 0) {
                            "상비약 취급 편의점만 있는 지역입니다"
                        } else {
                            "상비약 취급 편의점이 없는 지역입니다"
                        }

                    } else {
                        if (pl.convenience_count > pl.pharmacy_count) {
                            "약국이 상비약 취급 편의점보다 ${
                                String.format(
                                    "%.1f",
                                    pl.convenience_count / pl.pharmacy_count.toFloat()
                                )
                            }배 적은 지역입니다"
                        } else if (pl.convenience_count == pl.pharmacy_count) {
                            "약국과 상비약 취급 편의점의 수가 같은 지역입니다"
                        } else {
                            if(pl.convenience_count != 0){
                                "약국이 상비약 취급 편의점보다 ${
                                    String.format(
                                        "%.1f",
                                        pl.pharmacy_count / pl.convenience_count.toFloat()
                                    )
                                }배 많은 지역입니다"
                            } else {
                                "상비약 취급 편의점이 없는 지역입니다"
                            }

                        }

                    }

                    pharHospAnalyText.text = hospPharAnal

                    pharConvAnalyText.text = convPharAnal

                    pharHospGraph.apply {
                        if(pl.hospital_count != 0 && pl.pharmacy_count != 0) {
                            val pharHospSum = pl.hospital_count + pl.pharmacy_count.toFloat()

                            setValues(
                                pl.pharmacy_count / pharHospSum * 100,
                                pl.hospital_count / pharHospSum * 100
                            )
                            this.isVisible = true
                        } else {

                            this.isVisible = false

                        }


                    }
                    pharConvGraph.apply {

                        if(pl.pharmacy_count != 0 && pl.convenience_count != 0) {
                            val pharConvSum = pl.pharmacy_count + pl.convenience_count.toFloat()

                            setValues(
                                pl.pharmacy_count / pharConvSum * 100,
                                pl.convenience_count / pharConvSum * 100
                            )
                            this.isVisible = true
                        } else {
                            this.isVisible = false
                        }


                    }

                }

                itemView.setOnClickListener {

                    val action =
                        BriefFragmentDirections.actionFragmentBriefToFragmentDetail(pl)
                    findNavController().navigate(action)
                }
                

            }

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BriefViewHolder {
            val binding = ItemInBriefChartBinding.inflate(layoutInflater, parent, false)
            return BriefViewHolder(binding)

        }

        override fun onBindViewHolder(holder: BriefAdapter.BriefViewHolder, position: Int) {

            holder.bind(PLList[position])


        }

        override fun getItemCount(): Int = PLList.size


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBriefBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textInputEditText.setText(args.dongName)

        viewModel.checkInSeoulLiveData.observe(viewLifecycleOwner) { dongName ->

            myDongName = dongName


        }

        binding.floatingActionButton.setOnClickListener {
            val act = activity as MainActivity
            act.myLocation?.let {
                viewModel.checkInSeoul(it)
            }
            myDongName?.let {
                viewModel.fetchPLs(it)
                binding.textInputEditText.setText(it)
            } ?: run {
                Toast.makeText(requireContext(), "서울 지역만 서비스 가능합니다", Toast.LENGTH_SHORT).show()
            }

        }

        if (args.dongName.isNullOrEmpty()) {
            binding.briefInfoRecyclerView.adapter = BriefAdapter(emptyList())
            binding.briefInfoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.fetchedPLs.observe(viewLifecycleOwner) {
            binding.briefInfoRecyclerView.adapter = BriefAdapter(it)
            binding.briefInfoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.fetchPLs(args.dongName ?: "")


        binding.textInputEditText.imeOptions = EditorInfo.IME_ACTION_SEARCH


        binding.textInputEditText.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                return when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {

                        binding.textInputEditText.text?.let {

                            if (it.isNotEmpty() && it.matches(Constants.dongNamePattern)) {
                                viewModel.fetchPLs(it.toString())
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "지역을 다시 확인해주세요\n서울 지역만 가능합니다",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        true
                    }
                    else -> {
                        false
                    }
                }

            }

        })
        binding.textInputEditText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    binding.textInputEditText.text?.let {
                        if (it.isNotEmpty()) {
                            val result = it.matches(Constants.dongNamePattern)
                            if (result) {
                                //TODO{}
                            } else {
                                //에러 띄우기
                                binding.textInputLayout.error = "'동' 또는 '가'로 끝나야 합니다 ex) 역삼동"

                            }
                            Log.d("TAG", "tit: $it")
                        }
                    }

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (!binding.textInputEditText.isFocused) {
                        binding.textInputLayout.error = null
                    }

                }

            }
        )

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}