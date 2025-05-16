package com.gbsb.routiemobile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.databinding.ItemStoreBinding
import com.gbsb.routiemobile.dto.Item
import android.os.SystemClock
import android.view.View

class StoreAdapter(
    private val showPrice: Boolean = true,
    private val onPreview: (Item) -> Unit,
    private val onBuy: (Item) -> Unit
) : ListAdapter<Item, StoreAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(old: Item, new: Item) = old.itemId == new.itemId
            override fun areContentsTheSame(old: Item, new: Item) = old == new
        }
    }

    inner class ViewHolder(private val binding: ItemStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

            private var lastClickTime = 0L

        fun bind(item: Item) {
            // 이름 및 가격 표시
            binding.tvName.text = item.name
            if (showPrice) {
                binding.tvPrice.visibility = View.VISIBLE
                binding.tvPrice.text = "${item.price}코인"
            } else {
                binding.tvPrice.visibility = View.GONE
            }

            // 이미지 로딩
            val context = binding.imgItem.context
            val resId = context.resources.getIdentifier(
                item.nameEn,
                "drawable",
                context.packageName
            )
            if (resId != 0) {
                binding.imgItem.setImageResource(resId)
            } else {
                binding.imgItem.setImageResource(R.drawable.t_shirt)
            }

            // 아이템 클릭 시 onItemClick 호출
            binding.root.setOnClickListener {
                val now = SystemClock.elapsedRealtime()
                if (now - lastClickTime < 300) {
                    onBuy(item)
                } else {
                    onPreview(item)
                }
                lastClickTime = now
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
