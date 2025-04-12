package com.partners.hdfils_agent.presentation.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.partners.hdfils_agent.R
import com.partners.hdfils_agent.presentation.ui.components.MButtonIcon
import com.partners.hdfils_agent.presentation.ui.components.Space

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun CheckBopeto(){
    Scaffold {
        Column {
            Space(y=35)
            Card(Modifier.padding(10.dp),colors = CardDefaults.cardColors(
                containerColor = Color.White
            ), elevation = CardDefaults.cardElevation(5.dp)) {
                Row(modifier = Modifier.fillMaxWidth().padding(5.dp),verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.top), contentDescription = "", modifier = Modifier.size(30.dp), tint = Color.Red.copy(alpha = 0.5f))
                    Spacer(Modifier.width(10.dp))
                    Column() {
                        Text("Poubelle pleine")
                        Text("Client : ElieOko")
                        Text("Adresse : test 23 boffd")
                        Text("Depuis 25 Nov 2025")
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.Right) {
                        MButtonIcon(
                            textColor = Color.White,
                            label = "Vidage",
                            click = {},
                            icon = {
                                Icon(painterResource(R.drawable.trash), modifier = Modifier.size(23.dp), contentDescription = null)
                            }
                        )
//                        Space(x = 4)
//                        Column {
//                            Text("On arrive", Modifier.absoluteOffset(y = 12.dp))
//                        }

                    }
                }
            }
        }
    }
}