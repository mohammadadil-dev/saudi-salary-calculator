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
import com.saudi.salarycalculator.core.model.ContractType;
import com.saudi.salarycalculator.core.model.EmployeeType;
import com.saudi.salarycalculator.core.model.EmploymentSector;
import com.saudi.salarycalculator.core.model.EndOfServiceInput;
import com.saudi.salarycalculator.core.model.EndOfServiceResult;
import com.saudi.salarycalculator.core.model.GosiInput;
import com.saudi.salarycalculator.core.model.GosiRates;
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

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000$\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u000e\u0010\u0005\u001a\u0004\u0018\u00010\u0006*\u00020\u0001H\u0002\u001a\f\u0010\u0007\u001a\u00020\u0001*\u00020\u0006H\u0002\u001a\f\u0010\b\u001a\u00020\t*\u00020\nH\u0002\u001a\f\u0010\u000b\u001a\u00020\n*\u00020\tH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"SNAPSHOT_DELIMITER", "", "SNAPSHOT_FIELD_COUNT", "", "SNAPSHOT_NULL", "decodeNetSalaryInputSnapshot", "Lcom/saudi/salarycalculator/core/model/NetSalaryInput;", "encodeAsSnapshot", "toEntity", "Lcom/saudi/salarycalculator/core/database/entity/CalculationRecordEntity;", "Lcom/saudi/salarycalculator/core/model/CalculationRecord;", "toModel", "data_debug"})
public final class OfflineFirstSalaryRepositoryKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String SNAPSHOT_DELIMITER = "<<#>>";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String SNAPSHOT_NULL = "<<NULL>>";
    private static final int SNAPSHOT_FIELD_COUNT = 27;
    
    private static final com.saudi.salarycalculator.core.model.CalculationRecord toModel(com.saudi.salarycalculator.core.database.entity.CalculationRecordEntity $this$toModel) {
        return null;
    }
    
    private static final com.saudi.salarycalculator.core.database.entity.CalculationRecordEntity toEntity(com.saudi.salarycalculator.core.model.CalculationRecord $this$toEntity) {
        return null;
    }
    
    private static final java.lang.String encodeAsSnapshot(com.saudi.salarycalculator.core.model.NetSalaryInput $this$encodeAsSnapshot) {
        return null;
    }
    
    private static final com.saudi.salarycalculator.core.model.NetSalaryInput decodeNetSalaryInputSnapshot(java.lang.String $this$decodeNetSalaryInputSnapshot) {
        return null;
    }
}