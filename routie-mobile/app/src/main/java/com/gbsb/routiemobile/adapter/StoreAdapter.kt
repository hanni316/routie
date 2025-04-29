// 파일: app/src/main/kotlin/com/gbsb/routiemobile/adapter/StoreAdapter.kt
package com.gbsb.routiemobile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.databinding.ItemStoreBinding
import com.gbsb.routiemobile.dto.Item

class StoreAdapter(
    private val onItemClick: (Item) -> Unit
) : ListAdapter<Item, StoreAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(old: Item, new: Item) = old.itemId == new.itemId
            override fun areContentsTheSame(old: Item, new: Item) = old == new
        }
    }

    inner class ViewHolder(private val binding: ItemStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            // 이름 및 가격 표시
            binding.tvName.text = item.name
            binding.tvPrice.text = "${item.price}코인"

            // nameEn을 drawable 리소스명으로 사용하여 이미지 설정
            val context = binding.imgItem.context
            val resId = context.resources.getIdentifier(
                item.nameEn, // ex: "top_1"
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
                onItemClick(item)
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
