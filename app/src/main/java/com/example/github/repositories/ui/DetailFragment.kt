package com.example.github.repositories.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.github.repositories.R
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.databinding.FragmentDetailBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private val detailViewModel: DetailViewModel by viewModels()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private var repository: RepositoryDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            repository = arguments?.get(REPO_KEY) as RepositoryDTO
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text = repository?.name
        binding.detail.text =
            "${resources.getString(R.string.created_by)} ${repository?.owner?.login} , at ${repository?.created_at}"
        Picasso.get().load(repository?.owner?.avatar_url).into(binding.image)
        binding.description.text = repository?.description
        binding.url.text = repository?.html_url

        binding.image.setImageResource(
            if (detailViewModel.getBookmarks().contains(repository))
                R.drawable.baseline_bookmark_black_24
            else
                R.drawable.baseline_bookmark_border_black_24
        )
        binding.image.setOnClickListener {
            val isBookmarked = detailViewModel.getBookmarks().contains(repository)
            repository.let {
                if (it != null) {
                    detailViewModel.bookmarkRepo(it, !isBookmarked)
                }
            }
            binding.image.setImageResource(if (!isBookmarked) R.drawable.baseline_bookmark_black_24 else R.drawable.baseline_bookmark_border_black_24)
        }
        binding.detail.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, UserFragment.newInstance(repository?.owner!!))
                .addToBackStack("user")
                .commit()
        }
    }

    companion object {
        val REPO_KEY = "repo_key"
        fun newInstance(repositoryDTO: RepositoryDTO) = DetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(REPO_KEY, repositoryDTO)
            }
        }
    }
}