package com.example.neosoftjetpackapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.neosoftjetpackapp.model.ContentListItem
import com.example.neosoftjetpackapp.viewModel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val sheetState = rememberModalBottomSheetState()
    val openBottomSheet = remember { mutableStateOf(false) }

    val imageList = viewModel.imageList
    val currentPage = viewModel.currentPageState

    val contentList by viewModel.contentList.observeAsState(emptyList())
    var query by remember { mutableStateOf("") }

    if (openBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet.value = false },
            sheetState = sheetState
        ) {
            StatisticsBottomSheet(contentList)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openBottomSheet.value = true },
                containerColor = colorResource(id = R.color.sky_blue),
                shape = CircleShape
            ) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = stringResource(id = R.string.statistics)
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            CarouselView(imageList, currentPage.value, onPageChange = viewModel::updateContent)
            DotsIndicator(size = imageList.size, currentIndex = currentPage.value)

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.project_dimens_dp15)))
            SearchBar(
                query = query,
                onQueryChange = {
                    query = it
                    viewModel.filterContent(it)
                }
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.project_dimens_dp15)))
            ContentList(contentList, currentPage.value)
        }
    }
}

@Composable
fun CarouselView(
    imageList: List<Int>,
    currentPage: Int,
    onPageChange: (Int) -> Unit,
    imageHeight: Dp = dimensionResource(id = R.dimen.project_dimens_dp200)
) {
    val pagerState = rememberPagerState(initialPage = currentPage) { imageList.size }

    LaunchedEffect(pagerState.currentPage) {
        onPageChange(pagerState.currentPage)
    }

    HorizontalPager(
        state = pagerState,
        pageSpacing = dimensionResource(id = R.dimen.project_dimens_dp8),
        modifier = Modifier
            .fillMaxWidth()
            .height(imageHeight)
    ) { page ->
        Box(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.project_dimens_dp40))
                .fillMaxSize()
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.project_dimens_dp16)))
        ) {
            Image(
                painter = painterResource(id = imageList[page]),
                contentDescription = stringResource(id = R.string.carousel_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}


@Composable
fun DotsIndicator(size: Int, currentIndex: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.project_dimens_dp8))
    ) {
        repeat(size) { index ->
            val color =
                if (index == currentIndex) colorResource(id = R.color.sky_blue) else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.project_dimens_dp4))
                    .size(dimensionResource(id = R.dimen.project_dimens_dp8))
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.project_dimens_dp40))
            .padding(horizontal = dimensionResource(id = R.dimen.project_dimens_dp35))
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.project_dimens_dp10)))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.project_dimens_dp12),
                    end = dimensionResource(id = R.dimen.project_dimens_dp12)
                )
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search_icon),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(dimensionResource(id = R.dimen.project_dimens_dp18))
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.project_dimens_dp2)))

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.search),
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}


@Composable
fun ContentList(items: List<ContentListItem>, page: Int) {
    val imageList = listOf(
        R.drawable.webp_101,
        R.drawable.webp_102,
        R.drawable.webp_103,
        R.drawable.webp_104,
        R.drawable.webp_101,
        R.drawable.webp_102,
        R.drawable.webp_103
    )

    LazyColumn(modifier = Modifier.padding(dimensionResource(id = R.dimen.project_dimens_dp8))) {
        items(items) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = dimensionResource(id = R.dimen.project_dimens_dp2),
                        horizontal = dimensionResource(id = R.dimen.project_dimens_dp14)
                    ),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.project_dimens_dp12)),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.blue_light)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.project_dimens_dp7)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.project_dimens_dp60))
                            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.project_dimens_dp12)))
                            .background(colorResource(id = R.color.sky_blue))
                    ) {
                        if (page in imageList.indices) {
                            Image(
                                painter = painterResource(id = item.imageRes),
                                contentDescription = stringResource(id = R.string.carousel_image),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                stringResource(id = R.string.image_not_found),
                                modifier = Modifier.padding(dimensionResource(id = R.dimen.project_dimens_dp8))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.project_dimens_dp12)))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.contentTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = item.contentDescription,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun StatisticsBottomSheet(list: List<ContentListItem>) {
    val combined = list.joinToString("") { it.contentTitle.lowercase() }
    val stats = combined.filter { it.isLetter() }
        .groupingBy { it }
        .eachCount()
        .toList()
        .sortedByDescending { it.second }
        .take(3)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.project_dimens_dp16))
    ) {
        Text(
            "${stringResource(id = R.string.list)} (${list.size} ${stringResource(id = R.string.items)})",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.project_dimens_dp8)))
        stats.forEach {
            Text("${it.first} = ${it.second}", fontSize = 16.sp)
        }
    }
}