package com.imams.boardminton.ui.component.country

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryPickerBottomSheet(
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = BottomSheetDefaults.Elevation,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    onItemSelected: (country: Country) -> Unit,
    onDismissRequest: () -> Unit
) {
    var searchValue by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor
    ) {
        DefaultTitle()
        CountrySearchView(searchValue) { searchValue = it }
        Countries(searchValue) {
            scope.launch {
                sheetState.hide()
                onItemSelected(it)
            }
        }
        Spacer(modifier = Modifier.padding(bottom = 32.dp))
    }
}

@Composable
private fun DefaultTitle() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        text = "Select Country",
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
}

@Composable
private fun Countries(
    searchValue: String,
    onItemSelected: (country: Country) -> Unit
) {
    val context = LocalContext.current
    val defaultCountries = remember { countryList(context) }

    val countries = remember(searchValue) {
        if (searchValue.isEmpty()) {
            defaultCountries
        } else {
            defaultCountries.searchCountryList(searchValue)
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        items(countries) { country ->
            RowCountryFlag(country = country, onItemSelected = onItemSelected::invoke)
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                thickness = 0.5.dp
            )
        }
    }

}

@Composable
private fun RowCountryFlag(
    country: Country,
    onItemSelected: (country: Country) -> Unit
) {
    Row(modifier = Modifier
        .clickable { onItemSelected(country) }
        .padding(12.dp)
    ) {
        Text(text = localeToEmoji(country.code))
        Text(
            text = country.name,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(2f)
        )
    }
}

@Composable
private fun RowCountryPhoneCode(
    country: Country,
    onItemSelected: (country: Country) -> Unit
) {
    Row(modifier = Modifier
        .clickable { onItemSelected(country) }
        .padding(12.dp)
    ) {
        Text(text = localeToEmoji(country.code))
        Text(
            text = country.name,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(2f)
        )
        Text(
            text = country.dialCode,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun CountrySearchView(searchValue: String, onSearch: (searchValue: String) -> Unit) {
    val focusManager = LocalFocusManager.current
    Row {
        Box(
            modifier = Modifier
                .padding(12.dp)
                .background(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                )
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                value = searchValue, onValueChange = { onSearch(it) },
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                placeholder = {
                    Text(
                        text = "Search",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 16.sp,
                    )
                }, singleLine = true,
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }, trailingIcon = {
                    if (searchValue.isNotEmpty()) {
                        IconButton(onClick = {
                            onSearch("")
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "Clear icon"
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchView() {
    CountrySearchView("search") {}
}

@Preview
@Composable
private fun CountryPickerPreview() {
    Scaffold { p ->
        var searchValue by rememberSaveable { mutableStateOf("") }
        Surface(modifier = Modifier.padding(p)) {
            Column {
                DefaultTitle()
                CountrySearchView(searchValue) { searchValue = it }
                Countries(searchValue) {}
            }
        }
    }
}
