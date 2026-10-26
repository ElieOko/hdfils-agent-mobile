package com.partners.hdfils_agent.presentation.ui.pages

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.partners.hdfils_agent.R
import com.partners.hdfils_agent.data.shared.StoreData
import com.partners.hdfils_agent.data.utils.EndPoint
import com.partners.hdfils_agent.data.utils.EndPoint.trashAction
import com.partners.hdfils_agent.domain.models.Trash
import com.partners.hdfils_agent.domain.models.TrashClean
import com.partners.hdfils_agent.domain.remote.ClientKtor
import com.partners.hdfils_agent.domain.remote.ResponseAPI
import com.partners.hdfils_agent.domain.remote.ResponseAPIClient
import com.partners.hdfils_agent.domain.remote.ResponseAPIGeneric
import com.partners.hdfils_agent.domain.remote.ResponseAPIGenericClean
import com.partners.hdfils_agent.domain.remote.ResponseAPIGenirc
import com.partners.hdfils_agent.presentation.ui.components.Label
import com.partners.hdfils_agent.presentation.ui.components.MAlertDialog
import com.partners.hdfils_agent.presentation.ui.components.MButtonIcon
import com.partners.hdfils_agent.presentation.ui.components.MCard
import com.partners.hdfils_agent.presentation.ui.components.Space
import com.partners.hdfils_agent.presentation.ui.components.TopBar
import io.ktor.client.call.body
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
@Preview
fun CheckBopeto(isConnected: Boolean = false) {
    var isActive by remember { mutableStateOf(true) }
    var isShow by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf("") }
    var titleMsg by remember { mutableStateOf("Erreur") }
    val sheetState = rememberModalBottomSheetState()
    var onclick : ()-> Unit = {isShow = false}
    val context = LocalContext.current
    val listTrash = remember { mutableStateListOf<TrashClean>() }
    val listTrashDetail = remember { mutableStateListOf<TrashClean>(TrashClean(
        id = null,
        client_id =null,
        state_trash_id = null,
        is_active = null,
        created_at = null,
        client =null
    )) }
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val scaffoldState = rememberBottomSheetScaffoldState()
    val  scope = rememberCoroutineScope()

    Column(Modifier.background(Color(0xFFF4F4F8))) {
        scope.launch {
            StoreData(context).getDataClientTrash.collect{
                if(it.isNotEmpty()){
                    it.map { i->
                        listTrash.add(i)
                    }
                }
            }
        }
        TopBar(
            onclick = {
                try {
                    if(isConnected){
                        scope.launch {
                            val response = ClientKtor().getData(EndPoint.clientAction)
                            val status = response.status.value
                            when(status){
                                in 200..299 ->{
                                    val res = response.body<ResponseAPIClient>()
                                    Toast.makeText(context,"Mises à jours", Toast.LENGTH_LONG).show()
                                    Log.i("",
                                        res.toString()
                                    )
                                    scope.launch {
                                        StoreData(context).saveDataClient(res.clients)
                                    }
//                                    trashCleaner.add(TrashClean(
//                                        id = res.data.id,
//                                        state_trash_id = res.data.state_trash_id,
//                                        created_at = res.data.created_at,
//                                        client_id = res.data.client_id))
//                                    scope.launch {
//                                        StoreData(context).saveDataTrash(trashCleaner[0])
//                                    }
                                }
                                in 500..599 ->{
                                    Toast.makeText(context,"Erreur serveur", Toast.LENGTH_LONG).show()
                                }
                                in 400..499 ->{
                                    val res = response.body<ResponseAPI>()
                                    Toast.makeText(context,res.message, Toast.LENGTH_LONG).show()
                                }

                            }
                        }
                        scope.launch {
                            val response = ClientKtor().getData(EndPoint.trashAction)
                            val status = response.status.value
                            when(status){
                                in 200..299 ->{
                                    val res = response.body<ResponseAPIGeneric>()
                                    Toast.makeText(context,"Mises à jours", Toast.LENGTH_LONG).show()
                                    Log.i("",
                                        res.toString()
                                    )
                                    scope.launch {
                                        StoreData(context).saveDataTrash(res.trash_clients)
                                    }
//                                    trashCleaner.add(TrashClean(
//                                        id = res.data.id,
//                                        state_trash_id = res.data.state_trash_id,
//                                        created_at = res.data.created_at,
//                                        client_id = res.data.client_id))
//                                    scope.launch {
//                                        StoreData(context).saveDataTrash(trashCleaner[0])
//                                    }
                                }
                                in 500..599 ->{
                                    Toast.makeText(context,"Erreur serveur", Toast.LENGTH_LONG).show()
                                }
                                in 400..499 ->{
                                    val res = response.body<ResponseAPI>()
                                    Toast.makeText(context,res.message, Toast.LENGTH_LONG).show()
                                }

                            }
                        }
                    }
                    else{
                        Toast.makeText(context,"Vous n'êtes pas connecté veuillez vérifier votre connexion !!!",
                            Toast.LENGTH_LONG).show()
                    }
                }
                catch (e : Exception){
                    Toast.makeText(context,e.message, Toast.LENGTH_LONG).show()
                }
            }
        )
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContainerColor =  Color(0xFF8181E8),
            sheetContent = {
                if(listTrashDetail.isNotEmpty()){
                    val address = listTrashDetail[0].client?.address_client?.get(0)
                    val trash =  listTrashDetail[0]
                    var titleTrash = "propre"
                    when(trash.state_trash_id){
                        3 -> {
                            titleTrash = "pleine"
                        }
                        2 -> {
                            titleTrash = "en nettoyage"
                        }
                        1 ->{
                            titleTrash = "propre"
                        }
                    }
                    Column(Modifier.padding(10.dp).fillMaxWidth()) {
                        Row(Modifier.fillMaxWidth()) {
                            Label("Détails", size = 18, fontWeight = FontWeight.Bold, color = Color.White)
                            Space(x=5)
                            Icon(
                                painterResource(R.drawable.detail),
                                modifier = Modifier.size(23.dp),
                                contentDescription = null
                            )
                        }
                        Space(y=10)
                        Label("Etat de la poubelle", size = 16, fontWeight = FontWeight.Bold, color = Color.White)
                        Row(Modifier.fillMaxWidth()) {
                            Icon(painterResource(R.drawable.trash), modifier = Modifier.size(24.dp), contentDescription = null)
                            Space(x=10)
                            Text("Cette poubelle est $titleTrash", color = Color.White)
                        }

                        Space(y=10)
                        Label("Date", size = 16, fontWeight = FontWeight.Bold, color = Color.White)
                        Row(Modifier.fillMaxWidth()) {
                            Icon(painterResource(R.drawable.date), modifier = Modifier.size(24.dp), contentDescription = null)
                            Space(x=10)
                            Text("25-10-2025", color = Color.White)
                        }
                        Space(y=14)
                        HorizontalDivider()
                        Space(y=10)
                        Label("Adresse", size = 17, fontWeight = FontWeight.Bold, color = Color.White)
                        Space(y=10)
                        Label("Commune", size = 16, fontWeight = FontWeight.Bold, color = Color.White)
                        Row(Modifier.fillMaxWidth()) {
                            Icon(painterResource(R.drawable.location), modifier = Modifier.size(24.dp), contentDescription = null)
                            Space(x=10)
                            Text(listTrashDetail[0].client?.address_client?.get(0)?.commune_id.toString(), color = Color.White)
                        }
                        Space(y=10)
                        Label("Quartier", size = 16, fontWeight = FontWeight.Bold, color = Color.White)
                        Row(Modifier.fillMaxWidth()) {
                            Icon(painterResource(R.drawable.location), modifier = Modifier.size(24.dp), contentDescription = null)
                            Space(x=10)
                            address?.quartier?.let {
                                Text(
                                    it, color = Color.White)
                            }
                        }

                        Space(y=10)
                        Label("Avenue", size = 16, fontWeight = FontWeight.Bold, color = Color.White)
                        Row(Modifier.fillMaxWidth()) {
                            Icon(painterResource(R.drawable.street), modifier = Modifier.size(24.dp), contentDescription = null)
                            Space(x=10)
                            address?.avenue?.let { Text(it, color = Color.White) }
                        }
//                    HorizontalDivider()
                        Space(y=15)
                        Column(modifier = Modifier.padding(20.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painterResource(R.drawable.affiche),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop)
                        }
                        Space(y=40)
                    }
                }
            },
            sheetPeekHeight = 0.dp
        ) {
            Space(y=25)
            Column(Modifier.verticalScroll(rememberScrollState()).background(Color(0xFFF4F4F8))) {
                listTrash.map {
                    MCard(
                        items = it,
                        onClick = {
                        scope.launch {
                            listTrashDetail[0] = it
                            scaffoldState.bottomSheetState.expand()
                        }
                    },
                        onclickClean = {
                            if(it.state_trash_id == 3 || it.state_trash_id == 1){
                                titleMsg = "Information"
                                msg = "Votre poubelle est encours de nettoyage"
                                isShow = true
                                onclick = { isShow = false}
                                if(it.state_trash_id == 1){
                                    titleMsg = "Information"
                                    msg = "Cette poubelle est actuellement propre"
                                    isShow = true
                                    onclick = { isShow = false}
                                }
                            }
                            else{
                                if(isConnected){
                                    isActive = false
                                    scope.launch {
                                        delay(3000)
                                        val response = it.id?.let { it1 ->
                                            Trash(
                                                client_id = it1,
                                                state_trash_id = 2
                                            )
                                        }?.let { it2 ->
                                            ClientKtor().postData(
                                                trashAction,
                                                it2
                                            )
                                        }
                                        val status = response?.status?.value
                                        when(status){
                                            in 200..299 ->{
                                                isActive = true
                                                val res = response!!.body<ResponseAPIGenericClean>()
                                                Toast.makeText(context, res.message, Toast.LENGTH_LONG).show()
                                                msg = res.message
                                                titleMsg = "Success"
                                                isShow = true


                                            }
                                            in 500..599 ->{
                                                isActive = true
                                                msg = "Erreur serveur réssayer plus tard nous resolvons ce problème"
                                                isShow = true
                                            }
                                            in 400..499 ->{
                                                isActive = true
                                                val res = response?.body<ResponseAPI>()
                                                msg = res!!.message
                                                isShow = true
                                            }
                                        }
                                    }
                                }
                                else{
                                    titleMsg = "Success"
                                    msg = "Vous n'êtes pas connecté veuillez vérifier votre connexion !!!"
                                    isShow = true
                                }
                            }
                        }
                    )
                }

            }

            if(isShow){
                MAlertDialog(
                    dialogTitle = titleMsg,
                    dialogText =  msg,
                    onDismissRequest = {
                        isShow = false
                    },
                    onConfirmation = onclick
                )
            }
        }

    }

}