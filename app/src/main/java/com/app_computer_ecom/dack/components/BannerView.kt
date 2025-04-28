package com.app_computer_ecom.dack.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.model.BannerModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun BannerView(modifier: Modifier = Modifier) {
    var isLoading by remember { mutableStateOf(true) }

    var banners by remember {
        mutableStateOf(emptyList<BannerModel>())
    }

    LaunchedEffect(Unit) {
        banners = GlobalRepository.bannerRepository.getBannerByEnable()
        isLoading = false
    }

    Column(
        modifier = modifier
    ) {
        val pagerState = rememberPagerState(0) {
            banners.size
        }
        HorizontalPager(
            state = pagerState,
            pageSpacing = 24.dp,
        ) {
            AsyncImage(
                model = banners[it].imageUrl,
                contentDescription = "Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        DotsIndicator(
            dotCount = banners.size, type = ShiftIndicatorType(
                DotGraphic(
                    color = MaterialTheme.colorScheme.primary,
                    size = 6.dp
                )
            ),
            pagerState = pagerState
        )
    }


}

