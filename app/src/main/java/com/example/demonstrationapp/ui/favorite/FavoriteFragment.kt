package com.example.demonstrationapp.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.demonstrationapp.R
import com.example.demonstrationapp.base.BaseFragment
import com.example.demonstrationapp.databinding.FragmentWithRecyclerBinding
import com.example.demonstrationapp.utils.extensions.collectWhenStarted

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
        collectWhenStarted(viewModel.data, ::render)
    }

    private fun render(data: ExchangeEvent) {
        when (data) {
            is ExchangeEvent.Success -> {
                adapter?.submitList(data.result)
                binding.progressBar.isVisible = false
                binding.listEmpty.isVisible = false
            }
            is ExchangeEvent.Loading -> binding.progressBar.isVisible = true
            is ExchangeEvent.Empty -> {
                adapter?.submitList(emptyList())
                binding.progressBar.isVisible = false
                binding.listEmpty.isVisible = true
            }
        }
    }
}