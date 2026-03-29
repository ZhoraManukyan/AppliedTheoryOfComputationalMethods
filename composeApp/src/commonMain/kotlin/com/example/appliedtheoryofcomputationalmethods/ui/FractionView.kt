package com.example.appliedtheoryofcomputationalmethods.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FractionView(
    numerator: String,
    denominator: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {

        Box(
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(
                text = numerator,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .height(2.dp)
                .background(Color.Black)
        )

        Box(
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(
                text = denominator,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
