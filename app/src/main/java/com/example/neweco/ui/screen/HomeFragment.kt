package com.example.neweco.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.neweco.adapter.HorizontalSliderAdapter
import com.example.neweco.databinding.FragmentHomeBinding
import com.example.neweco.ui.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var binding : FragmentHomeBinding
    private var isUserScrolling : Boolean = false
    private var automaticTaskIsRunning : Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        mainFunc()
        return binding.root
    }

    private fun mainFunc() {
        // Showing slider and handling the necessary api call.
        showSlider()


    }


    private fun showSlider() {
        val sliderAdapter = HorizontalSliderAdapter(emptyList(), requireContext())
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

}