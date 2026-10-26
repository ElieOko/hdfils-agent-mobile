package com.partners.hdfils_agent.presentation.ui.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.partners.hdfils_agent.data.shared.StoreData
import com.partners.hdfils_agent.data.utils.EndPoint.clientAction
import com.partners.hdfils_agent.domain.models.Address
import com.partners.hdfils_agent.domain.models.Client
import com.partners.hdfils_agent.domain.models.ClientS
import com.partners.hdfils_agent.domain.models.Souscription
import com.partners.hdfils_agent.domain.models.Tranche
import com.partners.hdfils_agent.domain.models.communeLabel
import com.partners.hdfils_agent.domain.remote.ClientKtor
import com.partners.hdfils_agent.domain.remote.ResponseAPI
import com.partners.hdfils_agent.domain.remote.ResponseCreateClient
import com.partners.hdfils_agent.presentation.ui.components.AppField
import com.partners.hdfils_agent.presentation.ui.components.AppLogo
import com.partners.hdfils_agent.presentation.ui.components.DeviseSelector
import com.partners.hdfils_agent.presentation.ui.components.PrimaryButton
import com.partners.hdfils_agent.presentation.ui.components.ScreenHeader
import com.partners.hdfils_agent.presentation.ui.components.SearchableCommuneField
import com.partners.hdfils_agent.presentation.ui.components.SubscriptionBlock
import com.partners.hdfils_agent.presentation.ui.theme.AppBackground
import com.partners.hdfils_agent.presentation.ui.theme.Danger
import com.partners.hdfils_agent.presentation.ui.theme.Ink
import com.partners.hdfils_agent.presentation.ui.theme.InkMuted
import io.ktor.client.call.body
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun InscriptionPage(isConnected: Boolean) {
    val coroutineScope = rememberCoroutineScope()
    val ctx = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    var formVersion by remember { mutableIntStateOf(0) }
    var communeId by remember { mutableIntStateOf(0) }
    var nom by remember { mutableStateOf("") }
    var prenom by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var avenue by remember { mutableStateOf("") }
    var quartier by remember { mutableStateOf("") }
    var numeroParcelle by remember { mutableStateOf("") }
    var devise by remember { mutableStateOf("FC") }
    var montantText by remember { mutableStateOf("") }
    var tranchesText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf<ResponseCreateClient?>(null) }

    val montant = montantText.toIntOrNull() ?: 0
    val nombreTranches = tranchesText.toIntOrNull() ?: 0
    val preview = remember(montant, nombreTranches, devise) {
        if (montant > 0 && nombreTranches in 1..24) {
            val base = montant / nombreTranches
            val remainder = montant % nombreTranches
            Souscription(
                montant_souscription = montant,
                devise = devise,
                nombre_tranches = nombreTranches,
                tranche = trancheLabel(nombreTranches),
                tranches = List(nombreTranches) { index ->
                    Tranche(
                        numero = index + 1,
                        montant = base + if (index == nombreTranches - 1) remainder else 0
                    )
                }
            )
        } else {
            null
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        ScreenHeader(
            title = "Nouveau client",
            subtitle = "Adresse et souscription"
        )
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Identité", color = Ink, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    "Le client est rattaché à la commune choisie.",
                    color = InkMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                SearchableCommuneField(
                    resetKey = formVersion,
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
                Spacer(Modifier.height(10.dp))
                AppField(
                    value = prenom,
                    onValueChange = { prenom = it },
                    label = "Prénom",
                    icon = Icons.Default.Person
                )
                Spacer(Modifier.height(10.dp))
                AppField(
                    value = telephone,
                    onValueChange = { telephone = it.filter { ch -> ch.isDigit() || ch == '+' }.take(16) },
                    label = "Téléphone",
                    icon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone
                )
                Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth()) {
                    AppField(
                        value = avenue,
                        onValueChange = { avenue = it },
                        label = "Avenue",
                        icon = Icons.Default.Place,
                        modifier = Modifier.weight(1.5f)
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
                Spacer(Modifier.height(10.dp))
                AppField(
                    value = quartier,
                    onValueChange = { quartier = it },
                    label = "Quartier",
                    icon = Icons.Default.Place
                )

                Spacer(Modifier.height(18.dp))
                Text("Souscription", color = Ink, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    "Montant total, devise et nombre de tranches.",
                    color = InkMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                DeviseSelector(selected = devise, onSelect = { devise = it })
                Spacer(Modifier.height(10.dp))
                AppField(
                    value = montantText,
                    onValueChange = { montantText = it.filter { ch -> ch.isDigit() }.take(9) },
                    label = "Montant de la souscription",
                    icon = Icons.Default.Star,
                    keyboardType = KeyboardType.Number
                )
                Spacer(Modifier.height(10.dp))
                AppField(
                    value = tranchesText,
                    onValueChange = { tranchesText = it.filter { ch -> ch.isDigit() }.take(2) },
                    label = "Nombre de tranches",
                    icon = Icons.Default.DateRange,
                    keyboardType = KeyboardType.Number
                )
                preview?.let {
                    Spacer(Modifier.height(14.dp))
                    Text("Aperçu des tranches", color = InkMuted, fontSize = 12.sp)
                    Spacer(Modifier.height(6.dp))
                    SubscriptionBlock(it)
                }
                error?.let {
                    Text(
                        text = it,
                        color = Danger,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))
                PrimaryButton(
                    text = "Enregistrer le client",
                    loading = loading,
                    onClick = {
                        val message = validate(
                            communeId, nom, prenom, telephone, avenue, numeroParcelle, quartier,
                            montant, devise, nombreTranches
                        )
                        if (message != null) {
                            error = message
                        } else if (!isConnected) {
                            error = "Vous n'êtes pas connecté, vérifiez votre connexion."
                        } else {
                        error = null
                        coroutineScope.launch {
                            loading = true
                            try {
                                val payload = Client(
                                    nom = nom.trim(),
                                    prenom = prenom.trim(),
                                    telephone = telephone.trim(),
                                    avenue = avenue.trim(),
                                    quartier = quartier.trim(),
                                    numero_parcelle = numeroParcelle.trim(),
                                    commune_id = communeId,
                                    montant_souscription = montant,
                                    devise = devise,
                                    nombre_tranches = nombreTranches
                                )
                                val response = ClientKtor().postData(clientAction, payload)
                                when (val status = response.status.value) {
                                    in 200..299 -> {
                                        val res = response.body<ResponseCreateClient>()
                                        val store = StoreData(ctx)
                                        val current = store.getDataClient.first()
                                        val address = res.client ?: Address(
                                            commune_id = communeId,
                                            avenue = avenue.trim(),
                                            numero_parcelle = numeroParcelle.trim(),
                                            quartier = quartier.trim()
                                        )
                                        val souscription = res.souscription
                                        val created = ClientS(
                                            id = address.client_id.takeIf { it != 0 } ?: address.id,
                                            nom = nom.trim(),
                                            prenom = prenom.trim(),
                                            telephone = telephone.trim(),
                                            montant_souscription = souscription?.montant_souscription ?: montant,
                                            devise = souscription?.devise ?: devise,
                                            nombre_tranches = souscription?.nombre_tranches ?: nombreTranches,
                                            tranche = souscription?.tranche ?: trancheLabel(nombreTranches),
                                            tranches = souscription?.tranches ?: preview?.tranches.orEmpty(),
                                            address_client = listOf(address)
                                        )
                                        store.saveDataClient(current + created)
                                        success = res.copy(
                                            souscription = souscription ?: preview,
                                            client = address
                                        )
                                        nom = ""
                                        prenom = ""
                                        telephone = ""
                                        avenue = ""
                                        quartier = ""
                                        numeroParcelle = ""
                                        montantText = ""
                                        tranchesText = ""
                                        devise = "FC"
                                        communeId = 0
                                        formVersion += 1
                                        Toast.makeText(ctx, res.message, Toast.LENGTH_LONG).show()
                                    }
                                    in 500..599 -> error = "Erreur serveur. Réessayez dans un instant."
                                    in 400..499 -> {
                                        val res = response.body<ResponseAPI>()
                                        error = res.message.ifBlank { "La création a été refusée." }
                                    }
                                    else -> error = "Réponse inattendue ($status)."
                                }
                            } catch (e: Exception) {
                                error = "Impossible de joindre le serveur."
                            } finally {
                                loading = false
                            }
                        }
                        }
                    }
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        }
    }

    success?.let { result ->
        Dialog(onDismissRequest = { success = null }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(20.dp)) {
                    AppLogo(size = 64.dp, ring = AppBackground)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = result.message.ifBlank { "Compte client créé avec succès" },
                        color = Ink,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    val address = result.client
                    if (address != null) {
                        Text(
                            text = listOf(
                                address.avenue,
                                address.numero_parcelle,
                                address.quartier,
                                communeLabel(address.commune_id)
                            ).filter { it.isNotBlank() && it != "—" }.joinToString(", "),
                            color = InkMuted,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
                        )
                    }
                    result.souscription?.let { SubscriptionBlock(it) }
                    Spacer(Modifier.height(12.dp))
                    PrimaryButton(text = "Fermer", onClick = { success = null })
                }
            }
        }
    }
}

private fun validate(
    communeId: Int,
    nom: String,
    prenom: String,
    telephone: String,
    avenue: String,
    numero: String,
    quartier: String,
    montant: Int,
    devise: String,
    tranches: Int,
): String? {
    if (communeId == 0) return "Choisissez une commune dans la liste."
    if (nom.isBlank() || prenom.isBlank()) return "Le nom et le prénom sont obligatoires."
    if (telephone.isBlank()) return "Le téléphone est obligatoire."
    if (avenue.isBlank() || numero.isBlank() || quartier.isBlank()) {
        return "Complétez l'adresse : avenue, numéro et quartier."
    }
    if (devise != "FC" && devise != "$") return "Choisissez la devise FC ou $."
    if (montant <= 0) return "Indiquez un montant de souscription valide."
    if (tranches <= 0) return "Indiquez le nombre de tranches."
    return null
}

private fun trancheLabel(count: Int): String {
    val words = mapOf(
        1 to "une tranche",
        2 to "deux tranches",
        3 to "trois tranches",
        4 to "quatre tranches",
        5 to "cinq tranches",
        6 to "six tranches",
        7 to "sept tranches",
        8 to "huit tranches",
        9 to "neuf tranches",
        10 to "dix tranches"
    )
    return words[count] ?: "$count tranches"
}

@Composable
@Preview
fun InscriptionPageBody() {
    InscriptionPage(isConnected = false)
}
