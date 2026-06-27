package com.saudi.salarycalculator.core.designsystem.components

import android.icu.util.Calendar as IcuCalendar
import android.icu.util.ULocale
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreenLight
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor
import java.util.Calendar as JCalendar

private val GregorianWeekdayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
private val ArabicWeekdayLabels = listOf("ن", "ث", "ر", "خ", "ج", "س", "ح")
private val GregorianMonthNames = listOf(
  "January", "February", "March", "April", "May", "June",
  "July", "August", "September", "October", "November", "December"
)
private val HijriMonthNames = listOf(
  "محرم", "صفر", "ربيع الأول", "ربيع الآخر", "جمادى الأولى", "جمادى الآخرة",
  "رجب", "شعبان", "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
)

/**
 * Drop-in replacement for the stock Material3 `DatePicker`, styled to match the rest of the
 * design system (glass card, brand-green accents, animated month transitions, press-scale day
 * cells) instead of the plain grey grid Material3 ships by default.
 *
 * When [useHijri] is true the entire grid — month name, year, weekday row, every day cell — is
 * driven off the Umm al-Qura Hijri calendar (the same calendar Saudi government services use),
 * via ICU's `islamic-umalqura` calendar type. Tapping a day still resolves to a plain Gregorian
 * epoch millis through [onDateSelected], so callers (years-of-service math, payslip formatting,
 * etc.) never need to know Hijri exists — only the display flips, never the stored value.
 */
@Composable
fun CalendarPicker(
  initialMillis: Long?,
  darkMode: Boolean,
  useHijri: Boolean,
  todayLabel: String,
  modifier: Modifier = Modifier,
  onDateSelected: (Long) -> Unit
) {
  GlassCard(darkMode = darkMode, modifier = modifier, contentPadding = PaddingValues(16.dp)) {
    if (useHijri) {
      HijriCalendarBody(
        initialMillis = initialMillis,
        darkMode = darkMode,
        todayLabel = todayLabel,
        onDateSelected = onDateSelected
      )
    } else {
      GregorianCalendarBody(
        initialMillis = initialMillis,
        darkMode = darkMode,
        todayLabel = todayLabel,
        onDateSelected = onDateSelected
      )
    }
  }
}

@Composable
private fun GregorianCalendarBody(
  initialMillis: Long?,
  darkMode: Boolean,
  todayLabel: String,
  onDateSelected: (Long) -> Unit
) {
  val now = remember { JCalendar.getInstance() }
  val seedCal = remember { JCalendar.getInstance().apply { initialMillis?.let { timeInMillis = it } } }

  var year by remember { mutableStateOf(seedCal.get(JCalendar.YEAR)) }
  var month by remember { mutableStateOf(seedCal.get(JCalendar.MONTH)) }
  var selectedYear by remember { mutableStateOf(year) }
  var selectedMonth by remember { mutableStateOf(month) }
  var selectedDay by remember { mutableStateOf(if (initialMillis != null) seedCal.get(JCalendar.DAY_OF_MONTH) else null) }
  var showYearPicker by remember { mutableStateOf(false) }

  val monthKey = year * 12 + month

  Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
    CalendarHeader(
      title = "${GregorianMonthNames[month]} $year",
      todayLabel = todayLabel,
      darkMode = darkMode,
      showYearPicker = showYearPicker,
      onPrev = { if (month == 0) { month = 11; year -= 1 } else { month -= 1 } },
      onNext = { if (month == 11) { month = 0; year += 1 } else { month += 1 } },
      onToday = {
        year = now.get(JCalendar.YEAR)
        month = now.get(JCalendar.MONTH)
        selectedYear = year
        selectedMonth = month
        val day = now.get(JCalendar.DAY_OF_MONTH)
        selectedDay = day
        showYearPicker = false
        onDateSelected(gregorianMillisFor(year, month, day))
      },
      onTitleClick = { showYearPicker = !showYearPicker }
    )
    if (showYearPicker) {
      YearPickerGrid(
        selectedYear = year,
        darkMode = darkMode,
        onYearSelected = { y -> year = y; showYearPicker = false }
      )
    } else {
      AnimatedContent(
        targetState = monthKey,
        transitionSpec = { monthTransitionSpec() },
        label = "gregorian-month"
      ) { key ->
        val gy = key / 12
        val gm = key % 12
        val cells = remember(key) { gregorianMonthCells(gy, gm) }
        val isCurrentMonth = gy == now.get(JCalendar.YEAR) && gm == now.get(JCalendar.MONTH)
        val todayDay = if (isCurrentMonth) now.get(JCalendar.DAY_OF_MONTH) else null

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          WeekdayHeaderRow(labels = GregorianWeekdayLabels, darkMode = darkMode)
          cells.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
              week.forEach { day ->
                Box(modifier = Modifier.weight(1f)) {
                  DayCell(
                    day = day,
                    selected = day != null && gy == selectedYear && gm == selectedMonth && day == selectedDay,
                    isToday = day != null && day == todayDay,
                    darkMode = darkMode,
                    onClick = {
                      selectedYear = gy
                      selectedMonth = gm
                      selectedDay = day
                      if (day != null) onDateSelected(gregorianMillisFor(gy, gm, day))
                    }
                  )
                }
              }
              repeat(7 - week.size) { Box(modifier = Modifier.weight(1f)) }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun HijriCalendarBody(
  initialMillis: Long?,
  darkMode: Boolean,
  todayLabel: String,
  onDateSelected: (Long) -> Unit
) {
  val todayHijri = remember { hijriToday() }
  val seedHijri = remember { initialMillis?.let { hijriFromMillis(it) } ?: todayHijri }

  var year by remember { mutableStateOf(seedHijri.year) }
  var month by remember { mutableStateOf(seedHijri.month) }
  var selectedYear by remember { mutableStateOf(seedHijri.year) }
  var selectedMonth by remember { mutableStateOf(seedHijri.month) }
  var selectedDay by remember { mutableStateOf(if (initialMillis != null) seedHijri.day else null) }
  var showYearPicker by remember { mutableStateOf(false) }

  val monthKey = year * 12 + month

  Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
    CalendarHeader(
      title = "${HijriMonthNames[month]} $year",
      todayLabel = todayLabel,
      darkMode = darkMode,
      showYearPicker = showYearPicker,
      onPrev = { if (month == 0) { month = 11; year -= 1 } else { month -= 1 } },
      onNext = { if (month == 11) { month = 0; year += 1 } else { month += 1 } },
      onToday = {
        year = todayHijri.year
        month = todayHijri.month
        selectedYear = year
        selectedMonth = month
        selectedDay = todayHijri.day
        showYearPicker = false
        onDateSelected(hijriMillisFor(year, month, todayHijri.day))
      },
      onTitleClick = { showYearPicker = !showYearPicker }
    )
    if (showYearPicker) {
      YearPickerGrid(
        selectedYear = year,
        darkMode = darkMode,
        onYearSelected = { y -> year = y; showYearPicker = false }
      )
    } else {
      AnimatedContent(
        targetState = monthKey,
        transitionSpec = { monthTransitionSpec() },
        label = "hijri-month"
      ) { key ->
        val hy = key / 12
        val hm = key % 12
        val cells = remember(key) { hijriMonthCells(hy, hm) }
        val isCurrentMonth = hy == todayHijri.year && hm == todayHijri.month
        val todayDay = if (isCurrentMonth) todayHijri.day else null

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          WeekdayHeaderRow(labels = ArabicWeekdayLabels, darkMode = darkMode)
          cells.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
              week.forEach { day ->
                Box(modifier = Modifier.weight(1f)) {
                  DayCell(
                    day = day,
                    selected = day != null && hy == selectedYear && hm == selectedMonth && day == selectedDay,
                    isToday = day != null && day == todayDay,
                    darkMode = darkMode,
                    onClick = {
                      selectedYear = hy
                      selectedMonth = hm
                      selectedDay = day
                      if (day != null) onDateSelected(hijriMillisFor(hy, hm, day))
                    }
                  )
                }
              }
              repeat(7 - week.size) { Box(modifier = Modifier.weight(1f)) }
            }
          }
        }
      }
    }
  }
}

private fun AnimatedContentTransitionScope<Int>.monthTransitionSpec(): ContentTransform =
  if (targetState > initialState) {
    (slideInHorizontally(initialOffsetX = { it }) + fadeIn()) togetherWith
      (slideOutHorizontally(targetOffsetX = { -it }) + fadeOut())
  } else {
    (slideInHorizontally(initialOffsetX = { -it }) + fadeIn()) togetherWith
      (slideOutHorizontally(targetOffsetX = { it }) + fadeOut())
  }

@Composable
private fun CalendarHeader(
  title: String,
  todayLabel: String,
  darkMode: Boolean,
  showYearPicker: Boolean,
  onPrev: () -> Unit,
  onNext: () -> Unit,
  onToday: () -> Unit,
  onTitleClick: () -> Unit
) {
  val chevronRotation by animateFloatAsState(
    targetValue = if (showYearPicker) 180f else 0f,
    label = "year-picker-chevron"
  )
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(32.dp)
        .clip(CircleShape)
        .background(if (darkMode) Color.White.copy(alpha = 0.08f) else BrandGreen.copy(alpha = 0.08f))
        .clickable(enabled = !showYearPicker, onClick = onPrev),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        Icons.Filled.ChevronLeft,
        contentDescription = null,
        tint = if (showYearPicker) BrandGreen.copy(alpha = 0.3f) else BrandGreen
      )
    }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .clickable(onClick = onTitleClick)
          .padding(horizontal = 4.dp, vertical = 2.dp)
      ) {
        Text(
          title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Black,
          color = designSystemContentColor(darkMode)
        )
        Icon(
          Icons.Filled.ExpandMore,
          contentDescription = null,
          tint = BrandGreen,
          modifier = Modifier
            .size(20.dp)
            .graphicsLayer { rotationZ = chevronRotation }
        )
      }
      Text(
        todayLabel,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = BrandGreen,
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(BrandGreen.copy(alpha = 0.12f))
          .clickable(onClick = onToday)
          .padding(horizontal = 8.dp, vertical = 4.dp)
      )
    }
    Box(
      modifier = Modifier
        .size(32.dp)
        .clip(CircleShape)
        .background(if (darkMode) Color.White.copy(alpha = 0.08f) else BrandGreen.copy(alpha = 0.08f))
        .clickable(enabled = !showYearPicker, onClick = onNext),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        Icons.Filled.ChevronRight,
        contentDescription = null,
        tint = if (showYearPicker) BrandGreen.copy(alpha = 0.3f) else BrandGreen
      )
    }
  }
}

/** Tap target for jumping to an arbitrary year, opened via the month/year title in
 * [CalendarHeader]. Without this, [GregorianCalendarBody]/[HijriCalendarBody] only ever exposed
 * month-by-month stepping through the prev/next chevrons — there was no way to reach a year far
 * from today (e.g. a joining date from a decade ago) without dozens of taps. */
@Composable
private fun YearPickerGrid(
  selectedYear: Int,
  darkMode: Boolean,
  onYearSelected: (Int) -> Unit,
  yearsBefore: Int = 100,
  yearsAfter: Int = 50
) {
  val years = remember(selectedYear) { ((selectedYear - yearsBefore)..(selectedYear + yearsAfter)).toList() }
  val selectedIndex = remember(selectedYear, years) { years.indexOf(selectedYear).coerceAtLeast(0) }
  val gridState = rememberLazyGridState(initialFirstVisibleItemIndex = (selectedIndex - 6).coerceAtLeast(0))

  LazyVerticalGrid(
    columns = GridCells.Fixed(4),
    state = gridState,
    modifier = Modifier.fillMaxWidth().height(240.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    items(years) { y ->
      val selected = y == selectedYear
      Box(
        modifier = Modifier
          .aspectRatio(1.6f)
          .clip(RoundedCornerShape(10.dp))
          .then(
            if (selected) {
              Modifier.background(Brush.linearGradient(listOf(BrandGreenLight, BrandGreen)))
            } else {
              Modifier.background(if (darkMode) Color.White.copy(alpha = 0.05f) else BrandGreen.copy(alpha = 0.05f))
            }
          )
          .clickable { onYearSelected(y) },
        contentAlignment = Alignment.Center
      ) {
        Text(
          y.toString(),
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = if (selected) FontWeight.Black else FontWeight.SemiBold,
          color = if (selected) Color.White else designSystemContentColor(darkMode).copy(alpha = 0.85f)
        )
      }
    }
  }
}

@Composable
private fun WeekdayHeaderRow(labels: List<String>, darkMode: Boolean) {
  Row(modifier = Modifier.fillMaxWidth()) {
    labels.forEach { label ->
      Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
        Text(
          label,
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = designSystemContentColor(darkMode).copy(alpha = 0.45f)
        )
      }
    }
  }
}

@Composable
private fun DayCell(
  day: Int?,
  selected: Boolean,
  isToday: Boolean,
  darkMode: Boolean,
  onClick: () -> Unit
) {
  Box(modifier = Modifier.aspectRatio(1f), contentAlignment = Alignment.Center) {
    if (day != null) {
      val interactionSource = remember { MutableInteractionSource() }
      val pressed by interactionSource.collectIsPressedAsState()
      val scale by animateFloatAsState(
        targetValue = if (pressed) 0.85f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "calendar-day-scale"
      )
      Box(
        modifier = Modifier
          .fillMaxSize(0.82f)
          .graphicsLayer { scaleX = scale; scaleY = scale }
          .clip(CircleShape)
          .then(
            when {
              selected -> Modifier.background(Brush.linearGradient(listOf(BrandGreenLight, BrandGreen)))
              isToday -> Modifier.border(1.5.dp, BrandGreen, CircleShape)
              else -> Modifier
            }
          )
          .clickable(
            interactionSource = interactionSource,
            indication = rememberRipple(color = BrandGreen, bounded = true),
            onClick = onClick
          ),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = day.toString(),
          color = when {
            selected -> Color.White
            isToday -> BrandGreen
            else -> designSystemContentColor(darkMode).copy(alpha = 0.85f)
          },
          fontWeight = if (selected || isToday) FontWeight.Black else FontWeight.Medium,
          style = MaterialTheme.typography.bodyMedium
        )
      }
    }
  }
}

// ---- Gregorian helpers --------------------------------------------------------------------

private fun gregorianMonthCells(year: Int, month: Int): List<Int?> {
  val cal = JCalendar.getInstance()
  cal.clear()
  cal.set(year, month, 1)
  val daysInMonth = cal.getActualMaximum(JCalendar.DAY_OF_MONTH)
  val firstDow = cal.get(JCalendar.DAY_OF_WEEK) // SUNDAY=1 .. SATURDAY=7
  val mondayFirstOffset = (firstDow + 5) % 7
  val cells = MutableList<Int?>(mondayFirstOffset) { null }
  cells.addAll((1..daysInMonth).toList())
  return cells
}

private fun gregorianMillisFor(year: Int, month: Int, day: Int): Long {
  val cal = JCalendar.getInstance()
  cal.clear()
  cal.set(year, month, day, 12, 0, 0)
  return cal.timeInMillis
}

// ---- Hijri (Umm al-Qura) helpers ----------------------------------------------------------

/** Hijri "d MMMM yyyy" equivalent (e.g. "15 ذو القعدة 1447") for echoing a selected
 * [CalendarPicker] value back as text once it's confirmed — e.g. the compact date field that
 * shows the joining date after [CalendarPicker]'s sheet closes. Numerals stay Latin to match
 * every other number in the app (salary figures, etc. are never shown in Eastern Arabic numerals
 * either), only the month name and calendar system switch to Hijri. */
fun formatHijriDate(millis: Long): String {
  val d = hijriFromMillis(millis)
  return "${d.day} ${HijriMonthNames[d.month]} ${d.year}"
}

/** Hijri "MMMM yyyy" equivalent (e.g. "ذو القعدة 1447") for month-granularity fields like the
 * wizard's "calculation month". */
fun formatHijriMonthYear(millis: Long): String {
  val d = hijriFromMillis(millis)
  return "${HijriMonthNames[d.month]} ${d.year}"
}

private data class HijriDate(val year: Int, val month: Int, val day: Int)

private fun hijriCalendarInstance(): IcuCalendar =
  IcuCalendar.getInstance(ULocale.forLanguageTag("ar-SA-u-ca-islamic-umalqura"))

private fun hijriToday(): HijriDate {
  val cal = hijriCalendarInstance()
  return HijriDate(cal.get(IcuCalendar.YEAR), cal.get(IcuCalendar.MONTH), cal.get(IcuCalendar.DAY_OF_MONTH))
}

private fun hijriFromMillis(millis: Long): HijriDate {
  val cal = hijriCalendarInstance()
  cal.timeInMillis = millis
  return HijriDate(cal.get(IcuCalendar.YEAR), cal.get(IcuCalendar.MONTH), cal.get(IcuCalendar.DAY_OF_MONTH))
}

private fun hijriMonthCells(year: Int, month: Int): List<Int?> {
  val cal = hijriCalendarInstance()
  cal.clear()
  cal.set(year, month, 1)
  val daysInMonth = cal.getActualMaximum(IcuCalendar.DAY_OF_MONTH)
  val firstDow = cal.get(IcuCalendar.DAY_OF_WEEK) // SUNDAY=1 .. SATURDAY=7
  val mondayFirstOffset = (firstDow + 5) % 7
  val cells = MutableList<Int?>(mondayFirstOffset) { null }
  cells.addAll((1..daysInMonth).toList())
  return cells
}

private fun hijriMillisFor(year: Int, month: Int, day: Int): Long {
  val cal = hijriCalendarInstance()
  cal.clear()
  cal.set(year, month, day, 12, 0, 0)
  return cal.timeInMillis
}
