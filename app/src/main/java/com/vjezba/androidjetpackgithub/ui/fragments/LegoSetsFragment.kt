package com.vjezba.androidjetpackgithub.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.databinding.FragmentLegosetsBinding
import com.vjezba.androidjetpackgithub.ui.adapters.LegoSetAdapter
import com.vjezba.androidjetpackgithub.ui.utilities.ConnectivityUtil
import com.vjezba.androidjetpackgithub.ui.utilities.GridSpacingItemDecoration
import com.vjezba.androidjetpackgithub.ui.utilities.VerticalItemDecoration
import com.vjezba.androidjetpackgithub.ui.utilities.hide
import com.vjezba.androidjetpackgithub.viewmodels.LegoSetsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_languages_main.*


@AndroidEntryPoint
class LegoSetsFragment : Fragment() {

    private val viewModel : LegoSetsViewModel by viewModels()

    private val args: LegoSetsFragmentArgs by navArgs()

    private lateinit var binding: FragmentLegosetsBinding
    private val adapter: LegoSetAdapter by lazy { LegoSetAdapter() }
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var gridLayoutManager: GridLayoutManager
    private val linearDecoration: RecyclerView.ItemDecoration by lazy {
        VerticalItemDecoration(
                resources.getDimension(R.dimen.margin_normal).toInt())
    }
    private val gridDecoration: RecyclerView.ItemDecoration by lazy {
        GridSpacingItemDecoration(
                SPAN_COUNT, resources.getDimension(R.dimen.margin_normal).toInt())
    }

    private var isLinearLayoutManager: Boolean = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel.connectivityAvailable = ConnectivityUtil.isConnected(requireContext())
        viewModel.themeId = if (args.themeId == -1) null else args.themeId

        binding = FragmentLegosetsBinding.inflate(inflater, container, false)
        context ?: return binding.root

        linearLayoutManager = LinearLayoutManager(activity)
        gridLayoutManager = GridLayoutManager(activity, SPAN_COUNT)
        setLayoutManager()
        binding.recyclerView.adapter = adapter

        subscribeUi(adapter)
        activity?.speedDial?.visibility = View.GONE

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun subscribeUi(adapter: LegoSetAdapter) {
        viewModel.legoSets.observe(viewLifecycleOwner) {
            binding.progressBar.hide()
            adapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_representation, menu)
        setDataRepresentationIcon(menu.findItem(R.id.list))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.list -> {
                isLinearLayoutManager = !isLinearLayoutManager
                setDataRepresentationIcon(item)
                val lastPosition = findLastPositionInRecyclerView()
                setLayoutManager()
                scrollToLastPositionWhenSwicthingLayouts(lastPosition)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun findLastPositionInRecyclerView() : Int {
        var scrollPosition = 0
        // If a layout manager has already been set, get current scroll position.
        if (binding.recyclerView.layoutManager != null) {
            scrollPosition = (binding.recyclerView.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()
        }
        return scrollPosition
    }

    private fun setLayoutManager() {

        if (isLinearLayoutManager) {
            binding.recyclerView.removeItemDecoration(gridDecoration)
            binding.recyclerView.addItemDecoration(linearDecoration)
            binding.recyclerView.layoutManager = linearLayoutManager
        } else {
            binding.recyclerView.removeItemDecoration(linearDecoration)
            binding.recyclerView.addItemDecoration(gridDecoration)
            binding.recyclerView.layoutManager = gridLayoutManager
        }
    }

    private fun scrollToLastPositionWhenSwicthingLayouts(lastPosition: Int) {
        binding.recyclerView.scrollToPosition(lastPosition)
    }

    private fun setDataRepresentationIcon(item: MenuItem) {
        item.setIcon(if (isLinearLayoutManager)
            R.drawable.ic_dance_24dp else R.drawable.ic_doggo_24dp)
    }

    companion object {
        const val SPAN_COUNT = 3
    }

}
