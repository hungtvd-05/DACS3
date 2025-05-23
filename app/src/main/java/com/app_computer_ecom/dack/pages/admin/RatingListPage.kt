package com.app_computer_ecom.dack.pages.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.app_computer_ecom.dack.ui.theme.ThemeManager
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat

@Composable
fun RatingListPage(modifier: Modifier = Modifier) {
    var ratingList by remember { mutableStateOf(emptyList<RatingModel>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        GlobalRepository.ratingAndCommentRepository.getAllRatingAndComment().collectLatest { ratings ->
            ratingList = ratings
            isLoading = false
        }
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
                    itemsIndexed(ratingList, key = { index, item -> item.id }) { index, item ->
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
    var showReply by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                onClick = {
                    GlobalNavigation.navController.navigate("admin/reply-rating/ratingId=${ratingModel.id}")
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                SimpleDateFormat("dd-MM-yyyy HH:mm").format(ratingModel.createdAt.toDate()),
                fontSize = 8.sp,
                lineHeight = 8.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (ratingModel.commentModel.reply != "") {
                TextButton(
                    onClick = { showReply = !showReply },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.height(IntrinsicSize.Min)
                ) {
                    Row {
                        Text(
                            text = "Phản hồi người bán",
                            fontSize = 8.sp,
                            lineHeight = 8.sp
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "",
                            modifier = Modifier.size(10.dp),
                        )
                    }
                }
            }
        }
        if (showReply) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .background(
                        if (ThemeManager.isDarkTheme) {
                            Color(0xFF1A1A1A)
                        } else {
                            Color(0xFFF3F0F0)
                        }
                    )
                    .padding(8.dp)
            ) {
                Text(
                    "Phản hồi của người bán",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    ratingModel.commentModel.reply,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

    }
}