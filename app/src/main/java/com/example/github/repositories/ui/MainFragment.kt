package com.example.github.repositories.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    @Inject
    lateinit var repositoryAdapter: RepositoryAdapter
    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
        viewModel.refreshRepos.observe(viewLifecycleOwner) { state ->
            when(state){
                is MainViewModel.RefreshRepoState.Success -> {
                    binding.swipeRefresh.isRefreshing = false
                    setAdapter(state.list.take(20).toMutableList())
                }
                is MainViewModel.RefreshRepoState.Error -> {

                }
            }
        }

        binding.newsList.layoutManager = LinearLayoutManager(context)
        binding.newsList.adapter = repositoryAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchItems()
        viewModel.repositories.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MainViewModel.SearchReposState.Loading -> binding.loading.isVisible = true
                is MainViewModel.SearchReposState.Success -> {
                    binding.loading.isVisible = false
                    setAdapter(state.list.take(20))
                }
                is MainViewModel.SearchReposState.Error -> {
                    binding.loading.isVisible = false
                }
            }
        }
    }

    private fun setAdapter(list: List<RepositoryDTO>) {
        repositoryAdapter.submitList(list.toMutableList())
        repositoryAdapter.setOnClick {
            startDetailFragment(it)
        }
    }

    private fun startDetailFragment(repositoryDTO: RepositoryDTO){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, DetailFragment.newInstance(repositoryDTO))
            .addToBackStack("detail")
            .commit()
    }
}