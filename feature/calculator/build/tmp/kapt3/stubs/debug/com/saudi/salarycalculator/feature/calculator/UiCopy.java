package com.saudi.salarycalculator.feature.calculator;

import android.content.Context;
import android.content.Intent;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.SegmentedButtonDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.graphics.StrokeCap;
import androidx.compose.ui.graphics.drawscope.Stroke;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.compose.ui.unit.LayoutDirection;
import androidx.core.content.FileProvider;
import com.saudi.salarycalculator.core.model.EmployeeType;
import com.saudi.salarycalculator.core.model.NetSalaryResult;
import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\'\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0003\b\u00b1\u0001\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B\u00d5\u0003\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u0012\u0006\u0010\f\u001a\u00020\u0003\u0012\u0006\u0010\r\u001a\u00020\u0003\u0012\u0006\u0010\u000e\u001a\u00020\u0003\u0012\u0006\u0010\u000f\u001a\u00020\u0003\u0012\u0006\u0010\u0010\u001a\u00020\u0003\u0012\u0006\u0010\u0011\u001a\u00020\u0003\u0012\u0006\u0010\u0012\u001a\u00020\u0003\u0012\u0006\u0010\u0013\u001a\u00020\u0003\u0012\u0006\u0010\u0014\u001a\u00020\u0003\u0012\u0006\u0010\u0015\u001a\u00020\u0003\u0012\u0006\u0010\u0016\u001a\u00020\u0003\u0012\u0006\u0010\u0017\u001a\u00020\u0003\u0012\u0006\u0010\u0018\u001a\u00020\u0003\u0012\u0006\u0010\u0019\u001a\u00020\u0003\u0012\u0006\u0010\u001a\u001a\u00020\u0003\u0012\u0006\u0010\u001b\u001a\u00020\u0003\u0012\u0006\u0010\u001c\u001a\u00020\u0003\u0012\u0006\u0010\u001d\u001a\u00020\u0003\u0012\u0006\u0010\u001e\u001a\u00020\u0003\u0012\u0006\u0010\u001f\u001a\u00020\u0003\u0012\u0006\u0010 \u001a\u00020\u0003\u0012\u0006\u0010!\u001a\u00020\u0003\u0012\u0006\u0010\"\u001a\u00020\u0003\u0012\u0006\u0010#\u001a\u00020\u0003\u0012\u0006\u0010$\u001a\u00020\u0003\u0012\u0006\u0010%\u001a\u00020\u0003\u0012\u0006\u0010&\u001a\u00020\u0003\u0012\u0006\u0010\'\u001a\u00020\u0003\u0012\u0006\u0010(\u001a\u00020\u0003\u0012\u0006\u0010)\u001a\u00020\u0003\u0012\u0006\u0010*\u001a\u00020\u0003\u0012\u0006\u0010+\u001a\u00020\u0003\u0012\u0006\u0010,\u001a\u00020\u0003\u0012\u0006\u0010-\u001a\u00020\u0003\u0012\u0006\u0010.\u001a\u00020\u0003\u0012\u0006\u0010/\u001a\u00020\u0003\u0012\u0006\u00100\u001a\u00020\u0003\u0012\u0006\u00101\u001a\u00020\u0003\u0012\u0006\u00102\u001a\u00020\u0003\u0012\u0006\u00103\u001a\u00020\u0003\u0012\u0006\u00104\u001a\u00020\u0003\u0012\u0006\u00105\u001a\u00020\u0003\u0012\u0006\u00106\u001a\u00020\u0003\u0012\u0006\u00107\u001a\u00020\u0003\u0012\u0006\u00108\u001a\u00020\u0003\u0012\u0006\u00109\u001a\u00020\u0003\u0012\u0006\u0010:\u001a\u00020\u0003\u0012\u0006\u0010;\u001a\u00020\u0003\u0012\u0006\u0010<\u001a\u00020\u0003\u00a2\u0006\u0002\u0010=J\t\u0010y\u001a\u00020\u0003H\u00c6\u0003J\t\u0010z\u001a\u00020\u0003H\u00c6\u0003J\t\u0010{\u001a\u00020\u0003H\u00c6\u0003J\t\u0010|\u001a\u00020\u0003H\u00c6\u0003J\t\u0010}\u001a\u00020\u0003H\u00c6\u0003J\t\u0010~\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u007f\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0080\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0081\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0082\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0083\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0084\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0085\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0086\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0087\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0088\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0089\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u008a\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u008b\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u008c\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u008d\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u008e\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u008f\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0090\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0091\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0092\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0093\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0094\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0095\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0096\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0097\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0098\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u0099\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u009a\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u009b\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u009c\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u009d\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u009e\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u009f\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00a0\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00a1\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00a2\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00a3\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00a4\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00a5\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00a6\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00a7\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00a8\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00a9\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00aa\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00ab\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00ac\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00ad\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00ae\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00af\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00b0\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00b1\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u00b2\u0001\u001a\u00020\u0003H\u00c6\u0003J\u00ce\u0004\u0010\u00b3\u0001\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00032\b\b\u0002\u0010\u0016\u001a\u00020\u00032\b\b\u0002\u0010\u0017\u001a\u00020\u00032\b\b\u0002\u0010\u0018\u001a\u00020\u00032\b\b\u0002\u0010\u0019\u001a\u00020\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u00032\b\b\u0002\u0010\u001b\u001a\u00020\u00032\b\b\u0002\u0010\u001c\u001a\u00020\u00032\b\b\u0002\u0010\u001d\u001a\u00020\u00032\b\b\u0002\u0010\u001e\u001a\u00020\u00032\b\b\u0002\u0010\u001f\u001a\u00020\u00032\b\b\u0002\u0010 \u001a\u00020\u00032\b\b\u0002\u0010!\u001a\u00020\u00032\b\b\u0002\u0010\"\u001a\u00020\u00032\b\b\u0002\u0010#\u001a\u00020\u00032\b\b\u0002\u0010$\u001a\u00020\u00032\b\b\u0002\u0010%\u001a\u00020\u00032\b\b\u0002\u0010&\u001a\u00020\u00032\b\b\u0002\u0010\'\u001a\u00020\u00032\b\b\u0002\u0010(\u001a\u00020\u00032\b\b\u0002\u0010)\u001a\u00020\u00032\b\b\u0002\u0010*\u001a\u00020\u00032\b\b\u0002\u0010+\u001a\u00020\u00032\b\b\u0002\u0010,\u001a\u00020\u00032\b\b\u0002\u0010-\u001a\u00020\u00032\b\b\u0002\u0010.\u001a\u00020\u00032\b\b\u0002\u0010/\u001a\u00020\u00032\b\b\u0002\u00100\u001a\u00020\u00032\b\b\u0002\u00101\u001a\u00020\u00032\b\b\u0002\u00102\u001a\u00020\u00032\b\b\u0002\u00103\u001a\u00020\u00032\b\b\u0002\u00104\u001a\u00020\u00032\b\b\u0002\u00105\u001a\u00020\u00032\b\b\u0002\u00106\u001a\u00020\u00032\b\b\u0002\u00107\u001a\u00020\u00032\b\b\u0002\u00108\u001a\u00020\u00032\b\b\u0002\u00109\u001a\u00020\u00032\b\b\u0002\u0010:\u001a\u00020\u00032\b\b\u0002\u0010;\u001a\u00020\u00032\b\b\u0002\u0010<\u001a\u00020\u0003H\u00c6\u0001J\u0015\u0010\u00b4\u0001\u001a\u00030\u00b5\u00012\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\u000b\u0010\u00b6\u0001\u001a\u00030\u00b7\u0001H\u00d6\u0001J\u0011\u0010\u00b8\u0001\u001a\u00020\u00032\b\u0010\u00b8\u0001\u001a\u00030\u00b9\u0001J\n\u0010\u00ba\u0001\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010?R\u0011\u0010\u001d\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010?R\u0011\u0010\u0010\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u0010?R\u0011\u0010.\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010?R\u0011\u0010$\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u0010?R\u0011\u0010\u001c\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010?R\u0011\u0010\u0019\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bE\u0010?R\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bF\u0010?R\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u0010?R\u0011\u00108\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bH\u0010?R\u0011\u0010#\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bI\u0010?R\u0011\u0010/\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bJ\u0010?R\u0011\u0010*\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bK\u0010?R\u0011\u0010\u001a\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bL\u0010?R\u0011\u0010\u001b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bM\u0010?R\u0011\u0010\u0013\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bN\u0010?R\u0011\u00104\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bO\u0010?R\u0011\u00100\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bP\u0010?R\u0011\u0010&\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bQ\u0010?R\u0011\u0010;\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bR\u0010?R\u0011\u0010<\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bS\u0010?R\u0011\u0010\u0017\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bT\u0010?R\u0011\u00103\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bU\u0010?R\u0011\u0010 \u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bV\u0010?R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bW\u0010?R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bX\u0010?R\u0011\u0010\u001e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bY\u0010?R\u0011\u0010+\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bZ\u0010?R\u0011\u0010!\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b[\u0010?R\u0011\u0010\'\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\\\u0010?R\u0011\u0010\u0011\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b]\u0010?R\u0011\u00105\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b^\u0010?R\u0011\u00101\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b_\u0010?R\u0011\u0010)\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b`\u0010?R\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\ba\u0010?R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bb\u0010?R\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bc\u0010?R\u0011\u0010\"\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bd\u0010?R\u0011\u00102\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\be\u0010?R\u0011\u0010\u0016\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bf\u0010?R\u0011\u0010-\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bg\u0010?R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bh\u0010?R\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bi\u0010?R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bj\u0010?R\u0011\u0010%\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bk\u0010?R\u0011\u00109\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bl\u0010?R\u0011\u0010:\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bm\u0010?R\u0011\u0010\u0018\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bn\u0010?R\u0011\u0010\u0014\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bo\u0010?R\u0011\u00107\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bp\u0010?R\u0011\u0010\u0015\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bq\u0010?R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\br\u0010?R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bs\u0010?R\u0011\u0010\u001f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bt\u0010?R\u0011\u0010(\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bu\u0010?R\u0011\u0010\u0012\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bv\u0010?R\u0011\u00106\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bw\u0010?R\u0011\u0010,\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bx\u0010?\u00a8\u0006\u00bb\u0001"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/UiCopy;", "", "appName", "", "tagline", "heroTitle", "heroBody", "start", "salaryInput", "resultDashboard", "runCalculator", "openSalaryInput", "currentOffer", "newOffer", "compareOffers", "offerScore", "betterOffer", "monthlyDifference", "yearlyDifference", "eosTitle", "savingsPlanner", "settings", "reportPreview", "exportPdf", "saved", "clear", "emptyHistory", "emptySummary", "calculatedAck", "basic", "housing", "transport", "food", "mobile", "other", "deductions", "calculateTakeHome", "saudi", "expat", "monthly", "yearly", "netWage", "employerCost", "lastBasic", "years", "resignation", "calculateEos", "eligibleYears", "estimatedBenefit", "netOverride", "rent", "family", "estimateSavings", "monthlySavings", "yearlySavings", "savingsRate", "darkMode", "saudiEmployeeRate", "saudiEmployerRate", "expatEmployeeRate", "expatHazardRate", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAppName", "()Ljava/lang/String;", "getBasic", "getBetterOffer", "getCalculateEos", "getCalculateTakeHome", "getCalculatedAck", "getClear", "getCompareOffers", "getCurrentOffer", "getDarkMode", "getDeductions", "getEligibleYears", "getEmployerCost", "getEmptyHistory", "getEmptySummary", "getEosTitle", "getEstimateSavings", "getEstimatedBenefit", "getExpat", "getExpatEmployeeRate", "getExpatHazardRate", "getExportPdf", "getFamily", "getFood", "getHeroBody", "getHeroTitle", "getHousing", "getLastBasic", "getMobile", "getMonthly", "getMonthlyDifference", "getMonthlySavings", "getNetOverride", "getNetWage", "getNewOffer", "getOfferScore", "getOpenSalaryInput", "getOther", "getRent", "getReportPreview", "getResignation", "getResultDashboard", "getRunCalculator", "getSalaryInput", "getSaudi", "getSaudiEmployeeRate", "getSaudiEmployerRate", "getSaved", "getSavingsPlanner", "getSavingsRate", "getSettings", "getStart", "getTagline", "getTransport", "getYearly", "getYearlyDifference", "getYearlySavings", "getYears", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component29", "component3", "component30", "component31", "component32", "component33", "component34", "component35", "component36", "component37", "component38", "component39", "component4", "component40", "component41", "component42", "component43", "component44", "component45", "component46", "component47", "component48", "component49", "component5", "component50", "component51", "component52", "component53", "component54", "component55", "component56", "component57", "component58", "component6", "component7", "component8", "component9", "copy", "equals", "", "hashCode", "", "tab", "Lcom/saudi/salarycalculator/feature/calculator/AppTab;", "toString", "calculator_debug"})
final class UiCopy {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String appName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String tagline = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String heroTitle = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String heroBody = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String start = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String salaryInput = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String resultDashboard = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String runCalculator = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String openSalaryInput = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String currentOffer = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String newOffer = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String compareOffers = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerScore = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String betterOffer = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String monthlyDifference = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String yearlyDifference = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String eosTitle = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String savingsPlanner = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String settings = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String reportPreview = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String exportPdf = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String saved = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String clear = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String emptyHistory = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String emptySummary = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String calculatedAck = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String basic = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String housing = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String transport = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String food = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String mobile = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String other = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String deductions = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String calculateTakeHome = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String saudi = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String expat = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String monthly = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String yearly = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String netWage = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String employerCost = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String lastBasic = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String years = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String resignation = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String calculateEos = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String eligibleYears = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String estimatedBenefit = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String netOverride = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String rent = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String family = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String estimateSavings = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String monthlySavings = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String yearlySavings = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String savingsRate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String darkMode = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String saudiEmployeeRate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String saudiEmployerRate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String expatEmployeeRate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String expatHazardRate = null;
    
    public UiCopy(@org.jetbrains.annotations.NotNull()
    java.lang.String appName, @org.jetbrains.annotations.NotNull()
    java.lang.String tagline, @org.jetbrains.annotations.NotNull()
    java.lang.String heroTitle, @org.jetbrains.annotations.NotNull()
    java.lang.String heroBody, @org.jetbrains.annotations.NotNull()
    java.lang.String start, @org.jetbrains.annotations.NotNull()
    java.lang.String salaryInput, @org.jetbrains.annotations.NotNull()
    java.lang.String resultDashboard, @org.jetbrains.annotations.NotNull()
    java.lang.String runCalculator, @org.jetbrains.annotations.NotNull()
    java.lang.String openSalaryInput, @org.jetbrains.annotations.NotNull()
    java.lang.String currentOffer, @org.jetbrains.annotations.NotNull()
    java.lang.String newOffer, @org.jetbrains.annotations.NotNull()
    java.lang.String compareOffers, @org.jetbrains.annotations.NotNull()
    java.lang.String offerScore, @org.jetbrains.annotations.NotNull()
    java.lang.String betterOffer, @org.jetbrains.annotations.NotNull()
    java.lang.String monthlyDifference, @org.jetbrains.annotations.NotNull()
    java.lang.String yearlyDifference, @org.jetbrains.annotations.NotNull()
    java.lang.String eosTitle, @org.jetbrains.annotations.NotNull()
    java.lang.String savingsPlanner, @org.jetbrains.annotations.NotNull()
    java.lang.String settings, @org.jetbrains.annotations.NotNull()
    java.lang.String reportPreview, @org.jetbrains.annotations.NotNull()
    java.lang.String exportPdf, @org.jetbrains.annotations.NotNull()
    java.lang.String saved, @org.jetbrains.annotations.NotNull()
    java.lang.String clear, @org.jetbrains.annotations.NotNull()
    java.lang.String emptyHistory, @org.jetbrains.annotations.NotNull()
    java.lang.String emptySummary, @org.jetbrains.annotations.NotNull()
    java.lang.String calculatedAck, @org.jetbrains.annotations.NotNull()
    java.lang.String basic, @org.jetbrains.annotations.NotNull()
    java.lang.String housing, @org.jetbrains.annotations.NotNull()
    java.lang.String transport, @org.jetbrains.annotations.NotNull()
    java.lang.String food, @org.jetbrains.annotations.NotNull()
    java.lang.String mobile, @org.jetbrains.annotations.NotNull()
    java.lang.String other, @org.jetbrains.annotations.NotNull()
    java.lang.String deductions, @org.jetbrains.annotations.NotNull()
    java.lang.String calculateTakeHome, @org.jetbrains.annotations.NotNull()
    java.lang.String saudi, @org.jetbrains.annotations.NotNull()
    java.lang.String expat, @org.jetbrains.annotations.NotNull()
    java.lang.String monthly, @org.jetbrains.annotations.NotNull()
    java.lang.String yearly, @org.jetbrains.annotations.NotNull()
    java.lang.String netWage, @org.jetbrains.annotations.NotNull()
    java.lang.String employerCost, @org.jetbrains.annotations.NotNull()
    java.lang.String lastBasic, @org.jetbrains.annotations.NotNull()
    java.lang.String years, @org.jetbrains.annotations.NotNull()
    java.lang.String resignation, @org.jetbrains.annotations.NotNull()
    java.lang.String calculateEos, @org.jetbrains.annotations.NotNull()
    java.lang.String eligibleYears, @org.jetbrains.annotations.NotNull()
    java.lang.String estimatedBenefit, @org.jetbrains.annotations.NotNull()
    java.lang.String netOverride, @org.jetbrains.annotations.NotNull()
    java.lang.String rent, @org.jetbrains.annotations.NotNull()
    java.lang.String family, @org.jetbrains.annotations.NotNull()
    java.lang.String estimateSavings, @org.jetbrains.annotations.NotNull()
    java.lang.String monthlySavings, @org.jetbrains.annotations.NotNull()
    java.lang.String yearlySavings, @org.jetbrains.annotations.NotNull()
    java.lang.String savingsRate, @org.jetbrains.annotations.NotNull()
    java.lang.String darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String saudiEmployeeRate, @org.jetbrains.annotations.NotNull()
    java.lang.String saudiEmployerRate, @org.jetbrains.annotations.NotNull()
    java.lang.String expatEmployeeRate, @org.jetbrains.annotations.NotNull()
    java.lang.String expatHazardRate) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAppName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTagline() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHeroTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHeroBody() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getStart() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSalaryInput() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getResultDashboard() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRunCalculator() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOpenSalaryInput() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentOffer() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNewOffer() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCompareOffers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferScore() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBetterOffer() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMonthlyDifference() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getYearlyDifference() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEosTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSavingsPlanner() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSettings() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getReportPreview() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getExportPdf() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSaved() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getClear() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmptyHistory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmptySummary() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCalculatedAck() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBasic() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHousing() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTransport() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFood() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMobile() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOther() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDeductions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCalculateTakeHome() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSaudi() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getExpat() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMonthly() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getYearly() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNetWage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmployerCost() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLastBasic() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getYears() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getResignation() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCalculateEos() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEligibleYears() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEstimatedBenefit() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNetOverride() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFamily() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEstimateSavings() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMonthlySavings() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getYearlySavings() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSavingsRate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDarkMode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSaudiEmployeeRate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSaudiEmployerRate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getExpatEmployeeRate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getExpatHazardRate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String tab(@org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.feature.calculator.AppTab tab) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component17() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component18() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component19() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component20() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component21() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component22() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component23() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component24() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component25() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component26() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component27() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component28() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component29() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component30() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component31() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component32() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component33() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component34() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component35() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component36() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component37() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component38() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component39() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component40() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component41() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component42() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component43() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component44() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component45() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component46() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component47() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component48() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component49() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component50() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component51() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component52() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component53() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component54() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component55() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component56() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component57() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component58() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.feature.calculator.UiCopy copy(@org.jetbrains.annotations.NotNull()
    java.lang.String appName, @org.jetbrains.annotations.NotNull()
    java.lang.String tagline, @org.jetbrains.annotations.NotNull()
    java.lang.String heroTitle, @org.jetbrains.annotations.NotNull()
    java.lang.String heroBody, @org.jetbrains.annotations.NotNull()
    java.lang.String start, @org.jetbrains.annotations.NotNull()
    java.lang.String salaryInput, @org.jetbrains.annotations.NotNull()
    java.lang.String resultDashboard, @org.jetbrains.annotations.NotNull()
    java.lang.String runCalculator, @org.jetbrains.annotations.NotNull()
    java.lang.String openSalaryInput, @org.jetbrains.annotations.NotNull()
    java.lang.String currentOffer, @org.jetbrains.annotations.NotNull()
    java.lang.String newOffer, @org.jetbrains.annotations.NotNull()
    java.lang.String compareOffers, @org.jetbrains.annotations.NotNull()
    java.lang.String offerScore, @org.jetbrains.annotations.NotNull()
    java.lang.String betterOffer, @org.jetbrains.annotations.NotNull()
    java.lang.String monthlyDifference, @org.jetbrains.annotations.NotNull()
    java.lang.String yearlyDifference, @org.jetbrains.annotations.NotNull()
    java.lang.String eosTitle, @org.jetbrains.annotations.NotNull()
    java.lang.String savingsPlanner, @org.jetbrains.annotations.NotNull()
    java.lang.String settings, @org.jetbrains.annotations.NotNull()
    java.lang.String reportPreview, @org.jetbrains.annotations.NotNull()
    java.lang.String exportPdf, @org.jetbrains.annotations.NotNull()
    java.lang.String saved, @org.jetbrains.annotations.NotNull()
    java.lang.String clear, @org.jetbrains.annotations.NotNull()
    java.lang.String emptyHistory, @org.jetbrains.annotations.NotNull()
    java.lang.String emptySummary, @org.jetbrains.annotations.NotNull()
    java.lang.String calculatedAck, @org.jetbrains.annotations.NotNull()
    java.lang.String basic, @org.jetbrains.annotations.NotNull()
    java.lang.String housing, @org.jetbrains.annotations.NotNull()
    java.lang.String transport, @org.jetbrains.annotations.NotNull()
    java.lang.String food, @org.jetbrains.annotations.NotNull()
    java.lang.String mobile, @org.jetbrains.annotations.NotNull()
    java.lang.String other, @org.jetbrains.annotations.NotNull()
    java.lang.String deductions, @org.jetbrains.annotations.NotNull()
    java.lang.String calculateTakeHome, @org.jetbrains.annotations.NotNull()
    java.lang.String saudi, @org.jetbrains.annotations.NotNull()
    java.lang.String expat, @org.jetbrains.annotations.NotNull()
    java.lang.String monthly, @org.jetbrains.annotations.NotNull()
    java.lang.String yearly, @org.jetbrains.annotations.NotNull()
    java.lang.String netWage, @org.jetbrains.annotations.NotNull()
    java.lang.String employerCost, @org.jetbrains.annotations.NotNull()
    java.lang.String lastBasic, @org.jetbrains.annotations.NotNull()
    java.lang.String years, @org.jetbrains.annotations.NotNull()
    java.lang.String resignation, @org.jetbrains.annotations.NotNull()
    java.lang.String calculateEos, @org.jetbrains.annotations.NotNull()
    java.lang.String eligibleYears, @org.jetbrains.annotations.NotNull()
    java.lang.String estimatedBenefit, @org.jetbrains.annotations.NotNull()
    java.lang.String netOverride, @org.jetbrains.annotations.NotNull()
    java.lang.String rent, @org.jetbrains.annotations.NotNull()
    java.lang.String family, @org.jetbrains.annotations.NotNull()
    java.lang.String estimateSavings, @org.jetbrains.annotations.NotNull()
    java.lang.String monthlySavings, @org.jetbrains.annotations.NotNull()
    java.lang.String yearlySavings, @org.jetbrains.annotations.NotNull()
    java.lang.String savingsRate, @org.jetbrains.annotations.NotNull()
    java.lang.String darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String saudiEmployeeRate, @org.jetbrains.annotations.NotNull()
    java.lang.String saudiEmployerRate, @org.jetbrains.annotations.NotNull()
    java.lang.String expatEmployeeRate, @org.jetbrains.annotations.NotNull()
    java.lang.String expatHazardRate) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}