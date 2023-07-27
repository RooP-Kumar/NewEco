package com.example.presentation.ui.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.core.network.data.Product
import com.example.core.viewmodels.ProductViewModel
import com.example.home.presentation.R
import com.example.home.presentation.databinding.FragmentHomeBinding
import com.example.presentation.adapter.HorizontalSliderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import com.example.core.R as coreR

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var binding : FragmentHomeBinding
    private var isUserScrolling : Boolean = false
    private var automaticTaskIsRunning : Boolean = false
    private val products : SnapshotStateList<Product> = mutableStateListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainFunc()
    }

    private fun mainFunc() {
        // Showing slider and handling the necessary api call.
        showSlider()
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    MainLayout(products)
                }
            }
        }
    }


    private fun showSlider() {
        val clickHandler = object: HorizontalSliderAdapter.OnClickHandler {
            override fun onItemClick(position: Int) {
                val data = Bundle().apply {
                    putString("productId", products[position].id)
                }
                findNavController().navigate(R.id.action_homeFragment_to_detailFragment, data)
            }
        }
        val sliderAdapter = HorizontalSliderAdapter(emptyList(), requireContext(), clickHandler)
        binding.horizontalSlider.adapter = sliderAdapter
        var timer = Timer()
        var automaticSlideTask = createTask(binding.horizontalSlider)

        // Custom scroll listener for view pager
        binding.horizontalSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    //
                    isUserScrolling = true
                    automaticTaskIsRunning = false
                    automaticSlideTask.cancel()
                    timer.cancel()
                } else if (state == ViewPager2.SCROLL_STATE_IDLE && isUserScrolling) {
                    timer = Timer()
                    automaticSlideTask = createTask(binding.horizontalSlider)
                    if(!automaticTaskIsRunning){
                        automaticTaskIsRunning = true
                        timer.scheduleAtFixedRate(automaticSlideTask, 5000L, 5000L)
                    }
                    isUserScrolling = false
                }
            }
        })

        // There could be some error in the server side or any network issue
        try {
            // Fetching data from firebase
            viewModel.getProducts()

            // After getting the data from firebase then observe that data using Live Data.
            viewModel.products.observe(viewLifecycleOwner) { products ->
                if(products.isNotEmpty()) {
                    // By default there is a progress bar running over this slider so when products list is not empty then setting that progress bar display to None.
                    binding.sliderStickCard.visibility = View.GONE
                    binding.loadingProgress.visibility = View.GONE
                    sliderAdapter.setData(products)
                    this.products.addAll(products)

                    // For circular scrolling setting current item to half of max value so that user can scroll both side backward or forward.
                    binding.horizontalSlider.setCurrentItem(Int.MAX_VALUE/2, false)

                    // Starting a timer with automatic task with 5000 millisecond (5 second). It will start initially
                    if(!automaticTaskIsRunning) { // If any user start scrolling before loading the data. then this code will not run. because there will another task already running
                        automaticTaskIsRunning = true
                        timer.scheduleAtFixedRate(automaticSlideTask, 5000L, 5000L)
                    }
                } else {
                    binding.sliderStickCard.visibility = View.VISIBLE
                    binding.loadingProgress.visibility = View.GONE
                    binding.errorProgress.visibility = View.VISIBLE
                }
            }
        } catch (e : Exception) {
            binding.sliderStickCard.visibility = View.VISIBLE
            binding.loadingProgress.visibility = View.GONE
            binding.errorProgress.visibility = View.VISIBLE
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun createTask(viewPager : ViewPager2) : TimerTask {
        val task = object : TimerTask() {
            override fun run() {
                viewPager.post {
                    var currentItem = viewPager.currentItem
                    ++currentItem
                    viewPager.setCurrentItem(currentItem, true)
                }
            }
        }

        return task
    }

    @Composable
    fun MainLayout(
        products: SnapshotStateList<Product>
    ) {
        LazyColumn(Modifier.fillMaxWidth()) {
            item {

                var targetIndex by remember { mutableStateOf(0) }
                val coroutineScope = rememberCoroutineScope()
                val scrollState = rememberLazyListState()

                LaunchedEffect(key1 = scrollState, block = {
                    snapshotFlow { scrollState.firstVisibleItemIndex }
                        .collect {
                            targetIndex = it
                        }
                })

                LazyRow(
                    Modifier
                        .fillMaxWidth(),
                    state = scrollState
                ) {
                    items(products, key = {it.id}){ product ->
                        LazyRowItem(product = product, Modifier.fillParentMaxWidth())
                    }
                }

                LaunchedEffect(key1 = targetIndex, block = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(targetIndex)
                    }
                })
            }
        }
    }

    @Composable
    fun LazyRowItem(
        product: Product,
        modifier: Modifier
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current).data(product.image).size(50).build()
        )
        Row(
            modifier
                .padding(coreR.dimen.generalSpace.dp)
                .background(Color.Cyan)
                .padding(20.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = "",
                modifier = Modifier
            )
            Column {
                Text(
                    text = product.title
                )
            }
        }
    }

}