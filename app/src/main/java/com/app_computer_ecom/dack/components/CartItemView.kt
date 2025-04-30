package com.app_computer_ecom.dack.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.LoadingScreen
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.model.PriceInfo
import com.app_computer_ecom.dack.model.ProductModel
import com.app_computer_ecom.dack.repository.GlobalRepository
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartItemView(
    pid: String,
    quantity: Int,
    selectType: PriceInfo,
    delete: () -> Unit,
    increse: () -> Unit,
    decrese: () -> Unit
) {
    var product by remember { mutableStateOf<ProductModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    LaunchedEffect(Unit) {
        product = GlobalRepository.productRepository.getProductById(pid)
        isLoading = false
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(146.dp)
            .padding(bottom = 8.dp)
    ) {
        if (isLoading) {
            LoadingScreen()
        } else {
            Row {
                AsyncImage(
                    model = product!!.imageUrls.firstOrNull()?.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                    placeholder = painterResource(id = R.drawable.loading_svgrepo_com)
                )
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = product!!.name,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        minLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 10.sp,
                        lineHeight = 12.sp

                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .background(Color(233, 233, 233))
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = selectType.type,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 10.sp, lineHeight = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        Column {
                            Text(
                                text = formatter.format(selectType.price),
                                fontWeight = FontWeight.SemiBold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        decrese()
                                    }
                                ) {
                                    Text(
                                        text = "-",
                                        fontSize = 20.sp
                                    )
                                }
                                Text(
                                    text = quantity.toString(),
                                    fontSize = 16.sp
                                )
                                IconButton(
                                    onClick = {
                                        increse()
                                    }
                                ) {
                                    Text(
                                        text = "+",
                                        fontSize = 20.sp
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                delete()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }

    }
}