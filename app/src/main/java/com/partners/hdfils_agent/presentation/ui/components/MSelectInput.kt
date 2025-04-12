package com.partners.hdfils_agent.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SelectableOutlinedTextField(
    title: String = "Sélectionnez une option",
    textValue2 : String,
    onChangeText : (String) -> Unit,
    itemList : List<String> = listOf("Option 1", "Option 2", "Option 3", "Option 4")
) {

    var expanded by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(textValue2) }

    Column(modifier = Modifier.padding(1.dp)) {
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
//                unfocusedTextColor = Color.White,
//                focusedTextColor =Color.White,
//                focusedBorderColor = Color.White,
//                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
//                focusedLabelColor = Color.White,
//                unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
//                cursorColor = Color.White,
//                focusedLeadingIconColor = Color.White,
//                unfocusedLeadingIconColor = Color.White.copy(alpha = 0.8f)
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
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            itemList.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(text = item,)
                    },
                    onClick = {
                        textValue = item
                        expanded = false
                    }
                )
            }
        }


    }
}