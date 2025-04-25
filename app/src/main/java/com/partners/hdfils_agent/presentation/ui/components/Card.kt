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
import com.partners.hdfils_agent.domain.models.Client
import kotlin.io.path.Path
import androidx.core.graphics.toColorInt
import com.partners.hdfils_agent.R
import com.partners.hdfils_agent.data.shared.StoreData
import com.partners.hdfils_agent.domain.models.TrashClean
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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
@Preview
fun ClientStatsCardWithChart(
    accountClientSize : Double = 0.0
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val clients = remember { mutableStateListOf<Client>() }
    val sizeClient = remember { mutableIntStateOf(value = 0) }
    try {
        scope.launch {
            StoreData(context).getDataClient.collect{
                if(it.isNotEmpty()){
                    it.forEach { i->
                        if(i != null){
                            clients.add(i)
                            sizeClient.intValue = clients.size
                            Toast.makeText(context,"$it",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
    catch (e:Exception){
        e.message?.let {
            Log.e("ERROR************************************",
                it,)
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Fond avec graphique minimal
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                // Dégradé de fond
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Blue.copy(alpha = 0.1f),
                            Color.Blue.copy(alpha = 0.05f)
                        )
                    ),
                    size = size
                )

                // Ligne de graphique
                val points = listOf(
                    Offset(0.1f * width, 0.7f * height),
                    Offset(0.3f * width, 0.5f * height),
                    Offset(0.5f * width, 0.6f * height),
                    Offset(0.7f * width, 0.3f * height),
                    Offset(0.9f * width, 0.5f * height)
                )

                drawPath(
                    path = Path().apply {
                        moveTo(points.first().x, points.first().y)
                        points.drop(1).forEach { point ->
                            lineTo(point.x, point.y)
                        }
                    } as Path,
                    color = Color.Blue.copy(alpha = 0.3f),
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            // Contenu
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Total Clients",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "${sizeClient.intValue}",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "%",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MCard(
    items : TrashClean,
    onClick : ()-> Unit = {},
    onclickClean : ()-> Unit = {}
){
    var titleTrash = "Poubelle propre"
    var colorTrash = Color.Green.copy(alpha = 0.5F)
    var iconState = R.drawable.check
    when(items.state_trash_id){
        3 -> {
            titleTrash = "Poubelle pleine"
            iconState = R.drawable.run
            colorTrash  =  Color.Red.copy(alpha = 0.7F)
        }
        2 -> {
            titleTrash = "Nettoyage poubelle"
            iconState = R.drawable.transfert
            colorTrash = Color.Blue.copy(
                alpha = 0.7F,
            )
        }
        1 ->{
            titleTrash = "Poubelle propre"
            iconState = R.drawable.check
        }
    }
    Card(
        Modifier.padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Blue.copy(0.1f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(iconState),
                contentDescription = "",
                modifier = Modifier.size(30.dp),
                tint = colorTrash
            )
            Spacer(Modifier.width(10.dp))
            Column() {
                Label(titleTrash, fontWeight = FontWeight.Bold, color = Color.Black)
                Label("Client : ${items.client?.nom} ${items.client?.prenom}", fontWeight = FontWeight.Bold, color = Color.Black)
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Right
            ) {
                IconButton(
                    onClick = {
                        onClick()
                    }
                ) {
                    Icon(
                        painterResource(R.drawable.detail),
                        contentDescription = null,
                        tint = Color.Blue.copy(0.7f),
                        modifier = Modifier.size(30.dp)
                    )
                }
                IconButton(
                    onClick = onclickClean
                ) {
                    Icon(
                        painterResource(R.drawable.trash_clean),
                        modifier = Modifier.size(23.dp),
                        tint = Color.Red.copy(0.7f),
                        contentDescription = null
                    )
                }
            }
        }
    }
}
@Composable
@Preview
fun WasteCollectionStatsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // En-tête avec icône
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color(0xFF388E3C).copy(alpha = 0.2f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Poubelle",
                        tint = Color(0xFF388E3C),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = "STATISTIQUES DE VIDAGE",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Collecte des Déchets",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Statistiques principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    value = "48",
                    label = "Vidages\ncette semaine",
                    icon = Icons.Outlined.Home,
                    color = Color(0xFF2196F3)
                )

                StatItem(
                    value = "92%",
                    label = "Taux de\nréussite",
                    icon = Icons.Outlined.CheckCircle,
                    color = Color(0xFF4CAF50)
                )

                StatItem(
                    value = "3",
                    label = "Retards",
                    icon = Icons.Outlined.AccountBox,
                    color = Color(0xFFFF9800)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Graphique de fréquence
            Text(
                text = "Fréquence Hebdomadaire",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Space(y = 34)
            WeeklyCollectionChart()

            Spacer(modifier = Modifier.height(16.dp))

            // Légende
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LegendItem(color = Color(0xFF4CAF50), text = "Terminé")
                LegendItem(color = Color(0xFFFF9800), text = "Retard")
                LegendItem(color = Color(0xFFF44336), text = "Manqué")
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