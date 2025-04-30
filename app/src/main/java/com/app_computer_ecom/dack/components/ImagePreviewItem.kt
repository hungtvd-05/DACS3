package com.app_computer_ecom.dack.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.R

@Composable
fun ImagePreviewItem(
    uri: String,
    onClick: () -> Unit,
    onClickShowHidden: () -> Unit,
    isEnable: Boolean,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            model = uri,
            contentDescription = "",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )

        Row {
            IconButton(onClick = { onClick() }) {
                Icon(imageVector = Icons.Default.Delete,
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.dp),
                    tint = Color.Red
                )
            }
            IconButton(onClick = { onClickShowHidden() }) {
                Icon(painter = painterResource(id = if (isEnable) R.drawable.eye_svgrepo_com else R.drawable.eye_slash_svgrepo_com),
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.dp),
                    tint = Color(30, 136, 229)
                )
            }
        }
    }
}