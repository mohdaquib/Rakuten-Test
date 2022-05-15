package com.example.github.repositories.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.github.repositories.R
import com.example.github.repositories.data.LocalDataStore
import com.example.github.repositories.data.RepositoryDTO
import javax.inject.Inject

class RepositoryAdapter @Inject constructor(
    private val localDataStore: LocalDataStore
) : ListAdapter<RepositoryDTO, RepositoryAdapter.RepositoryViewHolder>(RepositoryDiffCallback) {
    private var list: List<RepositoryDTO>? = null
    private lateinit var onClick: (RepositoryDTO) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        return RepositoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_repository, parent, false),
            onClick, localDataStore
        )
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        list?.get(position).also {
            if (it != null) {
                holder.bind(it)
            }
        }
    }

    override fun submitList(list: MutableList<RepositoryDTO>?) {
        super.submitList(list)
        this.list = list
    }

    fun setOnClick(onClick: (RepositoryDTO) -> Unit) {
        this.onClick = onClick
    }

    class RepositoryViewHolder(
        view: View,
        val onClick: (RepositoryDTO) -> Unit,
        val localDataStore: LocalDataStore
    ) :
        RecyclerView.ViewHolder(view) {
        private val titleTxt: TextView = view.findViewById(R.id.title)
        private val imageVw: ImageView = view.findViewById(R.id.image)
        private val descriptionTxt: TextView = view.findViewById(R.id.description)
        private val authorTxt: TextView = view.findViewById(R.id.author)
        private var currentRepository: RepositoryDTO? = null

        init {
            itemView.setOnClickListener {
                currentRepository?.let {
                    onClick(it)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(repositoryDTO: RepositoryDTO?) {
            currentRepository = repositoryDTO
            titleTxt.text =
                "#" + (adapterPosition + 1) + ": " + repositoryDTO?.full_name?.uppercase()
            descriptionTxt.text =
                if (repositoryDTO?.description != null && repositoryDTO.description!!.length > 150) repositoryDTO.description?.take(
                    150
                ).plus("...") else repositoryDTO?.description ?: "No Description Available"
            authorTxt.text = repositoryDTO?.owner?.login
            imageVw.setImageResource(
                if (localDataStore.getBookmarks().contains(repositoryDTO))
                    R.drawable.baseline_bookmark_black_24
                else
                    R.drawable.baseline_bookmark_border_black_24
            )
        }
    }
}

object RepositoryDiffCallback : DiffUtil.ItemCallback<RepositoryDTO>() {
    override fun areItemsTheSame(oldItem: RepositoryDTO, newItem: RepositoryDTO): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: RepositoryDTO, newItem: RepositoryDTO): Boolean {
        return oldItem.id == newItem.id
    }
}