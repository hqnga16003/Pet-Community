package com.feature.wigdet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldInput(
    value: String,
    modifier: Modifier = Modifier,
    placeholder: String,
    leadingIcon: ImageVector,
    keyboardOptions: KeyboardOptions,
    isHide: Boolean = false,
    isError: Boolean = false,
    readOnly: Boolean = false,
    errorText: String = "",
    enabled: Boolean = true,

    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        readOnly = readOnly,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder)
        },
        leadingIcon = {
            Icon(leadingIcon, contentDescription = "")
        },
        keyboardOptions = keyboardOptions,
        visualTransformation = if (isHide) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardActions = keyboardActions,
        isError = isError,
        enabled = enabled,
        supportingText = {
            if (isError) {
                Text(text = errorText)
            }
        }
    )
}

@Composable
fun TextFieldInputNumber(
    value: String,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    errorText: String = "",
    isError: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        isError = isError,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder)
        },
        leadingIcon = {
            Image(
                painter = painterResource(id = androidx.core.R.drawable.ic_call_decline),
                contentDescription = "",
                Modifier.size(25.dp)
            )
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        supportingText = {
            if (isError) {
                Text(text = errorText)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldClick(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    leadingIcon: ImageVector,
    isError: Boolean,
    errorText: String
) {



    Box(modifier = modifier){
        OutlinedTextField(
            value = value,
            placeholder = {
                Text(text = placeholder)
            },

            isError = isError,
            enabled = false,
            onValueChange = {},
            leadingIcon = {
                Icon(leadingIcon, contentDescription = "")
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = if (isError)Color.Red else MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledSupportingTextColor =if (isError)Color.Red else MaterialTheme.colorScheme.outline,
            ),
            supportingText = {
                if (isError) {
                    Text(text = errorText)
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuGender(
    modifier: Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    value: String,
    isError: Boolean,
    errorText: String,
    onDismissRequest: () -> Unit,
    onClickDropdownMenuItem: (String) -> Unit
) {
    ExposedDropdownMenuBox(modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            onExpandedChange(it)
        }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(),
            value = value,
            readOnly = true,

            onValueChange = {},

            placeholder = {
                Text(text = "Gender")
            },
            leadingIcon = {
                Icon(Icons.Outlined.Face, contentDescription = "")
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            supportingText = {
                if (isError) {
                    Text(text = errorText)
                }
            },
            isError = isError

        )
        ExposedDropdownMenu(
            modifier = Modifier.background(Color.White),
            expanded = expanded,
            onDismissRequest = onDismissRequest,
        ) {
            DropdownMenuItem(
                text = { Text("Male") },
                onClick = {
                    onClickDropdownMenuItem("Male")

                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )
            DropdownMenuItem(
                text = { Text("Female") },
                onClick = {
                    onClickDropdownMenuItem("Female")

                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )
            DropdownMenuItem(
                text = { Text("Other") },
                onClick = {
                    onClickDropdownMenuItem("Other")

                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )

        }
    }
}