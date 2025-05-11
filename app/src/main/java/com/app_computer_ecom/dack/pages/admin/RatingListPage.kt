package com.app_computer_ecom.dack.pages.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.model.RatingModel
import com.app_computer_ecom.dack.repository.GlobalRepository

@Composable
fun RatingListPage(modifier: Modifier = Modifier) {
    var ratingList by remember { mutableStateOf(emptyList<RatingModel>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        ratingList = GlobalRepository.ratingAndCommentRepository.getAllRatingAndComment()
        isLoading = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .padding(top = 16.dp)
    ) {
        Text(text = "Đánh giá từ khách hàng", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))
        if (isLoading) {
            LoadingScreen()
        } else {
            if (ratingList.isEmpty()) {
                Text(text = "Không có đánh giá nào !!", fontSize = 14.sp)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(ratingList) { index, item ->
                        CommentItem(ratingModel = item)
                    }
                }
            }
        }
    }
}

@Composable
fun CommentItem(
    ratingModel: RatingModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                onClick = {
                    GlobalNavigation.navController.navigate("employee/reply-rating/ratingId=${ratingModel.id}")
                }
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ratingModel.avatar,
                contentDescription = "avatar",
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(50)),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                ratingModel.uname,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(ratingModel.rating) {
                Icon(
                    imageVector = Icons.Default.Star,
                    tint = Color(241, 179, 59, 255),
                    contentDescription = "rating",
                    modifier = Modifier
                        .size(10.dp)
                )
            }
        }
        Text(
            text = "Phân loại : ${ratingModel.selectType.type}",
            color = Color.Gray,
            fontSize = 12.sp
        )

        if (ratingModel.commentModel!!.content.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = ratingModel.commentModel.content,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        if (ratingModel.commentModel.imageUrls.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(ratingModel.commentModel.imageUrls) { index, item ->
                    AsyncImage(
                        model = item,
                        contentDescription = "",
                        modifier = Modifier.height(96.dp),
                        error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                        placeholder = painterResource(id = R.drawable.loading_svgrepo_com)
                    )
                }
            }
        }
    }
}