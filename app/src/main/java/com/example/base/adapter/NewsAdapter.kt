package com.example.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.net.toUri
import coil.load
import com.example.base.Application
import com.example.base.R
import com.example.base.base.BaseAdapter
import com.example.base.base.BaseDiffCallBack
import com.example.base.databinding.ItemVpNewsBinding
import com.example.base.model.Item
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

class NewsAdapter(private val listener: OnItemClickListener) :
    BaseAdapter<Item, ItemVpNewsBinding>(DiffCallBack()) {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemVpNewsBinding {
        return ItemVpNewsBinding.inflate(inflater, parent, false)
    }

    override fun bindItem(binding: ItemVpNewsBinding, item: Item) {
        binding.tvTitle.text = item.title
        binding.tvDescription.text = parseHTMLText(item.description)
        val imageUri =
            parseHTMLImg(item.description).toUri().buildUpon().scheme("https").build()
        binding.ivNews.load(imageUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.broken_image)
        }
        binding.tvTitle.setOnClickListener {
            it.startAnimation(
                AnimationUtils.loadAnimation(
                    Application.Instance,
                    androidx.appcompat.R.anim.abc_fade_out
                )
            )
            listener.onItemClick(item, "title")
        }
        binding.ivLike.setOnClickListener {
            binding.ivLike.isSelected = !binding.ivLike.isSelected
            if (binding.ivLike.isSelected) {
                binding.ivLike.setImageLevel(1)
                listener.onItemClick(item, "like")
            } else {
                binding.ivLike.setImageLevel(0)
                listener.onItemClick(item, "unlike")
            }
        }
    }

    private fun parseHTMLImg(html: String): String {
        // Parse the HTML using jsoup
        val doc = Jsoup.parse(html, "", Parser.xmlParser())
        // Get the image source URL
        val imgElement = doc.select("img").firstOrNull()
        val imgSrc = imgElement?.attr("src")
        return imgSrc ?: ""
    }

    private fun parseHTMLText(html: String): String {
        // Parse the HTML using jsoup
        val doc = Jsoup.parse(html, "", Parser.xmlParser())
        // Get the image source URL
        val content = doc.text()
        return content ?: ""
    }

    private class DiffCallBack : BaseDiffCallBack<Item>() {
        override fun areItemsTheSameImpl(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSameImpl(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Item, key: String)
    }
}