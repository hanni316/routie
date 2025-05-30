package com.gbsb.routiemobile.adapter

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.databinding.ItemStoreBinding
import com.gbsb.routiemobile.dto.Item

class StoreAdapter(
    private val showPrice: Boolean = true,
    private val onPreview: (Item) -> Unit,
    private val onBuy: (Item) -> Unit
) : ListAdapter<Item, StoreAdapter.ViewHolder>(DIFF_CALLBACK) {

    private val purchasedIds = mutableSetOf<Int>()

    fun setPurchasedItems(ids: Set<Int>) {
        purchasedIds.clear()
        purchasedIds.addAll(ids)
        notifyDataSetChanged()
    }

    companion object {
        // 반드시 : DiffUtil.ItemCallback<Item> 으로 타입을 명시
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Item> =
            object : DiffUtil.ItemCallback<Item>() {
                override fun areItemsTheSame(old: Item, new: Item): Boolean =
                    old.itemId == new.itemId

                override fun areContentsTheSame(old: Item, new: Item): Boolean =
                    old == new
            }
    }

    inner class ViewHolder(private val binding: ItemStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var lastClickTime = 0L

        fun bind(item: Item) {
            // 구매 여부 판단
            val isPurchased = purchasedIds.contains(item.itemId)

            // 이름 / 가격 표시
            binding.tvName.text = item.name
            if (showPrice) {
                binding.tvPrice.visibility = View.VISIBLE
                if (isPurchased) {
                    binding.tvPrice.text = "구매됨"
                    binding.tvPrice.alpha = 0.5f
                } else {
                    binding.tvPrice.text = "${item.price}코인"
                    binding.tvPrice.alpha = 1f
                }
            } else {
                binding.tvPrice.visibility = View.GONE
            }

            // 이미지 로딩
            val ctx = binding.imgItem.context
            val resId = ctx.resources.getIdentifier(
                item.nameEn, "drawable", ctx.packageName
            )
            binding.imgItem.setImageResource(
                if (resId != 0) resId else R.drawable.t_shirt
            )

            // 클릭 처리, 구매된 것은 차단
            binding.root.setOnClickListener {
                if (isPurchased) {
                    Toast.makeText(ctx, "이미 구매된 아이템입니다", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
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
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
