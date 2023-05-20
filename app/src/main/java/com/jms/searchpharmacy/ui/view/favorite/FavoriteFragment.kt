package com.jms.searchpharmacy.ui.view.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.jms.searchpharmacy.R
import com.jms.searchpharmacy.data.model.server.PharmacyLocation
import com.jms.searchpharmacy.databinding.FragmentFavoriteBinding

import com.jms.searchpharmacy.databinding.ItemFavoritePlBinding
import com.jms.searchpharmacy.ui.view.MainActivity
import com.jms.searchpharmacy.ui.viewmodel.MainViewModel

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter

    private val favoriteListItemHelper = object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            (viewHolder as? FavoriteAdapter.FavoriteViewHolder)?.let {
                val position = it.bindingAdapterPosition
                val pl = favoriteAdapter.currentList[position]

                viewModel.deletePharLocation(pl)
                Snackbar.make(requireView(), "찜 목록에서 제거되었습니다", Snackbar.LENGTH_SHORT).apply {
                    setAction("취소") {

                        viewModel.savePharLocation(pl)

                    }
                }.show()
            }
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }

        override fun isLongPressDragEnabled(): Boolean {
            return false
        }

    }

    private inner class FavoriteAdapter() :
        ListAdapter<PharmacyLocation, FavoriteAdapter.FavoriteViewHolder>(PharDiffCallback) {

        inner class FavoriteViewHolder(val itemBinding: ItemFavoritePlBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
            //lateinit var pl: PharmacyLocation

            fun bind(pl: PharmacyLocation) {
                //this.pl = pl
                itemBinding.apply {
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


                    //Detail 이동
                    moveThisBtn.setOnClickListener {
                        val action =
                            FavoriteFragmentDirections.actionFragmentFavoriteToFragmentDetail(pl)
                        findNavController().navigate(action)
                    }

                    shareBtn.setOnClickListener {
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
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
            val itemBinding = ItemFavoritePlBinding.inflate(layoutInflater, parent, false)
            return FavoriteViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
            val pl = currentList[position]
            holder.bind(pl)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).mainViewModel
        favoriteAdapter = FavoriteAdapter()
        binding.favoriteRv.adapter = favoriteAdapter
        ItemTouchHelper(favoriteListItemHelper).attachToRecyclerView(binding.favoriteRv)
        binding.favoriteRv.layoutManager = LinearLayoutManager(requireContext())

        viewModel.favoritePharLocations.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                favoriteAdapter.submitList(it)
                binding.favoriteRv.layoutManager = LinearLayoutManager(requireContext())
                binding.noItemNotice.isVisible = false
            } else {
                binding.noItemNotice.isVisible = true

            }

        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    companion object {
        private val PharDiffCallback = object : DiffUtil.ItemCallback<PharmacyLocation>() {
            override fun areItemsTheSame(
                oldItem: PharmacyLocation,
                newItem: PharmacyLocation
            ): Boolean {
                return oldItem.index == newItem.index
            }

            override fun areContentsTheSame(
                oldItem: PharmacyLocation,
                newItem: PharmacyLocation
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}