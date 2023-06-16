package com.example.base.ui.translate

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.base.Application
import com.example.base.R
import com.example.base.databinding.ItemVpNewsBinding
import com.example.base.model.Item
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

class NewsAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<NewsAdapter.Companion.NewsViewHolder>() {
    private val news = ArrayList<Item>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            ItemVpNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListNews(news: ArrayList<Item>) {
        this.news.clear()
        this.news.addAll(news)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = news.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(news[position])
        holder.binding.tvTitle.setOnClickListener {
            it.startAnimation(
                AnimationUtils.loadAnimation(
                    Application.Instance,
                    androidx.appcompat.R.anim.abc_fade_out
                )
            )
            listener.onItemClick(news[position])
        }
    }

    companion object {
        class NewsViewHolder(val binding: ItemVpNewsBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(item: Item) {
                binding.tvTitle.text = item.title
                binding.tvDescription.text = parseHTMLText(item.description)
                val imageUri =
                    parseHTMLImg(item.description).toUri().buildUpon().scheme("https").build()
                binding.ivNews.load(imageUri) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.broken_image)
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
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }
}