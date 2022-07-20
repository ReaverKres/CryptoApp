package com.example.demonstrationapp.ui.popular

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.example.demonstrationapp.R
import com.example.demonstrationapp.base.BaseFragment
import com.example.demonstrationapp.databinding.FragmentPopularBinding
import com.example.demonstrationapp.extensions.longToast
import com.example.demonstrationapp.ui.popular.PopularViewModel.ExchangeEvent
import com.example.demonstrationapp.ui.popular.SortDialogFragment.Companion.SORT_DIALOG_RESULT
import com.example.demonstrationapp.ui.popular.SortDialogFragment.Companion.TYPE_OF_SORT
import com.example.demonstrationapp.utils.OnItemSelectedListenerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularFragment : BaseFragment<FragmentPopularBinding>(
    R.layout.fragment_popular
) {
    val viewModel: PopularViewModel by viewModels()

    private var adapter: CurrencyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(SORT_DIALOG_RESULT) { _, bundle ->
            val selectedSort = bundle.getInt(TYPE_OF_SORT)
            viewModel.sortList(selectedSort)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CurrencyAdapter { name, value ->
            longToast(getString(R.string.save_db, name))
            viewModel.saveCurrency(name,value)
        }
        binding.recycler.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner, ::render)

        binding.apply {

            spFromCurrency.onItemSelectedListener =
                object : OnItemSelectedListenerAdapter() {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        viewModel.getExchangeData(p0?.selectedItem.toString())
                    }
                }

            icSort.setOnClickListener {
                SortDialogFragment.newInstance(viewModel.selectedSort).show(
                    parentFragmentManager, SortDialogFragment.TAG
                )
            }
        }
    }

    private fun render(exchangeEvent: ExchangeEvent) {
        if(adapter?.currencies?.isEmpty() == true) adapter?.currencies = listOf()
        when (exchangeEvent) {
            is ExchangeEvent.Success -> {
                binding.progressBar.isVisible = false
                adapter?.currencies = exchangeEvent.result.currencies
            }
            is ExchangeEvent.Failure -> {
                binding.progressBar.isVisible = false
                longToast(exchangeEvent.errorText)
            }
            is ExchangeEvent.Loading -> binding.progressBar.isVisible = true
            is ExchangeEvent.Empty -> { }
        }
    }
}

