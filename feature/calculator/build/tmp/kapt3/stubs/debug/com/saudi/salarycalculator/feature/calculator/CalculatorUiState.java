package com.saudi.salarycalculator.feature.calculator;

import androidx.lifecycle.ViewModel;
import com.saudi.salarycalculator.core.data.SalaryRepository;
import com.saudi.salarycalculator.core.model.CalculationRecord;
import com.saudi.salarycalculator.core.model.CalculationType;
import com.saudi.salarycalculator.core.model.EmployeeType;
import com.saudi.salarycalculator.core.model.EndOfServiceInput;
import com.saudi.salarycalculator.core.model.EndOfServiceResult;
import com.saudi.salarycalculator.core.model.GosiInput;
import com.saudi.salarycalculator.core.model.GosiRates;
import com.saudi.salarycalculator.core.model.NetSalaryInput;
import com.saudi.salarycalculator.core.model.NetSalaryResult;
import com.saudi.salarycalculator.core.model.OfferComparisonResult;
import com.saudi.salarycalculator.core.model.OfferInput;
import com.saudi.salarycalculator.core.model.SavingsInput;
import com.saudi.salarycalculator.core.model.SavingsResult;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b%\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\bp\b\u0086\b\u0018\u00002\u00020\u0001B\u00f5\u0003\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\b\u0012\b\b\u0002\u0010\f\u001a\u00020\b\u0012\b\b\u0002\u0010\r\u001a\u00020\b\u0012\b\b\u0002\u0010\u000e\u001a\u00020\b\u0012\b\b\u0002\u0010\u000f\u001a\u00020\b\u0012\b\b\u0002\u0010\u0010\u001a\u00020\b\u0012\b\b\u0002\u0010\u0011\u001a\u00020\b\u0012\b\b\u0002\u0010\u0012\u001a\u00020\b\u0012\b\b\u0002\u0010\u0013\u001a\u00020\b\u0012\b\b\u0002\u0010\u0014\u001a\u00020\b\u0012\b\b\u0002\u0010\u0015\u001a\u00020\b\u0012\b\b\u0002\u0010\u0016\u001a\u00020\b\u0012\b\b\u0002\u0010\u0017\u001a\u00020\b\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0019\u001a\u00020\b\u0012\b\b\u0002\u0010\u001a\u001a\u00020\b\u0012\b\b\u0002\u0010\u001b\u001a\u00020\b\u0012\b\b\u0002\u0010\u001c\u001a\u00020\b\u0012\b\b\u0002\u0010\u001d\u001a\u00020\b\u0012\b\b\u0002\u0010\u001e\u001a\u00020\b\u0012\b\b\u0002\u0010\u001f\u001a\u00020\b\u0012\b\b\u0002\u0010 \u001a\u00020\b\u0012\b\b\u0002\u0010!\u001a\u00020\b\u0012\b\b\u0002\u0010\"\u001a\u00020\b\u0012\b\b\u0002\u0010#\u001a\u00020\b\u0012\b\b\u0002\u0010$\u001a\u00020\b\u0012\b\b\u0002\u0010%\u001a\u00020\b\u0012\b\b\u0002\u0010&\u001a\u00020\b\u0012\b\b\u0002\u0010\'\u001a\u00020\b\u0012\b\b\u0002\u0010(\u001a\u00020\b\u0012\b\b\u0002\u0010)\u001a\u00020\b\u0012\b\b\u0002\u0010*\u001a\u00020\b\u0012\b\b\u0002\u0010+\u001a\u00020\b\u0012\b\b\u0002\u0010,\u001a\u00020\b\u0012\b\b\u0002\u0010-\u001a\u00020\b\u0012\b\b\u0002\u0010.\u001a\u00020\b\u0012\n\b\u0002\u0010/\u001a\u0004\u0018\u000100\u0012\n\b\u0002\u00101\u001a\u0004\u0018\u000102\u0012\n\b\u0002\u00103\u001a\u0004\u0018\u000104\u0012\n\b\u0002\u00105\u001a\u0004\u0018\u000106\u0012\n\b\u0002\u00107\u001a\u0004\u0018\u00010\b\u0012\b\b\u0002\u00108\u001a\u00020\b\u0012\u000e\b\u0002\u00109\u001a\b\u0012\u0004\u0012\u00020;0:\u00a2\u0006\u0002\u0010<J\t\u0010v\u001a\u00020\u0003H\u00c6\u0003J\t\u0010w\u001a\u00020\bH\u00c6\u0003J\t\u0010x\u001a\u00020\bH\u00c6\u0003J\t\u0010y\u001a\u00020\bH\u00c6\u0003J\t\u0010z\u001a\u00020\bH\u00c6\u0003J\t\u0010{\u001a\u00020\bH\u00c6\u0003J\t\u0010|\u001a\u00020\bH\u00c6\u0003J\t\u0010}\u001a\u00020\bH\u00c6\u0003J\t\u0010~\u001a\u00020\bH\u00c6\u0003J\t\u0010\u007f\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0080\u0001\u001a\u00020\u0005H\u00c6\u0003J\n\u0010\u0081\u0001\u001a\u00020\u0005H\u00c6\u0003J\n\u0010\u0082\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0083\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0084\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0085\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0086\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0087\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0088\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0089\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u008a\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u008b\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u008c\u0001\u001a\u00020\u0005H\u00c6\u0003J\n\u0010\u008d\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u008e\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u008f\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0090\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0091\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0092\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0093\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0094\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0095\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0096\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0097\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0098\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u0099\u0001\u001a\u00020\bH\u00c6\u0003J\f\u0010\u009a\u0001\u001a\u0004\u0018\u000100H\u00c6\u0003J\f\u0010\u009b\u0001\u001a\u0004\u0018\u000102H\u00c6\u0003J\f\u0010\u009c\u0001\u001a\u0004\u0018\u000104H\u00c6\u0003J\f\u0010\u009d\u0001\u001a\u0004\u0018\u000106H\u00c6\u0003J\f\u0010\u009e\u0001\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\n\u0010\u009f\u0001\u001a\u00020\bH\u00c6\u0003J\u0010\u0010\u00a0\u0001\u001a\b\u0012\u0004\u0012\u00020;0:H\u00c6\u0003J\n\u0010\u00a1\u0001\u001a\u00020\nH\u00c6\u0003J\n\u0010\u00a2\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u00a3\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u00a4\u0001\u001a\u00020\bH\u00c6\u0003J\n\u0010\u00a5\u0001\u001a\u00020\bH\u00c6\u0003J\u00fa\u0003\u0010\u00a6\u0001\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\b2\b\b\u0002\u0010\f\u001a\u00020\b2\b\b\u0002\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\u000e\u001a\u00020\b2\b\b\u0002\u0010\u000f\u001a\u00020\b2\b\b\u0002\u0010\u0010\u001a\u00020\b2\b\b\u0002\u0010\u0011\u001a\u00020\b2\b\b\u0002\u0010\u0012\u001a\u00020\b2\b\b\u0002\u0010\u0013\u001a\u00020\b2\b\b\u0002\u0010\u0014\u001a\u00020\b2\b\b\u0002\u0010\u0015\u001a\u00020\b2\b\b\u0002\u0010\u0016\u001a\u00020\b2\b\b\u0002\u0010\u0017\u001a\u00020\b2\b\b\u0002\u0010\u0018\u001a\u00020\u00052\b\b\u0002\u0010\u0019\u001a\u00020\b2\b\b\u0002\u0010\u001a\u001a\u00020\b2\b\b\u0002\u0010\u001b\u001a\u00020\b2\b\b\u0002\u0010\u001c\u001a\u00020\b2\b\b\u0002\u0010\u001d\u001a\u00020\b2\b\b\u0002\u0010\u001e\u001a\u00020\b2\b\b\u0002\u0010\u001f\u001a\u00020\b2\b\b\u0002\u0010 \u001a\u00020\b2\b\b\u0002\u0010!\u001a\u00020\b2\b\b\u0002\u0010\"\u001a\u00020\b2\b\b\u0002\u0010#\u001a\u00020\b2\b\b\u0002\u0010$\u001a\u00020\b2\b\b\u0002\u0010%\u001a\u00020\b2\b\b\u0002\u0010&\u001a\u00020\b2\b\b\u0002\u0010\'\u001a\u00020\b2\b\b\u0002\u0010(\u001a\u00020\b2\b\b\u0002\u0010)\u001a\u00020\b2\b\b\u0002\u0010*\u001a\u00020\b2\b\b\u0002\u0010+\u001a\u00020\b2\b\b\u0002\u0010,\u001a\u00020\b2\b\b\u0002\u0010-\u001a\u00020\b2\b\b\u0002\u0010.\u001a\u00020\b2\n\b\u0002\u0010/\u001a\u0004\u0018\u0001002\n\b\u0002\u00101\u001a\u0004\u0018\u0001022\n\b\u0002\u00103\u001a\u0004\u0018\u0001042\n\b\u0002\u00105\u001a\u0004\u0018\u0001062\n\b\u0002\u00107\u001a\u0004\u0018\u00010\b2\b\b\u0002\u00108\u001a\u00020\b2\u000e\b\u0002\u00109\u001a\b\u0012\u0004\u0012\u00020;0:H\u00c6\u0001J\u0015\u0010\u00a7\u0001\u001a\u00020\u00052\t\u0010\u00a8\u0001\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\n\u0010\u00a9\u0001\u001a\u00020\u0003H\u00d6\u0001J\n\u0010\u00aa\u0001\u001a\u00020\bH\u00d6\u0001R\u0011\u0010\u000b\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010>R\u0013\u00107\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010>R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010AR\u0011\u0010\u0011\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010>R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u0010DR\u0013\u00101\u001a\u0004\u0018\u000102\u00a2\u0006\b\n\u0000\u001a\u0004\bE\u0010FR\u0011\u0010\u0016\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u0010>R\u0011\u0010\u0018\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\bH\u0010AR\u0011\u0010\u0017\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bI\u0010>R\u0011\u0010\u0014\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bJ\u0010>R\u0011\u0010\u0015\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bK\u0010>R\u0011\u0010-\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bL\u0010>R\u0011\u0010\u000e\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bM\u0010>R\u0011\u0010+\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bN\u0010>R\u0017\u00109\u001a\b\u0012\u0004\u0012\u00020;0:\u00a2\u0006\b\n\u0000\u001a\u0004\bO\u0010PR\u0011\u0010\f\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bQ\u0010>R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bR\u0010>R\u0011\u0010\u000f\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bS\u0010>R\u0013\u0010/\u001a\u0004\u0018\u000100\u00a2\u0006\b\n\u0000\u001a\u0004\bT\u0010UR\u0011\u0010\u001a\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bV\u0010>R\u0011\u0010 \u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bW\u0010>R\u0011\u0010\u001d\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bX\u0010>R\u0011\u0010\u001b\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bY\u0010>R\u0011\u0010\u001e\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bZ\u0010>R\u0011\u0010\u001f\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b[\u0010>R\u0011\u0010\u0019\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\\\u0010>R\u0011\u0010\u001c\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b]\u0010>R\u0011\u0010\"\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b^\u0010>R\u0011\u0010(\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b_\u0010>R\u0011\u0010%\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b`\u0010>R\u0011\u0010#\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\ba\u0010>R\u0011\u0010&\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bb\u0010>R\u0011\u0010\'\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bc\u0010>R\u0011\u0010!\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bd\u0010>R\u0011\u0010$\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\be\u0010>R\u0013\u00103\u001a\u0004\u0018\u000104\u00a2\u0006\b\n\u0000\u001a\u0004\bf\u0010gR\u0011\u0010\u0010\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bh\u0010>R\u0011\u0010.\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bi\u0010>R\u0011\u0010*\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bj\u0010>R\u0011\u00108\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bk\u0010>R\u0011\u0010\u0012\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bl\u0010>R\u0011\u0010\u0013\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bm\u0010>R\u0011\u0010)\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bn\u0010>R\u0013\u00105\u001a\u0004\u0018\u000106\u00a2\u0006\b\n\u0000\u001a\u0004\bo\u0010pR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bq\u0010rR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\bs\u0010AR\u0011\u0010\r\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bt\u0010>R\u0011\u0010,\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bu\u0010>\u00a8\u0006\u00ab\u0001"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/CalculatorUiState;", "", "selectedTab", "", "showYearly", "", "darkMode", "language", "", "employeeType", "Lcom/saudi/salarycalculator/core/model/EmployeeType;", "basicSalary", "housingAllowance", "transportAllowance", "foodAllowance", "mobileAllowance", "otherAllowances", "deductions", "saudiEmployeeGosiRate", "saudiEmployerGosiRate", "expatEmployeeGosiRate", "expatEmployerHazardRate", "eosLastSalary", "eosYears", "eosResigned", "offerATitle", "offerABasic", "offerAHousing", "offerATransport", "offerAFood", "offerAMobile", "offerAOther", "offerADeduction", "offerBTitle", "offerBBasic", "offerBHousing", "offerBTransport", "offerBFood", "offerBMobile", "offerBOther", "offerBDeduction", "savingsNetSalary", "rentExpense", "foodExpense", "transportExpense", "familyExpense", "otherExpense", "netSalaryResult", "Lcom/saudi/salarycalculator/core/model/NetSalaryResult;", "endOfServiceResult", "Lcom/saudi/salarycalculator/core/model/EndOfServiceResult;", "offerComparisonResult", "Lcom/saudi/salarycalculator/core/model/OfferComparisonResult;", "savingsResult", "Lcom/saudi/salarycalculator/core/model/SavingsResult;", "calculationAck", "reportPreview", "history", "", "Lcom/saudi/salarycalculator/core/model/CalculationRecord;", "(IZZLjava/lang/String;Lcom/saudi/salarycalculator/core/model/EmployeeType;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/saudi/salarycalculator/core/model/NetSalaryResult;Lcom/saudi/salarycalculator/core/model/EndOfServiceResult;Lcom/saudi/salarycalculator/core/model/OfferComparisonResult;Lcom/saudi/salarycalculator/core/model/SavingsResult;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;)V", "getBasicSalary", "()Ljava/lang/String;", "getCalculationAck", "getDarkMode", "()Z", "getDeductions", "getEmployeeType", "()Lcom/saudi/salarycalculator/core/model/EmployeeType;", "getEndOfServiceResult", "()Lcom/saudi/salarycalculator/core/model/EndOfServiceResult;", "getEosLastSalary", "getEosResigned", "getEosYears", "getExpatEmployeeGosiRate", "getExpatEmployerHazardRate", "getFamilyExpense", "getFoodAllowance", "getFoodExpense", "getHistory", "()Ljava/util/List;", "getHousingAllowance", "getLanguage", "getMobileAllowance", "getNetSalaryResult", "()Lcom/saudi/salarycalculator/core/model/NetSalaryResult;", "getOfferABasic", "getOfferADeduction", "getOfferAFood", "getOfferAHousing", "getOfferAMobile", "getOfferAOther", "getOfferATitle", "getOfferATransport", "getOfferBBasic", "getOfferBDeduction", "getOfferBFood", "getOfferBHousing", "getOfferBMobile", "getOfferBOther", "getOfferBTitle", "getOfferBTransport", "getOfferComparisonResult", "()Lcom/saudi/salarycalculator/core/model/OfferComparisonResult;", "getOtherAllowances", "getOtherExpense", "getRentExpense", "getReportPreview", "getSaudiEmployeeGosiRate", "getSaudiEmployerGosiRate", "getSavingsNetSalary", "getSavingsResult", "()Lcom/saudi/salarycalculator/core/model/SavingsResult;", "getSelectedTab", "()I", "getShowYearly", "getTransportAllowance", "getTransportExpense", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component29", "component3", "component30", "component31", "component32", "component33", "component34", "component35", "component36", "component37", "component38", "component39", "component4", "component40", "component41", "component42", "component43", "component44", "component45", "component46", "component47", "component48", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "calculator_debug"})
public final class CalculatorUiState {
    private final int selectedTab = 0;
    private final boolean showYearly = false;
    private final boolean darkMode = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String language = null;
    @org.jetbrains.annotations.NotNull()
    private final com.saudi.salarycalculator.core.model.EmployeeType employeeType = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String basicSalary = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String housingAllowance = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String transportAllowance = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String foodAllowance = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String mobileAllowance = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String otherAllowances = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String deductions = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String saudiEmployeeGosiRate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String saudiEmployerGosiRate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String expatEmployeeGosiRate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String expatEmployerHazardRate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String eosLastSalary = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String eosYears = null;
    private final boolean eosResigned = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerATitle = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerABasic = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerAHousing = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerATransport = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerAFood = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerAMobile = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerAOther = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerADeduction = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerBTitle = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerBBasic = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerBHousing = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerBTransport = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerBFood = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerBMobile = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerBOther = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String offerBDeduction = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String savingsNetSalary = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String rentExpense = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String foodExpense = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String transportExpense = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String familyExpense = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String otherExpense = null;
    @org.jetbrains.annotations.Nullable()
    private final com.saudi.salarycalculator.core.model.NetSalaryResult netSalaryResult = null;
    @org.jetbrains.annotations.Nullable()
    private final com.saudi.salarycalculator.core.model.EndOfServiceResult endOfServiceResult = null;
    @org.jetbrains.annotations.Nullable()
    private final com.saudi.salarycalculator.core.model.OfferComparisonResult offerComparisonResult = null;
    @org.jetbrains.annotations.Nullable()
    private final com.saudi.salarycalculator.core.model.SavingsResult savingsResult = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String calculationAck = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String reportPreview = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.saudi.salarycalculator.core.model.CalculationRecord> history = null;
    
    public CalculatorUiState(int selectedTab, boolean showYearly, boolean darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String language, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.EmployeeType employeeType, @org.jetbrains.annotations.NotNull()
    java.lang.String basicSalary, @org.jetbrains.annotations.NotNull()
    java.lang.String housingAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String transportAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String foodAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String mobileAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String otherAllowances, @org.jetbrains.annotations.NotNull()
    java.lang.String deductions, @org.jetbrains.annotations.NotNull()
    java.lang.String saudiEmployeeGosiRate, @org.jetbrains.annotations.NotNull()
    java.lang.String saudiEmployerGosiRate, @org.jetbrains.annotations.NotNull()
    java.lang.String expatEmployeeGosiRate, @org.jetbrains.annotations.NotNull()
    java.lang.String expatEmployerHazardRate, @org.jetbrains.annotations.NotNull()
    java.lang.String eosLastSalary, @org.jetbrains.annotations.NotNull()
    java.lang.String eosYears, boolean eosResigned, @org.jetbrains.annotations.NotNull()
    java.lang.String offerATitle, @org.jetbrains.annotations.NotNull()
    java.lang.String offerABasic, @org.jetbrains.annotations.NotNull()
    java.lang.String offerAHousing, @org.jetbrains.annotations.NotNull()
    java.lang.String offerATransport, @org.jetbrains.annotations.NotNull()
    java.lang.String offerAFood, @org.jetbrains.annotations.NotNull()
    java.lang.String offerAMobile, @org.jetbrains.annotations.NotNull()
    java.lang.String offerAOther, @org.jetbrains.annotations.NotNull()
    java.lang.String offerADeduction, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBTitle, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBBasic, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBHousing, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBTransport, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBFood, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBMobile, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBOther, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBDeduction, @org.jetbrains.annotations.NotNull()
    java.lang.String savingsNetSalary, @org.jetbrains.annotations.NotNull()
    java.lang.String rentExpense, @org.jetbrains.annotations.NotNull()
    java.lang.String foodExpense, @org.jetbrains.annotations.NotNull()
    java.lang.String transportExpense, @org.jetbrains.annotations.NotNull()
    java.lang.String familyExpense, @org.jetbrains.annotations.NotNull()
    java.lang.String otherExpense, @org.jetbrains.annotations.Nullable()
    com.saudi.salarycalculator.core.model.NetSalaryResult netSalaryResult, @org.jetbrains.annotations.Nullable()
    com.saudi.salarycalculator.core.model.EndOfServiceResult endOfServiceResult, @org.jetbrains.annotations.Nullable()
    com.saudi.salarycalculator.core.model.OfferComparisonResult offerComparisonResult, @org.jetbrains.annotations.Nullable()
    com.saudi.salarycalculator.core.model.SavingsResult savingsResult, @org.jetbrains.annotations.Nullable()
    java.lang.String calculationAck, @org.jetbrains.annotations.NotNull()
    java.lang.String reportPreview, @org.jetbrains.annotations.NotNull()
    java.util.List<com.saudi.salarycalculator.core.model.CalculationRecord> history) {
        super();
    }
    
    public final int getSelectedTab() {
        return 0;
    }
    
    public final boolean getShowYearly() {
        return false;
    }
    
    public final boolean getDarkMode() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLanguage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.model.EmployeeType getEmployeeType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBasicSalary() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHousingAllowance() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTransportAllowance() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFoodAllowance() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMobileAllowance() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOtherAllowances() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDeductions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSaudiEmployeeGosiRate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSaudiEmployerGosiRate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getExpatEmployeeGosiRate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getExpatEmployerHazardRate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEosLastSalary() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEosYears() {
        return null;
    }
    
    public final boolean getEosResigned() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferATitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferABasic() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferAHousing() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferATransport() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferAFood() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferAMobile() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferAOther() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferADeduction() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferBTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferBBasic() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferBHousing() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferBTransport() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferBFood() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferBMobile() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferBOther() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOfferBDeduction() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSavingsNetSalary() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRentExpense() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFoodExpense() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTransportExpense() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFamilyExpense() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOtherExpense() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.saudi.salarycalculator.core.model.NetSalaryResult getNetSalaryResult() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.saudi.salarycalculator.core.model.EndOfServiceResult getEndOfServiceResult() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.saudi.salarycalculator.core.model.OfferComparisonResult getOfferComparisonResult() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.saudi.salarycalculator.core.model.SavingsResult getSavingsResult() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCalculationAck() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getReportPreview() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.saudi.salarycalculator.core.model.CalculationRecord> getHistory() {
        return null;
    }
    
    public CalculatorUiState() {
        super();
    }
    
    public final int component1() {
        return 0;
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
    
    public final boolean component19() {
        return false;
    }
    
    public final boolean component2() {
        return false;
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
    
    public final boolean component3() {
        return false;
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
    
    @org.jetbrains.annotations.Nullable()
    public final com.saudi.salarycalculator.core.model.NetSalaryResult component42() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.saudi.salarycalculator.core.model.EndOfServiceResult component43() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.saudi.salarycalculator.core.model.OfferComparisonResult component44() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.saudi.salarycalculator.core.model.SavingsResult component45() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component46() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component47() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.saudi.salarycalculator.core.model.CalculationRecord> component48() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.saudi.salarycalculator.core.model.EmployeeType component5() {
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
    public final com.saudi.salarycalculator.feature.calculator.CalculatorUiState copy(int selectedTab, boolean showYearly, boolean darkMode, @org.jetbrains.annotations.NotNull()
    java.lang.String language, @org.jetbrains.annotations.NotNull()
    com.saudi.salarycalculator.core.model.EmployeeType employeeType, @org.jetbrains.annotations.NotNull()
    java.lang.String basicSalary, @org.jetbrains.annotations.NotNull()
    java.lang.String housingAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String transportAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String foodAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String mobileAllowance, @org.jetbrains.annotations.NotNull()
    java.lang.String otherAllowances, @org.jetbrains.annotations.NotNull()
    java.lang.String deductions, @org.jetbrains.annotations.NotNull()
    java.lang.String saudiEmployeeGosiRate, @org.jetbrains.annotations.NotNull()
    java.lang.String saudiEmployerGosiRate, @org.jetbrains.annotations.NotNull()
    java.lang.String expatEmployeeGosiRate, @org.jetbrains.annotations.NotNull()
    java.lang.String expatEmployerHazardRate, @org.jetbrains.annotations.NotNull()
    java.lang.String eosLastSalary, @org.jetbrains.annotations.NotNull()
    java.lang.String eosYears, boolean eosResigned, @org.jetbrains.annotations.NotNull()
    java.lang.String offerATitle, @org.jetbrains.annotations.NotNull()
    java.lang.String offerABasic, @org.jetbrains.annotations.NotNull()
    java.lang.String offerAHousing, @org.jetbrains.annotations.NotNull()
    java.lang.String offerATransport, @org.jetbrains.annotations.NotNull()
    java.lang.String offerAFood, @org.jetbrains.annotations.NotNull()
    java.lang.String offerAMobile, @org.jetbrains.annotations.NotNull()
    java.lang.String offerAOther, @org.jetbrains.annotations.NotNull()
    java.lang.String offerADeduction, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBTitle, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBBasic, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBHousing, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBTransport, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBFood, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBMobile, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBOther, @org.jetbrains.annotations.NotNull()
    java.lang.String offerBDeduction, @org.jetbrains.annotations.NotNull()
    java.lang.String savingsNetSalary, @org.jetbrains.annotations.NotNull()
    java.lang.String rentExpense, @org.jetbrains.annotations.NotNull()
    java.lang.String foodExpense, @org.jetbrains.annotations.NotNull()
    java.lang.String transportExpense, @org.jetbrains.annotations.NotNull()
    java.lang.String familyExpense, @org.jetbrains.annotations.NotNull()
    java.lang.String otherExpense, @org.jetbrains.annotations.Nullable()
    com.saudi.salarycalculator.core.model.NetSalaryResult netSalaryResult, @org.jetbrains.annotations.Nullable()
    com.saudi.salarycalculator.core.model.EndOfServiceResult endOfServiceResult, @org.jetbrains.annotations.Nullable()
    com.saudi.salarycalculator.core.model.OfferComparisonResult offerComparisonResult, @org.jetbrains.annotations.Nullable()
    com.saudi.salarycalculator.core.model.SavingsResult savingsResult, @org.jetbrains.annotations.Nullable()
    java.lang.String calculationAck, @org.jetbrains.annotations.NotNull()
    java.lang.String reportPreview, @org.jetbrains.annotations.NotNull()
    java.util.List<com.saudi.salarycalculator.core.model.CalculationRecord> history) {
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