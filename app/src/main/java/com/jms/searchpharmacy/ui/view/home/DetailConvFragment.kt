package com.jms.searchpharmacy.ui.view.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jms.searchpharmacy.R
import com.jms.searchpharmacy.data.model.server.Convenience
import com.jms.searchpharmacy.data.model.server.Hospital
import com.jms.searchpharmacy.databinding.FragmentDetailConvBinding
import com.jms.searchpharmacy.databinding.ItemDetailConvBinding
import com.jms.searchpharmacy.databinding.ItemDetailHospBinding
import com.jms.searchpharmacy.ui.view.MainActivity
import com.jms.searchpharmacy.ui.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailConvFragment : Fragment() {
    private var _binding: FragmentDetailConvBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by lazy {
        (activity as MainActivity).mainViewModel
    }

    private inner class DetailConvAdapter(val convList: List<Convenience>) :
        RecyclerView.Adapter<DetailConvAdapter.DetailConvViewHolder>() {

        inner class DetailConvViewHolder(val itemBinding: ItemDetailConvBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {

            fun bind(conv: Convenience) {
                itemBinding.apply {
                    convAddr.text = conv.address
                    convDate.text = conv.startdate
                    convName.text = conv.name
                }
                itemView.setOnClickListener {
                    viewModel.moveThisPlace(conv.address)
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailConvViewHolder {
            val itemBinding = ItemDetailConvBinding.inflate(layoutInflater, parent, false)
            return DetailConvViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: DetailConvViewHolder, position: Int) {
            val conv = convList[position]
            holder.bind(conv)
        }

        override fun getItemCount(): Int = convList.size


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailConvBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchedConvList.observe(viewLifecycleOwner) {
            binding.convRv.adapter = DetailConvAdapter(it)
            binding.convRv.layoutManager = LinearLayoutManager(requireContext())

        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}