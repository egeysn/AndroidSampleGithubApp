package com.egeysn.githubapp.presentation.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.egeysn.githubapp.databinding.ListItemSearchMovieBinding
import com.egeysn.githubapp.domain.models.User

class SearchUserAdapter(
    private val listener: UserItemListener
) : RecyclerView.Adapter<ViewModel>() {

    private val data = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val binding = ListItemSearchMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewModel(binding, listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<User>) {
        this.data.clear()
        this.data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        with(holder) { bind(data[position]) }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class ViewModel(
    private val binding: ListItemSearchMovieBinding,
    private val listener: UserItemListener
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var user: User

    init {
        binding.root.setOnClickListener(this)
    }

    fun bind(item: User) {
        this.user = item
        binding.tvTitle.text = item.name
        Glide.with(itemView.context).load(item.avatar).into(binding.ivMovie)
        binding.tvOverview.text = "asd asd"
        binding.checkBoxFav.setOnClickListener { }
    }

    override fun onClick(v: View?) {
        listener.onUserClicked(user.name)
    }
}

interface UserItemListener {
    fun onUserClicked(username: String)
}
