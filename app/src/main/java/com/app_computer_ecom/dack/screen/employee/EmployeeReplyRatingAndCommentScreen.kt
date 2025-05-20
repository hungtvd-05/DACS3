package com.app_computer_ecom.dack.screen.employee

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.model.RatingModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EmployeeReplyRatingAndCommentScreen(ratingId: String) {
    BackHandler(enabled = true) {
        GlobalNavigation.navController.navigate("employee/2") {
            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    var isLoading by remember { mutableStateOf(true) }
    var ratingModel by remember { mutableStateOf<RatingModel?>(null) }
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    var reply by remember { mutableStateOf("") }
    var isReply by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        ratingModel = GlobalRepository.ratingAndCommentRepository.getRatingAndCommentById(ratingId)
        reply = ratingModel!!.commentModel!!.reply
        isReply = reply.isNotEmpty()
        isLoading = false
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = {
                    GlobalNavigation.navController.navigate("employee/2") {
                        popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(
                    text = "Đánh giá - Phản hồi",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            if (isLoading) {
                LoadingScreen()
            } else {
                LazyColumn (
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(8.dp)
                        ) {
                            ProductItemRating(
                                ratingModel = ratingModel!!,
                            )
                            Text(
                                text = buildAnnotatedString {
                                    append("Tổng số tiền : ")
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(formatter.format(ratingModel!!.quantity * ratingModel!!.selectType.price))
                                    }
                                },
                                fontSize = 12.sp,
                                lineHeight = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
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
                                    model = ratingModel!!.avatar,
                                    contentDescription = "avatar",
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(50)),
                                    contentScale = ContentScale.Crop,
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    ratingModel!!.uname,
                                    fontSize = 12.sp,
                                    lineHeight = 14.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            Row {

                                IconButton(
                                    onClick = {

                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (ratingModel!!.rating >= 1) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.scrim
                                    )
                                }
                                IconButton(
                                    onClick = {

                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (ratingModel!!.rating >= 2) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.scrim
                                    )
                                }
                                IconButton(
                                    onClick = {

                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (ratingModel!!.rating >= 3) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.scrim
                                    )
                                }
                                IconButton(
                                    onClick = {

                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (ratingModel!!.rating >= 4) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.scrim
                                    )
                                }
                                IconButton(
                                    onClick = {

                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (ratingModel!!.rating == 5) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.scrim
                                    )
                                }
                            }
                            OutlinedTextField(
                                value = ratingModel!!.commentModel!!.content,
                                onValueChange = {

                                },
                                label = {
                                    Text(
                                        text = "Mô tả, đánh giá sản phẩm",
                                        fontSize = 12.sp
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 14.sp),
                                readOnly = true,
                                minLines = 3
                            )
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(8.dp)
                        ) {
                            OutlinedTextField(
                                value = reply,
                                onValueChange = {
                                    reply = it
                                },
                                label = {
                                    Text(
                                        text = "Phản hồi của shop",
                                        fontSize = 12.sp
                                    )
                                },
                                readOnly = isReply,
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 14.sp),
                                minLines = 3
                            )
                        }
                    }
                    if (!isReply) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        if (reply.isEmpty()) {
                                            AppUtil.showToast(context, "Bạn chưa phản hồi đánh giá !!!")
                                        } else {
                                            isLoading = true
                                            scope.launch {
                                                try {
                                                    var commentModel = ratingModel?.commentModel?.copy(reply = reply)
                                                    GlobalRepository.ratingAndCommentRepository.replyRatingAndComment(ratingModel!!.id, commentModel!!)
                                                } catch (e: Exception) {

                                                } finally {
                                                    GlobalNavigation.navController.navigate("employee/2") {
                                                        popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                                                            inclusive = false
                                                        }
                                                        launchSingleTop = true
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Phản hồi",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItemRating(
    ratingModel: RatingModel
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    Column {
        Row(
            modifier = Modifier
                .height(100.dp)
                .padding(bottom = 8.dp)
                .clickable(
                    onClick = {
                        GlobalNavigation.navController.navigate("product-details/productId=${ratingModel.pid}") {
                            popUpTo(GlobalNavigation.navController.graph.startDestinationId) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = ratingModel.pimageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                    placeholder = painterResource(id = R.drawable.loading_svgrepo_com),
                    modifier = Modifier.background(Color.White)
                )
            }
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = ratingModel.pname,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 3,
                    minLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .background(Color(233, 233, 233))
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = ratingModel.selectType.type,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 10.sp, lineHeight = 12.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row {
                    Text(
                        text = formatter.format(ratingModel.selectType.price),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "x${ratingModel.quantity}",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}