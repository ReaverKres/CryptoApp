package com.example.demonstrationapp.ui.popular

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.navGraphViewModels
import com.example.demonstrationapp.R
import com.example.demonstrationapp.base.BaseFragment
import com.example.demonstrationapp.databinding.FragmentPopularBinding
import com.example.demonstrationapp.ui.PopularViewModel
import com.example.demonstrationapp.utils.OnItemSelectedListenerAdapter
import com.example.domain.model.ExchangeResponse
import com.example.domain.model.Output

class PopularFragment : BaseFragment<FragmentPopularBinding>(
    R.layout.fragment_popular
) {

    override val viewModel by navGraphViewModels<PopularViewModel>(R.id.nav_graph)

    private lateinit var adapter: CurrencyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.data.observe(viewLifecycleOwner) {
            render(it)
        }

        binding.apply {
            spFromCurrency.onItemSelectedListener =
                object : OnItemSelectedListenerAdapter() {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        viewModel.getExchangeData(p0?.selectedItem.toString())
                    }
                }

            icSort.setOnClickListener {
                SortDialogFragment().show(
                    childFragmentManager, SortDialogFragment.TAG
                )
//                {
//                    viewModel.sortList(it)
//                }
            }
        }
    }

    private fun render(output: Output<ExchangeResponse>) {
        when (output) {
            is Output.Success -> {
                binding.progressBar.isVisible = false
//                adapter.submitList(output.data?.currencies)
            }
            is Output.Error -> {
                binding.progressBar.isVisible = false
                showToast(requireContext(), output.exception)
            }
            is Output.Loading -> binding.progressBar.isVisible = true
        }
    }

    private fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
}

