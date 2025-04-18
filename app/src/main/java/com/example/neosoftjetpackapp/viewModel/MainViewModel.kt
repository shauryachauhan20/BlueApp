package com.example.neosoftjetpackapp.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.neosoftjetpackapp.model.ContentListItem
import java.util.ArrayList
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.AndroidViewModel
import com.example.neosoftjetpackapp.R

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    val imageList =
        listOf(
            R.drawable.webp_101,
            R.drawable.webp_102,
            R.drawable.webp_103,
            R.drawable.webp_104,
            R.drawable.webp_101
        )

    private fun contentListFirst() =
        ArrayList<ContentListItem>().apply {
            add(
                ContentListItem(
                    R.drawable.webp_101,
                    context.getString(R.string.neoSoft_company),
                    context.getString(R.string.des_first)
                )
            )
            add(
                ContentListItem(
                    R.drawable.webp_102,
                    context.getString(R.string.neoSoft_developer),
                    context.getString(R.string.des_second)
                )
            )
            add(
                ContentListItem(
                    R.drawable.webp_103,
                    context.getString(R.string.backend_developer),
                    context.getString(R.string.des_third)
                )
            )
            add(
                ContentListItem(
                    R.drawable.webp_104,
                    context.getString(R.string.frontend_developer),
                    context.getString(R.string.des_fourth)
                )
            )
            add(
                ContentListItem(
                    R.drawable.webp_101,
                    context.getString(R.string.android_developer),
                    context.getString(R.string.des_fifth)
                )
            )
            add(
                ContentListItem(
                    R.drawable.webp_102,
                    context.getString(R.string.data_analyst),
                    context.getString(R.string.des_sixth)
                )
            )
            add(
                ContentListItem(
                    R.drawable.webp_103,
                    context.getString(R.string.devops),
                    context.getString(R.string.des_seventh)
                )
            )
        }

    private fun contentListSecond() = ArrayList<ContentListItem>().apply {
        add(
            ContentListItem(
                R.drawable.webp_104,
                context.getString(R.string.neoSoft_company_noida),
                context.getString(R.string.des_first)
            )
        )
        add(
            ContentListItem(
                R.drawable.webp_103,
                context.getString(R.string.neoSoft_developer_noida),
                context.getString(R.string.des_second)
            )
        )
        add(
            ContentListItem(
                R.drawable.webp_102,
                context.getString(R.string.backend_developer_noida),
                context.getString(R.string.des_third)
            )
        )
        add(
            ContentListItem(
                R.drawable.webp_101,
                context.getString(R.string.frontend_developer_noida),
                context.getString(R.string.des_fourth)
            )
        )
        add(
            ContentListItem(
                R.drawable.webp_104,
                context.getString(R.string.android_developer_noida),
                context.getString(R.string.des_fifth)
            )
        )
        add(
            ContentListItem(
                R.drawable.webp_103,
                context.getString(R.string.data_analyst_noida),
                context.getString(R.string.des_sixth)
            )
        )
        add(
            ContentListItem(
                R.drawable.webp_102,
                context.getString(R.string.devops_noida),
                context.getString(R.string.des_seventh)
            )
        )
        add(
            ContentListItem(
                R.drawable.webp_101,
                context.getString(R.string.tester_noida),
                context.getString(R.string.des_eighth)
            )
        )
        add(
            ContentListItem(
                R.drawable.webp_104,
                context.getString(R.string.em_noida),
                context.getString(R.string.des_ninth)
            )
        )
    }

    private val contentOriginalListMap = mapOf(
        0 to contentListFirst(),
        1 to contentListSecond(),
        2 to contentListFirst(),
        3 to contentListSecond(),
        4 to contentListFirst()
    )

    private val contentListMain = MutableLiveData<List<ContentListItem>>()
    val contentList: LiveData<List<ContentListItem>> = contentListMain

    private var originalList = emptyList<ContentListItem>()

    private val currentPage = mutableIntStateOf(0)
    val currentPageState: State<Int> = currentPage

    fun updateContent(position: Int) {
        currentPage.intValue = position
        originalList = contentOriginalListMap[position] ?: emptyList()
        contentListMain.value = originalList
    }

    fun filterContent(query: String) {
        contentListMain.value = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter {
                it.contentTitle.contains(query, ignoreCase = true)
            }
        }
    }
}