# Saudi Salary Calculator

An Android app for calculating net salary, comparing job offers, and generating payslips for employees in Saudi Arabia. Handles GOSI contributions (Saudi and non-Saudi rates), allowances, bonuses, deductions, overtime, and estimated end-of-service benefits (EOSB). Fully bilingual (English/Arabic, with RTL support) and supports light/dark themes.

## Features

- **Net salary calculator** — a guided, multi-step wizard covering basic salary, housing/transport/other allowances, bonuses, commission, overtime, loan/absence/unpaid-leave deductions, and GOSI.
- **Offer comparison** — compare two job offers side by side (net salary, GOSI, EOSB, percentage difference).
- **Payslip** — a formatted payslip view generated from the last calculation, with PDF export and share.
- **Calculation history** — past calculations are saved locally; saved net-salary calculations can be reopened and edited, and any record can be deleted.
- **Settings** — language (English/Arabic) and theme (light/dark), applied app-wide.

## Tech stack

- Kotlin + [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- [Hilt](https://dagger.dev/hilt/) for dependency injection
- [Room](https://developer.android.com/training/data-storage/room) for local persistence
- Multi-module Gradle architecture with strict one-way dependencies (feature → core, never the reverse)

## Module structure

| Module | Responsibility |
|---|---|
| `app` | Application shell, navigation host, DI wiring, manifest, app icon/splash |
| `core:model` | Plain domain models shared across the app |
| `core:calculator` | Pure salary/GOSI/EOSB calculation logic |
| `core:database` | Room entities and DAOs |
| `core:data` | Repositories (offline-first, backed by `core:database`) |
| `core:preferences` | User settings (theme, language) |
| `core:designsystem` | Shared Compose UI components, theme, colors, icons (e.g. the Riyal symbol) |
| `feature:calculator` | All screens — wizard, result, payslip, comparison, history, settings — and their view models |

## Requirements

- Android Studio (latest stable)
- JDK 17
- Android SDK with `compileSdk`/`targetSdk` 35 installed (`minSdk` 24)

## Building and running

```bash
git clone <repo-url>
cd android
```

Open the project in Android Studio and let Gradle sync, or build from the command line:

```bash
./gradlew installDebug   # build + install the debug build on a connected device/emulator
./gradlew test           # run unit tests
```

## Release builds

See [`PLAY_STORE_RELEASE.md`](./PLAY_STORE_RELEASE.md) for signing setup and the steps to produce a Play Store–ready release bundle.
