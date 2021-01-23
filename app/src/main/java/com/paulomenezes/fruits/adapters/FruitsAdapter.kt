package com.paulomenezes.fruits.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.paulomenezes.fruits.DetailActivity
import com.paulomenezes.fruits.MainActivity
import com.paulomenezes.fruits.R
import com.paulomenezes.fruits.databinding.ListItemFruitBinding
import com.paulomenezes.fruits.models.Fruit
import com.paulomenezes.fruits.utils.toDP
import java.io.InputStream

class FruitsAdapter (
        private val context: Context,
        private val list: MutableList<Fruit>,
        private val onItemClick: (Int) -> Unit
    ) : RecyclerView.Adapter<FruitsAdapter.ViewHolder>() {

    class ViewHolder(itemView: ListItemFruitBinding) : RecyclerView.ViewHolder(itemView.root) {
        val imageView = itemView.imageView
        val textName = itemView.textName
        val textBenefit = itemView.textBenefit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_fruit, parent, false)
        val binding = ListItemFruitBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textName.text = list[position].name
        holder.textBenefit.text = list[position].benefits
        holder.imageView.setImageBitmap(list[position].image)

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }

        if (position == list.lastIndex) {
            val layoutParams = holder.imageView.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.bottomMargin = 16.toDP(context)

            holder.imageView.layoutParams = layoutParams
            holder.imageView.requestLayout()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}