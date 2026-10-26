package com.partners.hdfils_agent.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.widget.Toast
import com.partners.hdfils_agent.R
import com.partners.hdfils_agent.data.shared.SyncResult
import com.partners.hdfils_agent.data.shared.syncLocalData
import com.partners.hdfils_agent.presentation.ui.theme.Ink
import com.partners.hdfils_agent.presentation.ui.theme.InkMuted
import kotlinx.coroutines.launch

@Composable
fun TopBar(
    isConnected: Boolean = true,
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val agent = rememberAgent()
    var askRefresh by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF307EC7),
                Color(0xFF1B5C96),
                Color(0xFF8ECAFF),
                Color(0xFF307EC7)
            )
        )
    }
    Column(Modifier.padding(10.dp)) {
        Space(y = 8)
        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.logo_casa),
                contentScale = ContentScale.Crop,
                contentDescription = "CasaSalubrite",
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape).size(140.dp).border(
                        BorderStroke(4.dp, rainbowColorsBrush),
                        CircleShape
                    )
            )
            Column(Modifier.padding(10.dp).weight(1f)) {
                Label(
                    "CasaNayo Team Salubrite",
                    color = Ink,
                    fontWeight = FontWeight.Bold
                )
                Space(y = 1)
                Label(
                    agent?.displayName() ?: "Agent",
                    color = InkMuted
                )
            }
            IconButton(
                onClick = { if (!loading) askRefresh = true },
                enabled = !loading
            ) {
                Icon(
                    painterResource(R.drawable.transfert),
                    contentDescription = "Actualiser",
                    modifier = Modifier.size(34.dp)
                )
            }
        }
    }
    if (askRefresh) {
        MAlertDialog(
            dialogTitle = "Actualisation",
            dialogText = "Voulez-vous actualiser les données de CasaSalubrite ?",
            onDismissRequest = { askRefresh = false },
            onConfirmation = {
                askRefresh = false
                if (!isConnected) {
                    Toast.makeText(
                        context,
                        "Vous n'êtes pas connecté. Vérifiez votre connexion.",
                        Toast.LENGTH_LONG
                    ).show()
                    return@MAlertDialog
                }
                scope.launch {
                    loading = true
                    val result = syncLocalData(context)
                    loading = false
                    val message = when (result) {
                        SyncResult.Success -> "Les données ont été mises à jour."
                        is SyncResult.Error -> result.message
                    }
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
        )
    }
    if (loading) {
        LoadingDialog(message = "Actualisation des données...")
    }
}

@Composable
@Preview
fun TopBarPreview(){
    TopBar()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(
    titleMain : String = "Immobilier",
    shadow : Int = 0,
    iconStart: ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
    iconEnd : ImageVector =  Icons.Filled.CheckCircle,
    isAction : Boolean = false,
    colorIcon : Color = Color.LightGray,
    sizeIcon : Int = 33,
    containerColor : Color = Color.White,
    clickStartAction : ()-> Unit = {},
    clickEndAction : ()-> Unit = {},
    paddingSurface : Int = 5,
    username: String ="elieoko"
){

    TopAppBar(
            title = {
                Column(Modifier.padding(5.dp)) {
                    Label("Welcome")
                    Label("@$username", modifier = Modifier.absoluteOffset(x = 7.dp, y = (-10).dp))
                }
                    },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xDC3A57E7).copy(0.9f)),
            navigationIcon = {
                IconButton(onClick = { clickStartAction() }) {
                    Image(
                        painter = painterResource(R.drawable.logo_casa),
                        contentDescription = "",
                        modifier = Modifier
                            .size(180.dp)
                            .clip(CircleShape)
                    )
                }
            },
            actions = {
                Row {
                    IconButton(onClick = {}) {
                        MIcon(Icons.Default.Notifications,)
                    }
                    IconButton(onClick = {}) {
                        MIcon(Icons.AutoMirrored.Filled.Send)
                    }
                }
            }
        )

}