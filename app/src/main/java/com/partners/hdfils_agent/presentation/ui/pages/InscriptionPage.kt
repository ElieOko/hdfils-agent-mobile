package com.partners.hdfils_agent.presentation.ui.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.partners.hdfils_agent.R
import com.partners.hdfils_agent.data.utils.Constants.Companion.listCommune
import com.partners.hdfils_agent.data.utils.EndPoint.clientAction
import com.partners.hdfils_agent.domain.models.Client
import com.partners.hdfils_agent.domain.remote.ClientKtor
import com.partners.hdfils_agent.domain.remote.ResponseAPI
import com.partners.hdfils_agent.presentation.ui.components.SelectableOutlinedTextField
import com.partners.hdfils_agent.presentation.ui.components.Space
import io.ktor.client.call.body
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InscriptionPage(isConnected: Boolean) {
    val coroutineScope = rememberCoroutineScope()
    var isActive by remember { mutableStateOf(true) }
    var communeId by remember { mutableIntStateOf(0) }
    var nom by remember { mutableStateOf("") }
    var prenom by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var avenue by remember { mutableStateOf("") }
    var quartier by remember { mutableStateOf("") }
    var numeroParcelle by remember { mutableStateOf("") }
    var commune by remember { mutableStateOf("") }
    val ctx = LocalContext.current
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Space(y=30)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Blue.copy(alpha = 0.4f)
            )
        ) {
            Column(Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Space(y=2)
                Image(
                    painterResource(R.drawable.affiche),
                    contentDescription = "",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop)
                Space(y=4)
                Text(
                    text = "inscription Client",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Space(y = 6)

                SelectableOutlinedTextField(
                    title = "Commune",
                    textValue2 = commune,
                    itemList =listCommune,
                    onChangeText = {
                        Toast.makeText(ctx,"${it.id}",Toast.LENGTH_SHORT).show()
                     communeId = it.id
                    }
                )
                Space(y=5)
                OutlinedTextField(
                    value = nom,
                    onValueChange = { nom = it },
                    label = { Text("Nom") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.user_),
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
                Space(y=6)
                OutlinedTextField(
                    value = prenom,
                    onValueChange = { prenom = it },
                    label = { Text("Prenom") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.user_),
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
                Space(y=6)
                OutlinedTextField(
                    value = telephone,
                    onValueChange = { telephone = it },
                    label = { Text("Telephone") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.user_),
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
                Space(y=6)
                Row (Modifier.fillMaxWidth()){
                    OutlinedTextField(
                        value = avenue,
                        onValueChange = { avenue = it },
                        label = { Text("Avenue") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.user_),
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
                        shape = RoundedCornerShape(10.dp)
                    )
                    Space(x=6)
                    OutlinedTextField(
                        value = numeroParcelle,
                        onValueChange = { numeroParcelle = it },
                        label = { Text("N") },
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
                }
                Space(y=6)
                OutlinedTextField(
                    value = quartier,
                    onValueChange = { quartier = it },
                    label = { Text("Quartier") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.user_),
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
                Space(y=14)
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor =  Color.Black,
                        disabledContentColor = Color(0xFF080624),
                        disabledContainerColor = Color(0xFF080624)
                    ),
                    enabled = isActive,
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        if(isConnected){
                            coroutineScope.launch {
                                isActive = false
                                delay(6000)
                                val response = ClientKtor().postData(clientAction, Client(nom=nom,numero_parcelle=numeroParcelle, prenom = prenom, telephone = telephone, avenue = avenue, commune_id = communeId , quartier = quartier))
                                val status = response.status.value
                                when(status){
                                    in 200..299 ->{
                                        isActive = true
                                        val res = response.body<ResponseAPI>()
                                        Toast.makeText(ctx,res.message,Toast.LENGTH_LONG).show()
                                        nom             = ""
                                        prenom          = ""
                                        numeroParcelle = ""
                                        telephone       = ""
                                        avenue          = ""
                                        communeId      = 0
                                        quartier        = ""
                                    }
                                    in 500..599 ->{
                                        isActive = true
                                        Toast.makeText(ctx,"Erreur serveur",Toast.LENGTH_LONG).show()
                                    }
                                    in 400..499 ->{
                                        isActive = true
                                        val res = response.body<ResponseAPI>()
                                        Toast.makeText(ctx,res.message,Toast.LENGTH_LONG).show()
                                    }

                                }
                            }
                        }
                        else{
                            Toast.makeText(ctx,"Vous n'êtes pas connecté veuillez vérifier votre connexion !!!",Toast.LENGTH_LONG).show()
                        }

                    }
                ) {
                    Text(text = if(isActive) "Enregistrer" else "Chargement...", fontSize = 16.sp, color = Color.White)
                }
                Space(y = 5)
            }
        }
    }
}

@Composable
@Preview
fun InscriptionPageBody(){
    InscriptionPage(isConnected =false)
}