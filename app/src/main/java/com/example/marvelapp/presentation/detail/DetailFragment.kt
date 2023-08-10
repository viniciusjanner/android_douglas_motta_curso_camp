package com.example.marvelapp.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.marvelapp.databinding.FragmentDetailBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.extensions.showShortToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader

    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDetailBinding
            .inflate(inflater, container, false)
            .apply {
                _binding = this
            }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val detailViewArg = args.detailViewArg

        binding.imageCharacter.run {
            transitionName = detailViewArg.name
            imageLoader.load(this, detailViewArg.imageUrl)
        }

        setSharedElementTransitionOnEnter()

        loadCategoriesAndObserveUiState(detailViewArg)

        setAndObserveFavoriteUiState(detailViewArg)
    }

    private fun loadCategoriesAndObserveUiState(detailViewArg: DetailViewArg) {
        viewModel.categories
            .run {
                actionLoad(detailViewArg.characterId)

                state.observe(viewLifecycleOwner) { uiState ->
                    // ViewFlipper
                    binding.flipperDetail.displayedChild =
                        // State
                        when (uiState) {
                            CategoriesUiActionStateLiveData.UiState.Loading -> {
                                FLIPPER_CHILD_POSITION_LOADING
                            }

                            is CategoriesUiActionStateLiveData.UiState.Success -> {
                                binding.recyclerParentDetail.run {
                                    setHasFixedSize(true)
                                    adapter = DetailParentAdapter(uiState.detailParentList, imageLoader)
                                }
                                FLIPPER_CHILD_POSITION_DETAIL
                            }

                            CategoriesUiActionStateLiveData.UiState.Error -> {
                                binding.includeErrorView.buttonRetry.setOnClickListener {
                                    viewModel.categories.actionLoad(detailViewArg.characterId)
                                }
                                FLIPPER_CHILD_POSITION_ERROR
                            }

                            CategoriesUiActionStateLiveData.UiState.Empty -> {
                                FLIPPER_CHILD_POSITION_EMPTY
                            }
                        }
                }
            }
    }

    private fun setAndObserveFavoriteUiState(detailViewArg: DetailViewArg) {
        viewModel.favorite
            .run {
                actionCheckFavorite(detailViewArg.characterId)

                binding.imageFavoriteIcon.setOnClickListener {
                    actionUpdateFavorite(detailViewArg)
                }

                state.observe(viewLifecycleOwner) { uiState ->
                    // ViewFlipper
                    binding.flipperFavorite.displayedChild =
                        // State
                        when (uiState) {
                            FavoriteUiActionStateLiveData.UiState.Loading -> {
                                FLIPPER_FAVORITE_CHILD_POSITION_LOADING
                            }

                            is FavoriteUiActionStateLiveData.UiState.Icon -> {
                                binding.imageFavoriteIcon.setImageResource(uiState.icon)
                                FLIPPER_FAVORITE_CHILD_POSITION_IMAGE
                            }

                            is FavoriteUiActionStateLiveData.UiState.Error -> {
                                showShortToast(uiState.messageResId)
                                FLIPPER_FAVORITE_CHILD_POSITION_IMAGE
                            }
                        }
                }
            }
    }

    // Define a animação da transição como "move"
    private fun setSharedElementTransitionOnEnter() {
        TransitionInflater
            .from(requireContext())
            .inflateTransition(android.R.transition.move)
            .apply {
                sharedElementEnterTransition = this
            }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val FLIPPER_CHILD_POSITION_LOADING = 0
        private const val FLIPPER_CHILD_POSITION_DETAIL = 1
        private const val FLIPPER_CHILD_POSITION_ERROR = 2
        private const val FLIPPER_CHILD_POSITION_EMPTY = 3

        private const val FLIPPER_FAVORITE_CHILD_POSITION_IMAGE = 0
        private const val FLIPPER_FAVORITE_CHILD_POSITION_LOADING = 1
    }
}
