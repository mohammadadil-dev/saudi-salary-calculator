package com.saudi.salarycalculator.feature.calculator

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream

object PdfReportExporter {
  fun write(file: File, report: String) {
    val document = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = document.startPage(pageInfo)
    drawReport(page.canvas, report)
    document.finishPage(page)
    FileOutputStream(file).use { document.writeTo(it) }
    document.close()
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
