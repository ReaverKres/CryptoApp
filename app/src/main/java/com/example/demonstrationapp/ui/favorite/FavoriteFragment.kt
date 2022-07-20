package com.example.demonstrationapp.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.demonstrationapp.R
import com.example.demonstrationapp.base.BaseFragment
import com.example.demonstrationapp.databinding.FragmentWithRecyclerBinding

class FavoriteFragment :
    BaseFragment<FragmentWithRecyclerBinding>(R.layout.fragment_with_recycler) {

    private val viewModel: FavoriteViewModel by activityViewModels()

    private var adapter: CurrencyDbAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CurrencyDbAdapter { name, baseName ->
            viewModel.deleteCurrency(name, baseName)
        }
        binding.recycler.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner, ::render)
    }

    private fun render(data: ExchangeEvent) {
        when (data) {
            is ExchangeEvent.Success -> {
                adapter?.submitList(data.result)
                binding.progressBar.isVisible = false
            }
            is ExchangeEvent.Loading -> binding.progressBar.isVisible = true
            is ExchangeEvent.Empty -> {
                adapter?.submitList(emptyList())
                binding.progressBar.isVisible = false
            }
        }
    }
}