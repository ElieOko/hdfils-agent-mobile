package com.partners.hdfils_agent.presentation.ui.components

import android.accounts.Account
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.io.path.Path
import androidx.core.graphics.toColorInt
import com.partners.hdfils_agent.R
import com.partners.hdfils_agent.domain.models.TrashClean
import com.partners.hdfils_agent.domain.models.communeLabel
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlue
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlueDark
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlueLight
import com.partners.hdfils_agent.presentation.ui.theme.Danger
import com.partners.hdfils_agent.presentation.ui.theme.Ink
import com.partners.hdfils_agent.presentation.ui.theme.InkMuted
import com.partners.hdfils_agent.presentation.ui.theme.Success
import com.partners.hdfils_agent.presentation.ui.theme.Warning
import kotlinx.coroutines.launch


@Composable
@Preview
fun ElegantCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Fond avec dégradé
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                // Dégradé de fond
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6A11CB),
                            Color(0xFF2575FC)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(width, height)
                    ),
                    size = size
                )

                // Formes décoratives
                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    radius = size.minDimension * 0.4f,
                    center = Offset(width * 0.8f, height * 0.2f)
                )
            }

            // Contenu
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                // En-tête avec icône
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Premium",
                        tint = Color.Yellow,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = "PREMIUM FEATURE",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Titre principal
                Text(
                    text = "Analytics Dashboard",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                Text(
                    text = "Accédez à des analyses avancées et des insights personnalisés pour optimiser vos performances.",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Bouton avec icône
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clickable { /* Action */ },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Voir",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = "Explorer les analyses",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun ClientStatsCardWithChart(
    clientCount: Int = 0,
    totals: List<Pair<String, Int>> = emptyList(),
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(BrandBlue, BrandBlueDark)),
                    RoundedCornerShape(22.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "CLIENTS SUIVIS",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$clientCount",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
                if (totals.isEmpty()) {
                    Text(
                        text = "Aucune souscription enregistrée",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp
                    )
                } else {
                    totals.forEach { (devise, amount) ->
                        Text(
                            text = "Souscriptions · ${formatMoney(amount, devise)}",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MCard(
    items: TrashClean,
    onClick: () -> Unit = {},
    onclickClean: () -> Unit = {}
) {
    val titleTrash: String
    val colorTrash: Color
    val iconState: Int
    when (items.state_trash_id) {
        3 -> {
            titleTrash = "Poubelle pleine"
            iconState = R.drawable.run
            colorTrash = Danger
        }
        2 -> {
            titleTrash = "Nettoyage en cours"
            iconState = R.drawable.transfert
            colorTrash = Warning
        }
        else -> {
            titleTrash = "Poubelle propre"
            iconState = R.drawable.check
            colorTrash = Success
        }
    }
    val client = items.client
    val address = client?.address_client?.firstOrNull()
    val place = listOfNotNull(
        address?.avenue?.takeIf { it.isNotBlank() },
        address?.numero_parcelle?.takeIf { it.isNotBlank() }?.let { "n° $it" },
        address?.quartier?.takeIf { it.isNotBlank() },
        communeLabel(address?.commune_id).takeIf { it != "—" }
    ).joinToString(" · ")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(colorTrash)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(iconState),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = colorTrash
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(titleTrash, color = colorTrash, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Text(
                    text = listOfNotNull(client?.prenom, client?.nom).filter { it.isNotBlank() }.joinToString(" ").ifBlank { "Client" },
                    color = Ink,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                if (!client?.code.isNullOrBlank()) {
                    Text(client?.code.orEmpty(), color = BrandBlue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                if (place.isNotBlank()) {
                    Text(place, color = InkMuted, fontSize = 12.sp)
                }
                if ((client?.montant_souscription ?: 0) > 0) {
                    val label = client?.tranche?.takeIf { it.isNotBlank() }
                        ?: "${client?.nombre_tranches ?: 0} tranches"
                    Text(
                        text = "${formatMoney(client?.montant_souscription ?: 0, client?.devise.orEmpty())} · $label",
                        color = BrandBlueDark,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Column {
                IconButton(onClick = onClick) {
                    Icon(
                        painterResource(R.drawable.detail),
                        contentDescription = "Détails",
                        tint = BrandBlue,
                        modifier = Modifier.size(26.dp)
                    )
                }
                IconButton(onClick = onclickClean) {
                    Icon(
                        painterResource(R.drawable.trash_clean),
                        modifier = Modifier.size(22.dp),
                        tint = colorTrash,
                        contentDescription = "Mettre à jour"
                    )
                }
            }
        }
    }
}
@Composable
@Preview
fun WasteCollectionStatsCard(
    pleine: Int = 0,
    enCours: Int = 0,
    propre: Int = 0,
) {
    val total = (pleine + enCours + propre).coerceAtLeast(1)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(BrandBlueLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = BrandBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        text = "POUBELLES",
                        color = InkMuted,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "État des vidages",
                        color = Ink,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(value = "$pleine", label = "Pleines", icon = Icons.Outlined.Delete, color = Danger)
                StatItem(value = "$enCours", label = "En cours", icon = Icons.Outlined.AccountBox, color = Warning)
                StatItem(value = "$propre", label = "Propres", icon = Icons.Outlined.CheckCircle, color = Success)
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (pleine + enCours + propre == 0) {
                Text(
                    text = "Les poubelles apparaîtront ici après la connexion.",
                    color = InkMuted,
                    fontSize = 13.sp
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    if (propre > 0) Box(Modifier.weight(propre / total.toFloat()).fillMaxHeight().background(Success))
                    if (enCours > 0) Box(Modifier.weight(enCours / total.toFloat()).fillMaxHeight().background(Warning))
                    if (pleine > 0) Box(Modifier.weight(pleine / total.toFloat()).fillMaxHeight().background(Danger))
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    icon: ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color.copy(alpha = 0.1f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}

@Composable
@Preview
private fun WeeklyCollectionChart() {
    val weekData = listOf(
        DayData("Lun", 1f, 0f, 0f),
        DayData("Mar", 1f, 0f, 0f),
        DayData("Mer", 0.8f, 0.2f, 0f),
        DayData("Jeu", 1f, 0f, 0f),
        DayData("Ven", 0.7f, 0.3f, 0f),
        DayData("Sam", 0f, 0f, 1f), // Manqué
        DayData("Dim", 0.9f, 0.1f, 0f)
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        val barWidth = size.width / (weekData.size * 1.2f)
        val spacing = barWidth * 0.2f

        weekData.forEachIndexed { index, dayData ->
            val left = index * (barWidth + spacing) + spacing
            val centerX = left + barWidth / 2

            // Dessiner le texte du jour
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    dayData.day,
                    centerX,
                    size.height - 8.dp.toPx(),
                    android.graphics.Paint().apply {
                        color = "#666666".toColorInt()
                        textSize = 10.sp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }

            // Barre terminée (vert)
            if (dayData.completed > 0f) {
                drawRect(
                    color = Color(0xFF4CAF50),
                    topLeft = Offset(left, size.height * (1 - dayData.completed) - 20.dp.toPx()),
                    size = Size(barWidth, size.height * dayData.completed)

                )
            }

            // Barre retard (orange)
            if (dayData.delayed > 0f) {
                drawRect(
                    color = Color(0xFFFF9800),
                    topLeft = Offset(left, size.height * (1 - dayData.delayed - dayData.completed) - 20.dp.toPx()),
                    size = Size(barWidth, size.height * dayData.delayed)
                )
            }

            // Barre manqué (rouge)
            if (dayData.missed > 0f) {
                drawRect(
                    color = Color(0xFFF44336),
                    topLeft = Offset(left, size.height * (1 - dayData.missed) - 20.dp.toPx()),
                    size = Size(barWidth, size.height * dayData.missed)
                )
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

private data class DayData(
    val day: String,
    val completed: Float, // Terminé à temps
    val delayed: Float,   // En retard
    val missed: Float     // Manqué
)

@Composable
@Preview
fun AnimatedStatsCard(
    accountClientSize : Int = 5
) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedProgress.animateTo(1f, animationSpec = tween(1000))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Répartition Clients",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Barres animées
            ClientCategoryBar(
                label = "Particuliers",
                value = accountClientSize * 0.65f,
                total = accountClientSize.toFloat(),
                color = Color(0xFF4285F4),
                animatedProgress = animatedProgress.value
            )

            ClientCategoryBar(
                label = "Entreprises",
                value = accountClientSize * 0.25f,
                total = accountClientSize.toFloat(),
                color = Color(0xFF34A853),
                animatedProgress = animatedProgress.value
            )

            ClientCategoryBar(
                label = "Institutions",
                value = accountClientSize * 0.1f,
                total = accountClientSize.toFloat(),
                color = Color(0xFFEA4335),
                animatedProgress = animatedProgress.value
            )
        }
    }
}

@Composable
private fun ClientCategoryBar(
    label: String,
    value: Float,
    total: Float,
    color: Color,
    animatedProgress: Float
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Text(
                text = "${(value / total * 100).toInt()}% (${value.toInt()})",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color.copy(alpha = 0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (value / total) * animatedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}