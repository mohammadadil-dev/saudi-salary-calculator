package com.saudi.salarycalculator.core.data.repository;

import com.saudi.salarycalculator.core.calculator.EndOfServiceCalculatorService;
import com.saudi.salarycalculator.core.calculator.GosiCalculatorService;
import com.saudi.salarycalculator.core.calculator.NetSalaryCalculatorService;
import com.saudi.salarycalculator.core.calculator.OfferComparisonCalculatorService;
import com.saudi.salarycalculator.core.calculator.OvertimeCalculatorService;
import com.saudi.salarycalculator.core.calculator.SavingsCalculatorService;
import com.saudi.salarycalculator.core.data.SalaryRepository;
import com.saudi.salarycalculator.core.database.dao.CalculationRecordDao;
import com.saudi.salarycalculator.core.database.entity.CalculationRecordEntity;
import com.saudi.salarycalculator.core.model.CalculationRecord;
import com.saudi.salarycalculator.core.model.CalculationType;
import com.saudi.salarycalculator.core.model.EndOfServiceInput;
import com.saudi.salarycalculator.core.model.EndOfServiceResult;
import com.saudi.salarycalculator.core.model.GosiInput;
import com.saudi.salarycalculator.core.model.GosiResult;
import com.saudi.salarycalculator.core.model.NetSalaryInput;
import com.saudi.salarycalculator.core.model.NetSalaryResult;
import com.saudi.salarycalculator.core.model.OfferComparisonResult;
import com.saudi.salarycalculator.core.model.OfferInput;
import com.saudi.salarycalculator.core.model.OvertimeInput;
import com.saudi.salarycalculator.core.model.OvertimeResult;
import com.saudi.salarycalculator.core.model.SavingsInput;
import com.saudi.salarycalculator.core.model.SavingsResult;
import com.saudi.salarycalculator.core.preferences.UserPreferencesStore;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\f\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0002\u001a\f\u0010\u0003\u001a\u00020\u0002*\u00020\u0001H\u0002\u00a8\u0006\u0004"}, d2 = {"toEntity", "Lcom/saudi/salarycalculator/core/database/entity/CalculationRecordEntity;", "Lcom/saudi/salarycalculator/core/model/CalculationRecord;", "toModel", "data_debug"})
public final class OfflineFirstSalaryRepositoryKt {
    
    private static final com.saudi.salarycalculator.core.model.CalculationRecord toModel(com.saudi.salarycalculator.core.database.entity.CalculationRecordEntity $this$toModel) {
        return null;
    }
    
    private static final com.saudi.salarycalculator.core.database.entity.CalculationRecordEntity toEntity(com.saudi.salarycalculator.core.model.CalculationRecord $this$toEntity) {
        return null;
    }
}