package com.example.github.repositories.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.github.repositories.R
import com.example.github.repositories.data.LocalDataStore
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.databinding.LayoutRepositoryBinding
import javax.inject.Inject

class RepositoryAdapter @Inject constructor(
    private var context: Context,
    private val localDataStore: LocalDataStore
) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {
    private var list: List<RepositoryDTO>? = null
    private lateinit var onClick: (RepositoryDTO) -> Unit

    override fun getItemCount(): Int = list?.size!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutRepositoryBinding.inflate(LayoutInflater.from(context))
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.get(position).also {
            if (it != null) {
                holder.bind(it)
            }
        }
    }

    fun setOnClick(onClick: (RepositoryDTO) -> Unit) {
        this.onClick = onClick
    }

    fun setList(list: List<RepositoryDTO>){
        this.list = list
    }

    inner class ViewHolder(binding: LayoutRepositoryBinding, val onClick: (RepositoryDTO) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private val titleTxt: TextView = binding.title
        private val imageVw: ImageView = binding.image
        private val descriptionTxt: TextView = binding.description
        private val authorTxt: TextView = binding.author
        private var currentRepository: RepositoryDTO? = null

        init {
            itemView.setOnClickListener{
                currentRepository?.let {
                    onClick(it)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(repositoryDTO: RepositoryDTO) {
            currentRepository = repositoryDTO
            titleTxt.text = "#" + (adapterPosition + 1) + ": " + repositoryDTO.full_name?.uppercase()
            descriptionTxt.text = if (repositoryDTO.description?.length!! > 150) repositoryDTO.description?.take(150)
                .plus("...") else repositoryDTO.description
            authorTxt.text = repositoryDTO.owner?.login
            imageVw.setImageResource(
                if (localDataStore.getBookmarks().contains(repositoryDTO))
                    R.drawable.baseline_bookmark_black_24
                else
                    R.drawable.baseline_bookmark_border_black_24
            )
        }
    }
}