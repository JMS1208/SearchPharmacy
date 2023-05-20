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
import com.jms.searchpharmacy.data.model.server.Pharmacy
import com.jms.searchpharmacy.databinding.FragmentDetailPharBinding
import com.jms.searchpharmacy.databinding.ItemDetailHospBinding
import com.jms.searchpharmacy.databinding.ItemDetailPharBinding
import com.jms.searchpharmacy.ui.view.MainActivity
import com.jms.searchpharmacy.ui.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailPharFragment : Fragment() {
    private var _binding: FragmentDetailPharBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by lazy {
        (activity as MainActivity).mainViewModel
    }


    private inner class DetailPharAdapter(val pharList: List<Pharmacy>) :
        RecyclerView.Adapter<DetailPharAdapter.DetailPharViewHolder>() {

        inner class DetailPharViewHolder(val itemBinding: ItemDetailPharBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {

            fun bind(phar: Pharmacy) {
                itemBinding.apply {
                    pharAddr.text = phar.address
                    pharDate.text = phar.startdate
                    pharName.text = phar.name
                }

                itemView.setOnClickListener {
                    viewModel.moveThisPlace(phar.address)
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailPharViewHolder {
            val itemBinding = ItemDetailPharBinding.inflate(layoutInflater, parent, false)
            return DetailPharViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: DetailPharViewHolder, position: Int) {
            val phar = pharList[position]
            holder.bind(phar)
        }

        override fun getItemCount(): Int = pharList.size


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailPharBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchedPharList.observe(viewLifecycleOwner) {
            binding.pharRv.adapter = DetailPharAdapter(it)
            binding.pharRv.layoutManager = LinearLayoutManager(requireContext())


        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}