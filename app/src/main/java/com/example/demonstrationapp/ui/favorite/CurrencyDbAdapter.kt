package com.example.demonstrationapp.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.demonstrationapp.R
import com.example.demonstrationapp.databinding.CurrencyItemDbBinding
import com.example.domain.model.dto.FavoriteCurrencyDomain

class CurrencyDbAdapter(
    private val clickListener: (String, String) -> Unit
) : ListAdapter<FavoriteCurrencyDomain, CurrencyDbAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(private val binding: CurrencyItemDbBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rate: FavoriteCurrencyDomain, clickListener: (String, String) -> Unit) = with(binding) {
            baseName.text = rate.baseName
            baseValue.text = rate.baseValue.toString()
            name.text = rate.name
            value.text = rate.value.toString()
            icon.setImageResource(R.drawable.ic_star_blue)
            icon.setOnClickListener {
                clickListener(rate.name, rate.baseName)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    CurrencyItemDbBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<FavoriteCurrencyDomain>(){
        override fun areItemsTheSame(oldItem: FavoriteCurrencyDomain, newItem: FavoriteCurrencyDomain): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: FavoriteCurrencyDomain, newItem: FavoriteCurrencyDomain): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}