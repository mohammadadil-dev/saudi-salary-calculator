package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor

/** One entry in the bottom navigation bar. [key] is an opaque route identifier owned by the
 * navigation graph (e.g. a Screen route string) so this design-system module never needs to know
 * about the app's actual NavGraph/Screen types. */
data class BottomNavItem(
  val key: String,
  val label: String,
  val icon: ImageVector,
  val selectedIcon: ImageVector = icon
)

/** Material3 [NavigationBar] themed with the brand green, used to switch between
 * Home / Calculator / Result / Compare / Settings. */
@Composable
fun BottomNavigationBar(
  items: List<BottomNavItem>,
  selectedKey: String,
  darkMode: Boolean,
  onSelect: (String) -> Unit
) {
  NavigationBar(
    containerColor = if (darkMode) Color(0xFF11201B) else Color.White,
    tonalElevation = 0.dp
  ) {
    items.forEach { item ->
      val isSelected = item.key == selectedKey
      NavigationBarItem(
        selected = isSelected,
        onClick = { onSelect(item.key) },
        icon = {
          Icon(if (isSelected) item.selectedIcon else item.icon, contentDescription = item.label)
        },
        label = { Text(item.label, maxLines = 1) },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = Color.White,
          selectedTextColor = BrandGreen,
          indicatorColor = BrandGreen,
          unselectedIconColor = designSystemContentColor(darkMode).copy(alpha = 0.5f),
          unselectedTextColor = designSystemContentColor(darkMode).copy(alpha = 0.5f)
        )
      )
    }
  }
}
