package com.saudi.salarycalculator.feature.calculator

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream

object PdfReportExporter {
  /** [watermarkText], when provided, is tiled diagonally behind the report text as a faint
   * repeated mark (e.g. "UNOFFICIAL - NOT EMPLOYER-CERTIFIED" for the salary certificate export).
   * It's drawn first so the report text always paints on top and stays fully readable; the
   * disclaimer should also be included as plain first-class text within [report] itself, since a
   * faint diagonal watermark alone is easy to crop out or overlook. */
  fun write(file: File, report: String, watermarkText: String? = null) {
    val document = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = document.startPage(pageInfo)
    watermarkText?.let { drawWatermark(page.canvas, it) }
    drawReport(page.canvas, report)
    document.finishPage(page)
    FileOutputStream(file).use { document.writeTo(it) }
    document.close()
  }

  private fun drawWatermark(canvas: Canvas, text: String) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      textSize = 26f
      isFakeBoldText = true
      color = Color.argb(38, 200, 30, 30)
      textAlign = Paint.Align.CENTER
    }
    canvas.save()
    canvas.translate(297f, 421f) // page center (595x842 page)
    canvas.rotate(-35f)
    var y = -450f
    while (y < 500f) {
      canvas.drawText(text, 0f, y, paint)
      y += 100f
    }
    canvas.restore()
  }

  private fun drawReport(canvas: Canvas, report: String) {
    val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      textSize = 22f
      isFakeBoldText = true
    }
    val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      textSize = 13f
    }
    canvas.drawText("Saudi Salary Calculator", 40f, 52f, titlePaint)
    var y = 92f
    report.lines().forEach { line ->
      canvas.drawText(line.take(86), 40f, y, bodyPaint)
      y += 22f
    }
  }
}
