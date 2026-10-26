package com.partners.hdfils_agent.presentation.ui.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.partners.hdfils_agent.data.shared.StoreData
import com.partners.hdfils_agent.data.utils.Constants.Companion.listDevise
import com.partners.hdfils_agent.data.utils.Constants.Companion.listTranche
import com.partners.hdfils_agent.data.utils.EndPoint.clientAction
import com.partners.hdfils_agent.domain.models.Client
import com.partners.hdfils_agent.domain.models.ClientCreate
import com.partners.hdfils_agent.domain.remote.ClientKtor
import com.partners.hdfils_agent.domain.remote.ResponseAPI
import com.partners.hdfils_agent.presentation.ui.components.AppField
import com.partners.hdfils_agent.presentation.ui.components.AppLogo
import com.partners.hdfils_agent.presentation.ui.components.PrimaryButton
import com.partners.hdfils_agent.presentation.ui.components.SearchableCommuneField
import com.partners.hdfils_agent.presentation.ui.components.TrancheSelect
import com.partners.hdfils_agent.presentation.ui.theme.AppBackground
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlueDeep
import com.partners.hdfils_agent.presentation.ui.theme.Danger
import com.partners.hdfils_agent.presentation.ui.theme.InkMuted
import io.ktor.client.call.body
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun InscriptionPage(isConnected: Boolean) {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var isActive by remember { mutableStateOf(true) }
    var resetKey by remember { mutableIntStateOf(0) }
    var communeId by remember { mutableIntStateOf(0) }
    var nom by remember { mutableStateOf("") }
    var prenom by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var avenue by remember { mutableStateOf("") }
    var quartier by remember { mutableStateOf("") }
    var numeroParcelle by remember { mutableStateOf("") }
    var montant by remember { mutableStateOf("") }
    var devise by remember { mutableStateOf("") }
    var nombreTranches by remember { mutableStateOf("") }
    var formError by remember { mutableStateOf<String?>(null) }
    val ctx = LocalContext.current

    fun validate(): String? = when {
        communeId == 0 -> "Sélectionnez une commune"
        nom.isBlank() -> "Le nom est obligatoire"
        prenom.isBlank() -> "Le prénom est obligatoire"
        telephone.isBlank() -> "Le téléphone est obligatoire"
        montant.toIntOrNull() == null || montant.toInt() <= 0 -> "Le montant de souscription est obligatoire"
        devise.isBlank() -> "Sélectionnez une devise"
        nombreTranches.toIntOrNull() == null -> "Sélectionnez le nombre de tranches"
        else -> null
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 28.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppLogo(size = 78.dp)
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "CasaNayo Team Salubrite",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandBlueDeep,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Inscription client",
                    fontSize = 14.sp,
                    color = InkMuted,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )
                SearchableCommuneField(
                    resetKey = resetKey,
                    selectedId = communeId,
                    onSelect = { communeId = it.id }
                )
                Spacer(Modifier.height(10.dp))
                AppField(
                    value = nom,
                    onValueChange = { nom = it },
                    label = "Nom",
                    icon = Icons.Default.Person
                )
                Spacer(Modifier.height(8.dp))
                AppField(
                    value = prenom,
                    onValueChange = { prenom = it },
                    label = "Prénom",
                    icon = Icons.Default.Person
                )
                Spacer(Modifier.height(8.dp))
                AppField(
                    value = telephone,
                    onValueChange = { telephone = it },
                    label = "Téléphone",
                    icon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth()) {
                    AppField(
                        value = avenue,
                        onValueChange = { avenue = it },
                        label = "Avenue",
                        icon = Icons.Default.Place,
                        modifier = Modifier.weight(1.4f)
                    )
                    Spacer(Modifier.width(8.dp))
                    AppField(
                        value = numeroParcelle,
                        onValueChange = { numeroParcelle = it },
                        label = "N°",
                        icon = Icons.Default.Home,
                        modifier = Modifier.weight(0.8f)
                    )
                }
                Spacer(Modifier.height(8.dp))
                AppField(
                    value = quartier,
                    onValueChange = { quartier = it },
                    label = "Quartier",
                    icon = Icons.Default.Place
                )
                Spacer(Modifier.height(8.dp))
                AppField(
                    value = montant,
                    onValueChange = { montant = it.filter(Char::isDigit).take(9) },
                    label = "Montant de souscription",
                    icon = Icons.Default.Info,
                    keyboardType = KeyboardType.Number
                )
                Spacer(Modifier.height(8.dp))
                TrancheSelect(
                    label = "Devise",
                    placeholder = "FC ou USD",
                    selected = devise,
                    onSelect = { devise = it },
                    options = listDevise
                )
                Spacer(Modifier.height(8.dp))
                TrancheSelect(
                    label = "Nombre de tranches",
                    placeholder = "Sélectionnez",
                    selected = nombreTranches,
                    onSelect = { nombreTranches = it },
                    options = listTranche
                )
                if (formError != null) {
                    Text(
                        text = formError.orEmpty(),
                        color = Danger,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))
                PrimaryButton(
                    text = "Enregistrer",
                    loading = !isActive,
                    onClick = {
                        val error = validate()
                        formError = error
                        if (error != null) return@PrimaryButton
                        if (!isConnected) {
                            Toast.makeText(
                                ctx,
                                "Vous n'êtes pas connecté. Vérifiez votre connexion.",
                                Toast.LENGTH_LONG
                            ).show()
                            return@PrimaryButton
                        }
                        if (!isActive) return@PrimaryButton
                        isActive = false
                        focusManager.clearFocus()
                        coroutineScope.launch {
                            try {
                                val created = ClientCreate(
                                    nom = nom.trim(),
                                    prenom = prenom.trim(),
                                    telephone = telephone.trim(),
                                    commune_id = communeId,
                                    avenue = avenue.trim(),
                                    quartier = quartier.trim(),
                                    numero_parcelle = numeroParcelle.trim(),
                                    montant_souscription = montant.toInt(),
                                    devise = devise,
                                    nombre_tranches = nombreTranches.toInt()
                                )
                                val response = ClientKtor().postData(clientAction, created)
                                when (val status = response.status.value) {
                                    in 200..299 -> {
                                        val res = response.body<ResponseAPI>()
                                        val store = StoreData(ctx)
                                        val existing = store.getDataClient.first().filterNotNull()
                                        store.saveDataClient(
                                            existing + Client(
                                                nom = created.nom,
                                                prenom = created.prenom,
                                                telephone = created.telephone,
                                                avenue = created.avenue,
                                                quartier = created.quartier,
                                                numero_parcelle = created.numero_parcelle,
                                                commune_id = created.commune_id,
                                                montant_souscription = created.montant_souscription,
                                                devise = created.devise,
                                                nombre_tranches = created.nombre_tranches
                                            )
                                        )
                                        Toast.makeText(ctx, res.message, Toast.LENGTH_LONG).show()
                                        nom = ""
                                        prenom = ""
                                        numeroParcelle = ""
                                        telephone = ""
                                        avenue = ""
                                        quartier = ""
                                        montant = ""
                                        devise = ""
                                        nombreTranches = ""
                                        communeId = 0
                                        formError = null
                                        resetKey += 1
                                    }
                                    in 500..599 -> {
                                        Toast.makeText(ctx, "Erreur serveur", Toast.LENGTH_LONG).show()
                                    }
                                    in 400..499 -> {
                                        val res = response.body<ResponseAPI>()
                                        formError = res.message
                                        Toast.makeText(ctx, res.message, Toast.LENGTH_LONG).show()
                                    }
                                    else -> {
                                        Toast.makeText(ctx, "Réponse inattendue ($status)", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } catch (error: Exception) {
                                Toast.makeText(
                                    ctx,
                                    "Enregistrement impossible. Réessayez.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } finally {
                                isActive = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
@Preview
fun InscriptionPageBody() {
    InscriptionPage(isConnected = false)
}
