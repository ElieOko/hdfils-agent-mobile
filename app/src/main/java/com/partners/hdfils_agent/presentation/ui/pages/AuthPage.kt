package com.partners.hdfils_agent.presentation.ui.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.partners.hdfils_agent.presentation.ui.components.Space
import com.partners.hdfils_agent.presentation.ui.theme.AnimatedBackgroundShapes
import io.ktor.client.call.body
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.partners.hdfils_agent.R
import com.partners.hdfils_agent.data.shared.StoreData
import com.partners.hdfils_agent.data.utils.EndPoint.agentAuth
import com.partners.hdfils_agent.domain.models.Agent
import com.partners.hdfils_agent.domain.models.AgentAuth
import com.partners.hdfils_agent.domain.models.TrashClean
import com.partners.hdfils_agent.domain.remote.ClientKtor
import com.partners.hdfils_agent.domain.remote.ResponseAPI
import com.partners.hdfils_agent.domain.remote.ResponseAPIGenirc
import com.partners.hdfils_agent.domain.route.ScreenRoute

@Composable

fun AuthPage(navC: NavHostController, isConnected: Boolean) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var matricule by remember { mutableStateOf("") }
    var isAnimating by remember { mutableStateOf(false) }
    var isActive by remember { mutableStateOf(true) }
    val ctx = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Ajouter les formes en arrière-plan
        AnimatedBackgroundShapes()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF3E4EBD)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Space(y=5)
                    Image(
                        painterResource(R.drawable.affiche),
                        contentDescription = "",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop)
                    Space(y=10)
                    Text(
                        text = "Connexion",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Agent sur terrain pour la salubrite",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = matricule,
                        onValueChange = { matricule = it },
                        label = { Text("Code Agent") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.house),
                                contentDescription = null,
                                modifier = Modifier.size(25.dp),
                                tint = Color.White
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            cursorColor = Color.White,
                            focusedLeadingIconColor = Color.White,
                            unfocusedLeadingIconColor = Color.White
                        ),
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Space(y=20)
                    Button(
                        onClick = {
                            try {
                                if(isConnected){
                                    coroutineScope.launch {
                                        isActive = false
                                        delay(6000)
                                        val response = ClientKtor().postData(agentAuth,AgentAuth(matricule))
                                        val status = response.status.value
                                        when(status){
                                            in 200..299 ->{
                                                isActive = true
                                                val res = response.body<ResponseAPIGenirc>()
                                                Toast.makeText(context,res.message,Toast.LENGTH_LONG).show()
                                                scope.launch {
                                                    StoreData(context).saveDataAgentAuth(
                                                        Agent(
                                                            code = res.agent.code,
                                                            nom = res.agent.nom,
                                                            postnom = res.agent.postnom,
                                                            prenom = res.agent.prenom,
                                                            genre = res.agent.genre,
                                                            telephone = res.agent.telephone,
                                                            address = res.agent.address)
                                                    )
                                                }
                                                if(res.trash_client.isNotEmpty()){
                                                    scope.launch {
                                                        val x = res.trash_client.toList()
                                                        StoreData(context).saveDataClientTrash(x)
                                                    }
                                                }

                                                if (res.client.isNotEmpty()){
                                                    scope.launch {
                                                        StoreData(context).saveDataClient(res.client)
                                                    }
                                                }
                                                navC.navigate(route = ScreenRoute.Home.name){
                                                    popUpTo(navC.graph.id){
                                                        inclusive = true
                                                    }
                                                }
                                            }
                                            in 500..599 ->{
                                                isActive = true
                                                Toast.makeText(context,"Erreur serveur",Toast.LENGTH_LONG).show()
                                            }
                                            in 400..499 ->{
                                                isActive = true
                                                val res = response.body<ResponseAPI>()
                                                Toast.makeText(context,res.message,Toast.LENGTH_LONG).show()
                                            }

                                        }
                                    }
                                }
                                else{
                                    Toast.makeText(ctx,"Vous n'êtes pas connecté veuillez vérifier votre connexion !!!",Toast.LENGTH_LONG).show()
                                }
                            }
                            catch (e:Exception){
                                e.message?.let {
                                    Log.e("ERROR************************************",
                                        it,)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor =  Color.Black,
                            disabledContentColor = Color(0xFF080624),
                            disabledContainerColor = Color(0xFF080624)
                        ),
                        enabled = isActive,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = if(isActive) "Se connecter" else "Chargement...", fontSize = 16.sp, color = Color.White)
                    }
                    Space(y=30)
                }
            }
        }
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Top) {
            if(!isActive){
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    trackColor =Color(0xFF070524),
                )
            }
        }
    }
}