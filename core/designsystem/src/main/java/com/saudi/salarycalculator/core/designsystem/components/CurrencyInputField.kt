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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import kotlin.math.roundToInt

/** Numeric SAR input used for every money field in the wizard (basic salary, allowances, bonus,
 * deductions...). Filters input to digits/decimal point, shows the currency symbol as a leading
 * marker, and can optionally pair the text field with a [Slider] for faster coarse entry. */
@Composable
fun CurrencyInputField(
  label: String,
  value: String,
  onValueChange: (String) -> Unit,
  darkMode: Boolean,
  currencySymbol: String,
  modifier: Modifier = Modifier,
  helperText: String? = null,
  optionalLabel: String? = null,
  invalidNumberText: String = "Enter a valid number",
  showSlider: Boolean = false,
  sliderRange: ClosedFloatingPointRange<Float> = 0f..20000f
) {
  val isError = value.isNotBlank() && value.toDoubleOrNull() == null
  Column(modifier = modifier.fillMaxWidth()) {
    Row(modifier = Modifier.fillMaxWidth()) {
      Text(
        label,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = designSystemContentColor(darkMode).copy(alpha = 0.85f),
        modifier = Modifier.weight(1f)
      )
      optionalLabel?.let {
        Text(it, style = MaterialTheme.typography.labelSmall, color = designSystemContentColor(darkMode).copy(alpha = 0.45f))
      }
    }
    Spacer(Modifier.height(6.dp))
    OutlinedTextField(
      value = value,
      onValueChange = { input -> onValueChange(input.filter { ch -> ch.isDigit() || ch == '.' }) },
      modifier = Modifier.fillMaxWidth(),
      isError = isError,
      singleLine = true,
      leadingIcon = { RiyalSymbol(tint = BrandGreen, size = 16.dp, contentDescription = currencySymbol) },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
      shape = RoundedCornerShape(14.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BrandGreen,
        unfocusedBorderColor = if (darkMode) Color.White.copy(alpha = 0.18f) else Color(0xFFD6DDD8),
        focusedContainerColor = if (darkMode) Color.White.copy(alpha = 0.05f) else Color.White,
        unfocusedContainerColor = if (darkMode) Color.White.copy(alpha = 0.03f) else Color.White,
        cursorColor = BrandGreen
      )
    )
    if (showSlider) {
      Slider(
        value = (value.toFloatOrNull() ?: 0f).coerceIn(sliderRange),
        onValueChange = { onValueChange(it.roundToInt().toString()) },
        valueRange = sliderRange,
        colors = SliderDefaults.colors(
          thumbColor = BrandGreen,
          activeTrackColor = BrandGreen,
          inactiveTrackColor = if (darkMode) Color.White.copy(alpha = 0.15f) else Color(0xFFE7ECE8)
        )
      )
    }
    AnimatedVisibility(
      visible = isError || helperText != null,
      enter = fadeIn() + expandVertically(),
      exit = fadeOut() + shrinkVertically()
    ) {
      Text(
        text = if (isError) invalidNumberText else helperText.orEmpty(),
        style = MaterialTheme.typography.labelSmall,
        color = if (isError) BrandRed else designSystemContentColor(darkMode).copy(alpha = 0.55f),
        modifier = Modifier.padding(top = 4.dp)
      )
    }
  }
}
