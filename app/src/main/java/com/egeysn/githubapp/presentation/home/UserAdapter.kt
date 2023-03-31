package com.egeysn.githubapp.presentation.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.egeysn.githubapp.databinding.ListItemUserBinding
import com.egeysn.githubapp.domain.models.User

class UserAdapter(
    private val listener: UserItemListener
) : RecyclerView.Adapter<ViewHolder>() {

    private val data = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, listener)
    }

    fun updateItems(favIds: List<Int>) {
        data.forEach {
            it.isFav = favIds.contains(it.id)
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<User>) {
        this.data.clear()
        this.data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder) { bind(data[position]) }

    override fun getItemCount(): Int = data.size
}

class ViewHolder(
    private val binding: ListItemUserBinding,
    private val listener: UserItemListener
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var user: User

    init {
        binding.root.setOnClickListener(this)
    }

    fun bind(item: User) {
        this.user = item
        binding.tvTitle.text = item.name
        Glide.with(itemView.context).load(item.avatar).into(binding.ivUser)
        binding.tvOverview.text = item.name
        binding.checkBoxFav.isChecked = item.isFav
        binding.checkBoxFav.setOnCheckedChangeListener { _, isChecked ->
            // item.isFav = isChecked
            listener.onFavoriteClicked(id = user.id, isChecked)
        }
    }

    override fun onClick(v: View?) = listener.onUserClicked(user.name)
}

interface UserItemListener {
    fun onUserClicked(username: String)

    fun onFavoriteClicked(id: Int, isChecked: Boolean)
}
