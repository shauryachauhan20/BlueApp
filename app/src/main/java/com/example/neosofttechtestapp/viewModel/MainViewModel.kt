package com.example.neosofttechtestapp.viewModel

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neosofttechtestapp.R
import com.example.neosofttechtestapp.dataSource.model.ContentListItem
import java.util.ArrayList

class MainViewModel(private val context: Context) : ViewModel() {
    val imageList =
        listOf(
            R.drawable.webp_101,
            R.drawable.webp_102,
            R.drawable.webp_103,
            R.drawable.webp_104,
            R.drawable.webp_101
        )

    private fun contentListFirst() = ArrayList<ContentListItem>().apply {
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_101),
                context.getString(R.string.neoSoft_company),
                context.getString(R.string.des_first)
            )
        )
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_102),
                context.getString(R.string.neoSoft_developer),
                context.getString(R.string.des_second)
            )
        )
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_103),
                context.getString(R.string.backend_developer),
                context.getString(R.string.des_third)
            )
        )
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_104),
                context.getString(R.string.frontend_developer),
                context.getString(R.string.des_fourth)
            )
        )
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_101),
                context.getString(R.string.android_developer),
                context.getString(R.string.des_fifth)
            )
        )
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_102),
                context.getString(R.string.data_analyst),
                context.getString(R.string.des_sixth)
            )
        )
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_103),
                context.getString(R.string.devops),
                context.getString(R.string.des_seventh)
            )
        )
    }

    private fun contentListSecond() = ArrayList<ContentListItem>().apply {
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_104),
                context.getString(R.string.neoSoft_company_noida),
                context.getString(R.string.des_first)
            )
        )
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_103),
                context.getString(R.string.neoSoft_developer_noida),
                context.getString(R.string.des_second)
            )
        )
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_102),
                context.getString(R.string.backend_developer_noida),
                context.getString(R.string.des_third)
            )
        )
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_101),
                context.getString(R.string.frontend_developer_noida),
                context.getString(R.string.des_fourth)
            )
        )
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_104),
                context.getString(R.string.android_developer_noida),
                context.getString(R.string.des_fifth)
            )
        )
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_103),
                context.getString(R.string.data_analyst_noida),
                context.getString(R.string.des_sixth)
            )
        )
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_102),
                context.getString(R.string.devops_noida),
                context.getString(R.string.des_seventh)
            )
        )
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_101),
                context.getString(R.string.tester_noida),
                context.getString(R.string.des_eighth)
            )
        )
        add(
            ContentListItem(
                AppCompatResources.getDrawable(context, R.drawable.webp_104),
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

    fun updateContent(position: Int) {
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