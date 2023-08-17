package com.example.marvelapp.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.marvelapp.databinding.FragmentCharactersBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.characters.adapters.CharactersAdapter
import com.example.marvelapp.presentation.characters.adapters.CharactersLoadMoreStateAdapter
import com.example.marvelapp.presentation.characters.adapters.CharactersRefreshStateAdapter
import com.example.marvelapp.presentation.detail.DetailViewArg
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader

    private val headerAdapter: CharactersRefreshStateAdapter by lazy {
        CharactersRefreshStateAdapter(
            // Duas maneiras de fazer a chamada
            // charactersAdapter.retry()
            charactersAdapter::retry,
        )
    }

    private val footerAdapter: CharactersLoadMoreStateAdapter by lazy {
        CharactersLoadMoreStateAdapter(
            // Duas maneiras de fazer a chamada
            // charactersAdapter.retry()
            charactersAdapter::retry,
        )
    }

    private val charactersAdapter: CharactersAdapter by lazy {
        CharactersAdapter(imageLoader) { character, view ->
            val extras = FragmentNavigatorExtras(
                view to character.name,
            )

            val directions = CharactersFragmentDirections
                .actionCharactersFragmentToDetailFragment(
                    character.name,
                    DetailViewArg(
                        characterId = character.id,
                        name = character.name,
                        imageUrl = character.imageUrl,
                    ),
                )

            findNavController().navigate(directions, extras)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentCharactersBinding
            .inflate(inflater, container, false)
            .apply {
                _binding = this
            }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCharactersAdapter()
        observeInitialLoadState()

        viewModel.state.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is CharactersViewModel.UiState.SearchResult -> {
                    //
                    // viewLifecycleOwner.lifecycle : vincula o ciclo de vida do Fragment ao PagingData.
                    //
                    charactersAdapter.submitData(viewLifecycleOwner.lifecycle, uiState.data)
                }
            }
        }

        viewModel.searchCharacters()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initCharactersAdapter() {
        postponeEnterTransition()
        with(binding.recyclerCharacters) {
            setHasFixedSize(true)
            adapter = charactersAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = footerAdapter,
            )
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }

    private fun observeInitialLoadState() {
        lifecycleScope.launch {
            charactersAdapter.loadStateFlow.collectLatest { loadState ->
                // Adapter Header
                headerAdapter.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf {
                        it is LoadState.Error && charactersAdapter.itemCount > 0
                    } ?: loadState.prepend

                // ViewFlipper
                binding.flipperCharacters.displayedChild =
                    //
                    // mediator : é referente aos dados remotos.
                    // source : é referente aos dados locais (Room).
                    //
                    when {
                        loadState.mediator?.refresh is LoadState.Loading -> {
                            setShimmerVisibility(true)
                            FLIPPER_CHILD_LOADING
                        }

                        //
                        // Se solicitamos dados remotos e obtemos erro e nao temos dados cacheados.
                        //
                        loadState.mediator?.refresh is LoadState.Error && charactersAdapter.itemCount == 0 -> {
                            setShimmerVisibility(false)
                            binding.includeErrorView.buttonRetry.setOnClickListener {
                                charactersAdapter.retry()
                            }
                            FLIPPER_CHILD_ERROR
                        }

                        //
                        // Sucesso! Buscamos os dados remotos e fizemos o cache local.
                        //
                        loadState.source.refresh is LoadState.NotLoading ||
                            loadState.mediator?.refresh is LoadState.NotLoading -> {
                            setShimmerVisibility(false)
                            FLIPPER_CHILD_CHARACTERS
                        }

                        //
                        // Casos desconhecidos.
                        //
                        else -> {
                            setShimmerVisibility(false)
                            FLIPPER_CHILD_CHARACTERS
                        }
                    }
            }
        }
    }

    private fun setShimmerVisibility(visibility: Boolean) {
        binding.includeViewCharactersLoadingState
            .shimmerCharacters.run {
                isVisible = visibility
                if (visibility) {
                    startShimmer()
                } else {
                    stopShimmer()
                }
            }
    }

    companion object {
        private const val FLIPPER_CHILD_LOADING = 0
        private const val FLIPPER_CHILD_CHARACTERS = 1
        private const val FLIPPER_CHILD_ERROR = 2
    }
}
