package com.example.github.repositories.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.github.repositories.R
import com.example.github.repositories.data.OwnerDTO
import com.example.github.repositories.databinding.FragmentUserBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException
import javax.inject.Inject

@AndroidEntryPoint
class UserFragment : Fragment() {
    @Inject
    lateinit var repositoryAdapter: RepositoryAdapter
    private val viewModel: UserViewModel by viewModels()
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private var user: OwnerDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            user = arguments?.get(REPO_OWNER_KEY) as OwnerDTO
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.text = user?.login
        Picasso.get().load(user?.avatar_url?.toUri()).into(binding.image)
        binding.list.adapter = repositoryAdapter

        user?.login?.let { viewModel.fetchUser(it) }
        viewModel.user.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UserViewModel.UserState.Success -> {
                    binding.detail.text = state.result.twitter_username?.let {
                        viewModel.getTitleText(
                            resources.getString(R.string.twitter_handle),
                            it
                        )
                    }
                    state.result.repos_url?.let { viewModel.fetchRepositories(it) }
                }
                is UserViewModel.UserState.Error -> {
                    showErrorScreen(state.error)
                }
            }
        }

        viewModel.repositories.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UserViewModel.UserReposState.Success -> {
                    repositoryAdapter.submitList(state.result.toMutableList())
                    repositoryAdapter.setOnClick {

                    }
                }
                is UserViewModel.UserReposState.Error -> {
                    showErrorScreen(state.error)
                }
            }
        }
    }

    private fun showErrorScreen(exception: Exception) {
        binding.contentLayout.visibility = View.GONE
        binding.errorLayout.root.visibility = View.VISIBLE
        binding.errorLayout.error.text =
            if (exception is SocketTimeoutException) resources.getString(R.string.no_internet_connection_msg) else
                resources.getString(R.string.something_went_wrong)
        binding.errorLayout.retry.setOnClickListener {
            user?.login?.let { viewModel.fetchUser(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val REPO_OWNER_KEY = "repo_owner_key"

        fun newInstance(ownerDTO: OwnerDTO) = UserFragment().apply {
            arguments = Bundle().apply {
                putParcelable(REPO_OWNER_KEY, ownerDTO)
            }
        }
    }
}