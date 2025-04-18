package com.example.neosofttechtestapp.view

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.neosofttechtestapp.R
import com.example.neosofttechtestapp.adapter.CarouselAdapter
import com.example.neosofttechtestapp.adapter.ContentAdapter
import com.example.neosofttechtestapp.dataSource.model.ContentListItem
import com.example.neosofttechtestapp.databinding.ActivityMainBinding
import com.example.neosofttechtestapp.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by lazy { MainViewModel(this) }
    private lateinit var contentAdapter: ContentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.filterContent(text?.toString() ?: "")
        }

        binding.fabStats.setOnClickListener {
            viewModel.contentList.value?.let { list ->
                showStatisticsBottomSheet(list)
            }
        }

        binding.carouselViewPager.adapter = CarouselAdapter(viewModel.imageList)

        binding.carouselViewPager.post {
            viewModel.updateContent(0)
            setupDots(viewModel.imageList.size, 0)
        }

        recyclerViewSetup()

        contentObserverAndSet()


    }

    private fun recyclerViewSetup() {
        contentAdapter = ContentAdapter(emptyList())
        binding.contentRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.contentRecyclerView.adapter = contentAdapter
    }

    private fun contentObserverAndSet() {
        viewModel.contentList.observe(this) {
            contentAdapter = ContentAdapter(it)
            binding.contentRecyclerView.adapter = contentAdapter
        }

        binding.carouselViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.updateContent(position)
                setupDots(viewModel.imageList.size, position)
            }
        })
    }

    private fun setupDots(size: Int, currentPosition: Int) {
        binding.dotContainer.removeAllViews()

        for (i in 0 until size) {
            val dot = ImageView(this).apply {
                val params = LinearLayout.LayoutParams(
                    22,
                    22
                ).apply {
                    marginStart = 8
                    marginEnd = 8
                }

                layoutParams = params
                setImageResource(if (i == currentPosition) R.drawable.dot_selected else R.drawable.dot_unselected)
            }
            binding.dotContainer.addView(dot)
        }
    }

    private fun showStatisticsBottomSheet(currentList: List<ContentListItem>) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_statistics, null)
        dialog.setContentView(view)

        val countText = view.findViewById<TextView>(R.id.pageItemCount)
        val charStats = view.findViewById<TextView>(R.id.topCharStats)

        countText.text =
            getString(R.string.list) + " (" + currentList.size + " " + getString(R.string.items) + ")"

        val combinedText = currentList.joinToString("") { it.contentTitle.lowercase() }
        val freqMap = combinedText.filter { it.isLetter() }
            .groupingBy { it }
            .eachCount()
            .toList()
            .sortedByDescending { it.second }
            .take(3)

        charStats.text = freqMap.joinToString("\n") { "${it.first} = ${it.second}" }

        dialog.show()
    }

}