package pl.betoniarze.kawapp.feature.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.ui.components.KawFieldShape
import pl.betoniarze.kawapp.ui.components.RoastDateField

/**
 * One-field dialog used for the user name and for a new grinder. The field is focused on open and
 * the keyboard's Done key confirms.
 *
 * @param allowBlank Whether an empty value can be confirmed (clearing the user name is allowed,
 * adding a nameless grinder is not).
 */
@Composable
fun TextInputDialog(
    title: String,
    label: String,
    confirmText: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    initialValue: String = "",
    placeholder: String? = null,
    allowBlank: Boolean = false,
) {
    var value by rememberSaveable { mutableStateOf(initialValue) }
    val canConfirm = allowBlank || value.isNotBlank()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                label = { Text(text = label) },
                placeholder = placeholder?.let { { Text(text = it) } },
                singleLine = true,
                shape = KawFieldShape,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { if (canConfirm) onConfirm(value) }),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(value) }, enabled = canConfirm) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        },
    )
}

/**
 * Bottom sheet for adding a coffee: roaster, beans, optional origin and roast date. Same fields and
 * order as the coffee section of the "save brew" form.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCoffeeBagSheet(
    onConfirm: (roasterName: String, beanName: String, origin: String, roastDate: LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var roasterName by rememberSaveable { mutableStateOf("") }
    var beanName by rememberSaveable { mutableStateOf("") }
    var origin by rememberSaveable { mutableStateOf("") }
    var roastDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }
    val date = roastDate
    val canConfirm = roasterName.isNotBlank() && beanName.isNotBlank() && date != null

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.add_coffee_title),
                style = MaterialTheme.typography.titleLarge,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SheetField(
                    value = roasterName,
                    onValueChange = { roasterName = it },
                    label = stringResource(R.string.roaster_name),
                    modifier = Modifier.weight(1f),
                )
                SheetField(
                    value = beanName,
                    onValueChange = { beanName = it },
                    label = stringResource(R.string.bean_name),
                    modifier = Modifier.weight(1f),
                )
            }
            SheetField(
                value = origin,
                onValueChange = { origin = it },
                label = stringResource(R.string.coffee_origin),
                modifier = Modifier.fillMaxWidth(),
            )
            RoastDateField(
                date = roastDate,
                onDateChange = { roastDate = it },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(4.dp))
            Button(
                onClick = { if (date != null) onConfirm(roasterName, beanName, origin, date) },
                enabled = canConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            ) {
                Text(text = stringResource(R.string.add_coffee), style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun SheetField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = true,
        shape = KawFieldShape,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
        modifier = modifier,
    )
}
