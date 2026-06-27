package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandRed
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor

/** Generic labeled text input (employee name, job title, free-text fields) with an animated
 * helper/error message underneath. [CurrencyInputField] builds on the same visual language for
 * numeric SAR amounts specifically. */
@Composable
fun SalaryInputField(
  label: String,
  value: String,
  onValueChange: (String) -> Unit,
  darkMode: Boolean,
  modifier: Modifier = Modifier,
  placeholder: String? = null,
  helperText: String? = null,
  isError: Boolean = false,
  errorText: String? = null,
  keyboardType: KeyboardType = KeyboardType.Text,
  singleLine: Boolean = true,
  trailing: @Composable (() -> Unit)? = null,
  labelTrailing: @Composable (() -> Unit)? = null
) {
  Column(modifier = modifier.fillMaxWidth()) {
    Row(modifier = Modifier.fillMaxWidth()) {
      Text(
        label,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = designSystemContentColor(darkMode).copy(alpha = 0.85f),
        modifier = Modifier.weight(1f)
      )
      labelTrailing?.invoke()
    }
    Spacer(Modifier.height(6.dp))
    OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      modifier = Modifier.fillMaxWidth(),
      placeholder = placeholder?.let { ph -> { Text(ph, color = designSystemContentColor(darkMode).copy(alpha = 0.35f)) } },
      isError = isError,
      singleLine = singleLine,
      trailingIcon = trailing,
      keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
      shape = RoundedCornerShape(14.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BrandGreen,
        unfocusedBorderColor = if (darkMode) Color.White.copy(alpha = 0.18f) else Color(0xFFD6DDD8),
        focusedContainerColor = if (darkMode) Color.White.copy(alpha = 0.05f) else Color.White,
        unfocusedContainerColor = if (darkMode) Color.White.copy(alpha = 0.03f) else Color.White,
        cursorColor = BrandGreen
      )
    )
    AnimatedVisibility(
      visible = (isError && errorText != null) || (!isError && helperText != null),
      enter = fadeIn() + expandVertically(),
      exit = fadeOut() + shrinkVertically()
    ) {
      Text(
        text = (if (isError) errorText else helperText).orEmpty(),
        style = MaterialTheme.typography.labelSmall,
        color = if (isError) BrandRed else designSystemContentColor(darkMode).copy(alpha = 0.55f),
        modifier = Modifier.padding(top = 4.dp)
      )
    }
  }
}
