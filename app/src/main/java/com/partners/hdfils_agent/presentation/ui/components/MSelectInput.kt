package com.partners.hdfils_agent.presentation.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.partners.hdfils_agent.domain.models.Commune

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun SelectableOutlinedTextField(
    title: String = "Sélectionnez une option",
    textValue2: String,
    onChangeText: (Commune) -> Unit = {},
    itemList: List<Commune>?
) {
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(textValue2) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = false}
    ) {
        OutlinedTextField(
            value = textValue,
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
            onValueChange = { textValue = it },
            label = { Text(title) },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
            ,
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (!expanded) Icons.Outlined.KeyboardArrowDown else Icons.Outlined.KeyboardArrowUp,
                        contentDescription = "Sélectionner",
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded=false}
        ) {
            itemList?.forEach { item ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = {
                        Text(text = item.nom, color = Color.Black)
                    },
                    onClick = {
                        textValue = item.nom
                        onChangeText(item) // Appel de la fonction de changement
                        expanded = false
                        onChangeText(item)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }

    }
//    Box(modifier = Modifier.fillMaxWidth()) {
//
//
//        // Positionner le DropdownMenu juste en dessous
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier
//                .width(textfieldSize.width.dp)
//                .offset(y = textfieldSize.height.dp) // Déplacer vers le bas
//        ) {
//            itemList?.forEach { item ->
//                DropdownMenuItem(
//                    modifier = Modifier.fillMaxWidth(),
//                    text = {
//                        Text(text = item.nom, color = Color.White)
//                    },
//                    onClick = {
//                        textValue = item.nom
//                        onChangeText(item) // Appel de la fonction de changement
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
}

@Composable
fun dropDownMenu() {

    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("Kotlin", "Java", "Dart", "Python")
    var selectedText by remember { mutableStateOf("") }

    var textfieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column(Modifier.padding(20.dp)) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            label = {Text("Label")},
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { expanded = !expanded })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textfieldSize.width.toDp()})
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedText = label
                    expanded = false
                }, text ={  Text(text = label)} )
            }
        }
    }

}

data class DropDownItem(
    val text: String
)

@Composable
fun SelectCase(
    title: String,
    name: String,
    dropdownItems: List<Commune>,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onItemClick: (Commune) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(name) }
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val density = LocalDensity.current

    OutlinedTextField(
        value = textValue,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            cursorColor = Color.White,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.White
        ),
        onValueChange = {textValue = it},
        label = { Text(title) },
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = {
                expanded = true
            }) {
                Icon(
                    if(!expanded) Icons.Outlined.KeyboardArrowDown else Icons.Outlined.KeyboardArrowUp, // Remplacez par votre icône
                    contentDescription = "Sélectionner",
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    )

    Card(
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = modifier
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .indication(interactionSource, LocalIndication.current)
                .pointerInput(true) {
                    detectTapGestures(
                        onLongPress = {
                            isContextMenuVisible = true
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        },
                        onPress = {
                            val press = PressInteraction.Press(it)
                            interactionSource.emit(press)
                            tryAwaitRelease()
                            interactionSource.emit(PressInteraction.Release(press))
                        }
                    )
                }
                .padding(16.dp)
        ) {
            Text(text = name)
        }
        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = {
                isContextMenuVisible = false
            },
            offset = pressOffset.copy(
                y = pressOffset.y - itemHeight
            )
        ) {
            dropdownItems.forEach {
                DropdownMenuItem(
                    onClick = {
                        onItemClick(it)
                        isContextMenuVisible = false
                    },
                    text = { Text(text = it.nom)},
                )
            }
        }
    }
}