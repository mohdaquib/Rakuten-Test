package com.example.github.repositories.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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

        binding.newsList.layoutManager = LinearLayoutManager(context)
        viewModel.fetchItems()
        viewModel.repositories.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MainViewModel.SearchReposState.Success -> {
                    repositoryAdapter.setList(state.list.take(20).toMutableList())
                    repositoryAdapter.setOnClick {
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .add(android.R.id.content, DetailFragment.newInstance(it))
                            .addToBackStack("detail")
                            .commit()
                    }
                    binding.newsList.adapter = repositoryAdapter
                }
                is MainViewModel.SearchReposState.Error -> {

                }
            }
        }
    }
}