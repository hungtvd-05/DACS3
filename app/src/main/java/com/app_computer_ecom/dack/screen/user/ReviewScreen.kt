package com.app_computer_ecom.dack.screen.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.components.TopBar
import com.app_computer_ecom.dack.model.RatingModel
import com.app_computer_ecom.dack.repository.GlobalRepository

@Composable
fun ReviewScreen(productId: String) {

    var listRatingModel by remember { mutableStateOf(emptyList<RatingModel>()) }
    LaunchedEffect(Unit) {
        listRatingModel =
            GlobalRepository.ratingAndCommentRepository.getRatingAndComment(productId)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    )
    {
        TopBar(title = "Đánh giá sản phẩm") {
            GlobalNavigation.navController.popBackStack()
        }

        LazyColumn {
            items(listRatingModel.size) {
                CommentItem(listRatingModel[it])
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}