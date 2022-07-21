package com.example.demonstrationapp.ui.popular

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.example.demonstrationapp.R
import com.example.demonstrationapp.base.BaseFragment
import com.example.demonstrationapp.databinding.FragmentPopularBinding
import com.example.demonstrationapp.ui.popular.PopularViewModel.ExchangeEvent
import com.example.demonstrationapp.ui.popular.SortDialogFragment.Companion.SORT_DIALOG_RESULT
import com.example.demonstrationapp.ui.popular.SortDialogFragment.Companion.TYPE_OF_SORT
import com.example.demonstrationapp.utils.OnItemSelectedListenerAdapter
import com.example.demonstrationapp.utils.extensions.collectWhenStarted
import com.example.demonstrationapp.utils.extensions.longToast
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

        collectWhenStarted(viewModel.data, ::render)
        createAdapter()

        binding.apply {
            spFromCurrency.onItemSelectedListener =
                object : OnItemSelectedListenerAdapter() {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        if(viewModel.baseName != p0?.selectedItem.toString())
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

    private fun createAdapter() {
        adapter = CurrencyAdapter { rate ->
            if(rate.isSaved) viewModel.deleteCurrency(rate)
            else viewModel.saveCurrency(rate)
        }
        binding.recycler.adapter = adapter
    }

    private fun render(exchangeEvent: ExchangeEvent) {
        when (exchangeEvent) {
            is ExchangeEvent.Success -> {
                adapter?.currencies = exchangeEvent.currenciesDto.currencies
                val allAccessibleCurrencies = exchangeEvent.currenciesDto.allAccessibleCurrencies.toList()
                setItemsToSpinner(allAccessibleCurrencies)
            }
            is ExchangeEvent.Failure -> {
                longToast(exchangeEvent.errorText)
            }
            is ExchangeEvent.Loading -> adapter?.currencies = listOf()
            is ExchangeEvent.Empty -> { }
        }
    }

    private fun setItemsToSpinner(allAccessibleCurrencies: List<String>) {
        if(allAccessibleCurrencies.isNotEmpty()) {
            val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                allAccessibleCurrencies
            )
            binding.spFromCurrency.adapter = spinnerAdapter
        }
    }
}

