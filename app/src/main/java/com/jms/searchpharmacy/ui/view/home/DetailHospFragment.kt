package com.jms.searchpharmacy.ui.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jms.searchpharmacy.R
import com.jms.searchpharmacy.data.model.server.Hospital
import com.jms.searchpharmacy.databinding.FragmentDetailHospBinding
import com.jms.searchpharmacy.databinding.ItemDetailHospBinding

import com.jms.searchpharmacy.ui.view.MainActivity
import com.jms.searchpharmacy.ui.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailHospFragment : Fragment() {
    private var _binding: FragmentDetailHospBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        (activity as MainActivity).mainViewModel
    }

    private inner class DetailHospAdapter(val hospList: List<Hospital>) :
        RecyclerView.Adapter<DetailHospAdapter.DetailHospViewHolder>() {

        inner class DetailHospViewHolder(val itemBinding: ItemDetailHospBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {

            fun bind(hosp: Hospital) {
                itemBinding.apply {
                    hospAddr.text = hosp.address
                    hospDate.text = hosp.startdate
                    hospDocCnt.text = hosp.total_doctor.toString()
                    hospName.text = hosp.name
                    hospType.text = hosp.type

                }
                itemView.setOnClickListener {
                    viewModel.moveThisPlace(hosp.address)
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailHospViewHolder {
            val itemBinding = ItemDetailHospBinding.inflate(layoutInflater, parent, false)
            return DetailHospViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: DetailHospViewHolder, position: Int) {
            val hosp = hospList[position]
            holder.bind(hosp)
        }

        override fun getItemCount(): Int = hospList.size


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailHospBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchedHospList.observe(viewLifecycleOwner) {
            binding.hospRv.adapter = DetailHospAdapter(it)
            binding.hospRv.layoutManager = LinearLayoutManager(requireContext())


        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}