package com.example.demonstrationapp.ui.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.demonstrationapp.databinding.CurrencyItemBinding
import com.example.demonstrationapp.databinding.ListItemWithShimmerBinding
import com.example.domain.model.dto.Currency

class Shimmer()

class CurrencyAdapter(
    private val clickListener: (String, Double) -> Unit
) : ListAdapter<Any, RecyclerView.ViewHolder>(ItemComparator()) {

    companion object {
        private const val CURRENCY_ITEM_TYPE = 0
        private const val SHIMMER_ITEM_TYPE = 1
    }

    var currencies: List<Currency> = listOf()
        set(value) {
            field = value
            this.submitList(buildMergeList())
        }

    class ShimmerHolder(private val binding: ListItemWithShimmerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): ShimmerHolder {
                return ShimmerHolder(
                    ListItemWithShimmerBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }


    class ItemHolder(private val binding: CurrencyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    CurrencyItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }

        fun bind(rate: Currency, clickListener: (String, Double) -> Unit) = with(binding) {
            title.text = rate.name
            value.text = rate.value.toString()
            icon.setOnClickListener {
                clickListener(rate.name, rate.value)
            }
        }
    }

    private fun buildMergeList(): List<Any> {
        return when {
            currencies.isEmpty() -> createShimmerList()
            else -> currencies
        }
    }

    private fun createShimmerList(): List<Shimmer> {
        val mutableList = mutableListOf<Shimmer>()
        repeat(10) {
            mutableList.add(Shimmer())
        }
        return mutableList.toList()
    }

    class ItemComparator : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is Currency && newItem is Currency -> oldItem == newItem
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is Currency && newItem is Currency -> oldItem.name == newItem.name
                else -> false
            }
        }

    }

    override fun getItemViewType(position: Int): Int =
        when (this.currentList[position]) {
            is Currency -> CURRENCY_ITEM_TYPE
            else -> SHIMMER_ITEM_TYPE
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CURRENCY_ITEM_TYPE -> ItemHolder.create(parent)
            else -> ShimmerHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemHolder -> holder.bind(currencies[position], clickListener)
        }
    }
}