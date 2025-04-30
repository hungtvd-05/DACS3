package com.app_computer_ecom.dack.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_computer_ecom.dack.GlobalNavigation
import com.app_computer_ecom.dack.R
import com.app_computer_ecom.dack.model.ProductModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductItem(product: ProductModel) {

    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    Card(
        onClick = {
            GlobalNavigation.navController.navigate("product-details/productId=${product.id}")
        },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp),
//        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Column(
        ) {
            AsyncImage(
                model = product.imageUrls.firstOrNull()?.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.image_broken_svgrepo_com),
                placeholder = painterResource(id = R.drawable.loading_svgrepo_com)
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {

                Row {
                    product.prices.forEach(
                        action = {
                            Row(
                                modifier = Modifier
                                    .background(Color(233, 233, 233))
                                    .padding(horizontal = 4.dp)
                            ) {
                                Text(text = it.type, fontSize = 8.sp, lineHeight = 10.sp)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.name,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formatter.format(product.prices[0].price),
                    fontSize = 10.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold

                )
            }
        }
    }
}