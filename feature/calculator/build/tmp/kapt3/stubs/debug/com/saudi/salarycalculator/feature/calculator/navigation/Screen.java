package com.saudi.salarycalculator.feature.calculator.navigation;

/**
 * All top-level destinations in the app. [Splash] and [Payslip] are not part of the bottom
 * navigation bar; the other five map 1:1 to [bottomNavScreens] in display order.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u0000 \b2\u00020\u0001:\b\u0007\b\t\n\u000b\f\r\u000eB\u000f\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u0082\u0001\u0007\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u00a8\u0006\u0016"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen;", "", "route", "", "(Ljava/lang/String;)V", "getRoute", "()Ljava/lang/String;", "Calculator", "Companion", "Comparison", "Home", "Payslip", "Result", "Settings", "Splash", "Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen$Calculator;", "Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen$Comparison;", "Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen$Home;", "Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen$Payslip;", "Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen$Result;", "Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen$Settings;", "Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen$Splash;", "calculator_debug"})
public abstract class Screen {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String route = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.saudi.salarycalculator.feature.calculator.navigation.Screen> bottomNavScreens = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.saudi.salarycalculator.feature.calculator.navigation.Screen.Companion Companion = null;
    
    private Screen(java.lang.String route) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRoute() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen$Calculator;", "Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen;", "()V", "calculator_debug"})
    public static final class Calculator extends com.saudi.salarycalculator.feature.calculator.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.saudi.salarycalculator.feature.calculator.navigation.Screen.Calculator INSTANCE = null;
        
        private Calculator() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\b\u001a\u00020\u00052\b\u0010\t\u001a\u0004\u0018\u00010\nR\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u000b"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen$Companion;", "", "()V", "bottomNavScreens", "", "Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen;", "getBottomNavScreens", "()Ljava/util/List;", "fromRoute", "route", "", "calculator_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.saudi.salarycalculator.feature.calculator.navigation.Screen> getBottomNavScreens() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.saudi.salarycalculator.feature.calculator.navigation.Screen fromRoute(@org.jetbrains.annotations.Nullable()
        java.lang.String route) {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen$Comparison;", "Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen;", "()V", "calculator_debug"})
    public static final class Comparison extends com.saudi.salarycalculator.feature.calculator.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.saudi.salarycalculator.feature.calculator.navigation.Screen.Comparison INSTANCE = null;
        
        private Comparison() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen$Home;", "Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen;", "()V", "calculator_debug"})
    public static final class Home extends com.saudi.salarycalculator.feature.calculator.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.saudi.salarycalculator.feature.calculator.navigation.Screen.Home INSTANCE = null;
        
        private Home() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen$Payslip;", "Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen;", "()V", "calculator_debug"})
    public static final class Payslip extends com.saudi.salarycalculator.feature.calculator.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.saudi.salarycalculator.feature.calculator.navigation.Screen.Payslip INSTANCE = null;
        
        private Payslip() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen$Result;", "Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen;", "()V", "calculator_debug"})
    public static final class Result extends com.saudi.salarycalculator.feature.calculator.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.saudi.salarycalculator.feature.calculator.navigation.Screen.Result INSTANCE = null;
        
        private Result() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen$Settings;", "Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen;", "()V", "calculator_debug"})
    public static final class Settings extends com.saudi.salarycalculator.feature.calculator.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.saudi.salarycalculator.feature.calculator.navigation.Screen.Settings INSTANCE = null;
        
        private Settings() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen$Splash;", "Lcom/saudi/salarycalculator/feature/calculator/navigation/Screen;", "()V", "calculator_debug"})
    public static final class Splash extends com.saudi.salarycalculator.feature.calculator.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.saudi.salarycalculator.feature.calculator.navigation.Screen.Splash INSTANCE = null;
        
        private Splash() {
        }
    }
}