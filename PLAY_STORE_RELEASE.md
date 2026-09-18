# Play Store Release Guide

Everything needed to take this app from source to a signed, submittable Play Store release.
Read the whole **Before you publish** checklist at the bottom before uploading anything.

## 1. Signing

A release keystore has already been generated:

- Keystore file: `release-keystore.jks` (project root)
- Key alias: `key0`
- Credentials: `keystore.properties` (project root)

Both files are listed in `.gitignore` and must **never** be committed. `app/build.gradle.kts`
reads them automatically — if `keystore.properties` is missing, the release build type just
builds unsigned (so a fresh clone without the file still compiles).

**Back this up now, somewhere other than this machine** (password manager, encrypted cloud
storage) — both `release-keystore.jks` and `keystore.properties`. If you lose the keystore, Google
does not have a recovery path: you would never be able to publish an update to this app listing
again, only a brand-new listing under a new package name. The passwords were also shared in chat
when they were generated — save them now if you haven't.

**Incident, 2026-06-29 — keystore mismatch:** a second `release-keystore.jks` had been generated
(alias `saudisalarycalculator`) at some point after the app's first-ever upload, silently
replacing the original file. Play Console rejected a re-upload with "Your Android App Bundle is
signed with the wrong key," since it had already permanently registered the certificate from that
very first upload (alias `key0`, fingerprint
`9F:9A:90:0A:51:D8:83:67:18:4F:CB:0E:84:DC:0F:B2:7D:C8:F4:C4`). Recovered by locating the original
file — saved separately during the initial "Generate Signed Bundle" wizard run, at
`~/Documents/android-release-key/saudi-salary-calculator` — and restoring it as
`release-keystore.jks`, with `keystore.properties`'s `keyAlias` updated to `key0` to match. The
incorrect keystore is kept as `release-keystore-WRONG-do-not-use.jks` for reference only (still
covered by `.gitignore`'s `*.jks` wildcard) — never use it.

**Never run `keytool -genkeypair` again for this app, for any reason.** Regenerating
`release-keystore.jks` under the same filename/alias — even by accident — creates an entirely new,
incompatible key and silently breaks every future upload, exactly what happened here. If the file
is ever genuinely missing, the only recovery paths are finding the original backup or Play
Console's Setup → App integrity → "Request upload key reset."

To verify the keystore at any point:

```bash
keytool -list -v -keystore release-keystore.jks
```

## 2. AdMob — done

Real IDs are wired into `app/build.gradle.kts`'s `release` block as of 2026-06-28 (App ID
`ca-app-pub-8890346685665889~3172188319`, banner unit `ca-app-pub-8890346685665889/1725199104`).
Debug builds intentionally keep Google's sample IDs (`defaultConfig`) — that's correct and matches
Google's own guidance; only release ships the real ones.

Note: a freshly created ad unit can take up to an hour to start serving real ads — if the banner
looks blank right after a release build, that's expected, not a bug.

## 3. Versioning

**Current: `versionCode = 7`, `versionName = "1.2.0"`** — a real feature release bumped from
6/"1.1.2". New tools: a home-screen payday countdown widget, an EOSB accrual tracker, a leave
balance tracker, an offer red-flag checker, a reverse salary calculator ("what salary do I need to
hit X take-home"), and a city cost-of-living estimator. Also new: in-app review prompts (Google
Play's In-App Review API) with a sentiment-gate check in front of it — an "Enjoying the app?"
Yes/No shown before the native review dialog, so an unhappy moment gets routed to a quiet
thank-you instead of a public bad rating; plus a loyalty-based trigger so calculator-only users
who never export/compare still get a fair shot at being asked — and explicit "Rate this app" /
"Share this app" rows in Settings.

Alongside the new tools, a large batch of correctness and clarity fixes accumulated across two
review passes (a QA pass and a UX/UI pass) before this release:

- EOSB comparison on the Comparison screen always showed a 0 difference regardless of the two
  offers' actual years of service — `OfferInput.yearsOfService` was never populated by any UI, so
  it silently defaulted to 0 for both sides. Fixed by comparing at a fixed 5-year reference point.
- The Result screen kept showing a stale net-salary figure after resetting or editing the wizard,
  because `netSalaryResult` wasn't cleared alongside the wizard state.
- Reverse Salary displayed its internal binary-search ceiling as if it were a real recommended
  basic salary whenever a target was mathematically unreachable — now hidden behind an
  `isAchievable` check with an explanatory "not realistic" message instead.
- `otherDeductions` was a wired-up input field that silently never reached the actual calculation
  — fixed end-to-end (wizard input, review step, payslip line item, calculator input).
- The RTL arrow icon on the primary CTA button used a non-mirroring icon asset and an animation
  offset that wasn't direction-aware, so the "nudge" animation visually pointed the wrong way in
  Arabic. Fixed with `Icons.AutoMirrored` and a layout-direction-aware offset sign.
- Bottom navigation icons had a redundant `contentDescription` that duplicated the adjacent visible
  label in the same merged semantics node, so TalkBack announced every tab name twice.
- The +/- stepper control (used on 9 fields across 5 screens) went through several rounds of
  fixes: missing screen-reader labels on the +/- buttons, a sub-minimum 36dp touch target, a
  disabled-state color that looked like a different button than the enabled state, zero spacing
  between the buttons and the number, and finally a size/prominence pass (48dp → 32dp visible
  circle via `minimumInteractiveComponentSize()`, so the tap target stays accessible while the
  button reads smaller and cleaner).
- Added short, low-emphasis explanatory captions anywhere a number could plausibly be
  misread — the employer GOSI contribution row, the EOSB helper text, the Comparison screen's
  color legend, the Leave Balance Tracker's balance/accrued/entitlement relationship, and the
  Reverse Salary allowance-split note (which used to imply, confusingly, that the 25%/10% split
  only applied to Saudi nationals rather than the whole Saudi job market regardless of the
  Saudi/Non-Saudi toggle).
- Renamed "Scan Offer" / "Offer red-flag scanner" to "Check Offer" / "Offer red-flag checker" (EN
  only — the Arabic copy was already correctly worded) and added an explicit "this isn't a
  document scanner" line, since "scan" was read as camera/OCR document scanning rather than the
  manual checklist it actually is.
- Renamed "Additional adults" to "Family member" on the Cost of Living screen, with a caption
  clarifying it means adult family members specifically (not the user, not children — children
  keep their own separate counter so the school-fee estimate still works).

---

**Previous — `versionCode = 6`, `versionName = "1.1.2"` (2026-09-05)** — a stability + compliance
release bumped from 5/"1.1.1". Two changes, no new features:

1. **Splash crash fix.** `SplashScreenViewProvider.iconView` is implemented as
   `platformView.iconView!!` in core-splashscreen 1.0.1 on API 31+, so on devices whose platform
   `SplashScreenView` carries no icon the *getter itself* throws NPE — the existing `if (icon ==
   null)` fallback in `MainActivity` was unreachable. Now read via
   `runCatching { provider.iconView }.getOrNull()`. This was 98.5% of all crash events on 1.1.1
   (43 affected users), a hard launch-path crash: affected users could not open the app at all.
   First seen on Infinix SMART 9 HD / Android 14.
2. **`compileSdk`/`targetSdk` 35 → 36** (see Section 4 — the deadline passed).

AGP was also pinned back from 8.13.2 to **8.10.0**: 8.13.2 exceeds what the installed Android
Studio supports ("Latest supported version is AGP 8.10.0"), while 8.10.0 still supports
`compileSdk 36`. Gradle wrapper stays at 8.13 (AGP 8.10 requires >= 8.11.1).

Previous release — `versionCode = 4`, `versionName = "1.1.0"` as of 2026-07-14, bumped from
3/"1.0.1" for a real feature release: phased GOSI rate schedule (New System now tracks the actual
2024-2028 step-up, corrected against Oracle/ZenHR payroll-legislation sources), Ramadan reduced
working hours, expat residency cost estimator, city cost-of-living reference, unofficial payroll
CSV export, watermarked salary certificate PDF export, rate/share options in Settings, plus a
handful of UI fixes (a date picker that used to require a separate Confirm tap, a segmented
control that could visually overlap on long labels, an unresolved icon reference caught by a real
build). Also new: wizard-level input validation — the "Next" button on the Basic Salary step and
the Calculate button on Review are disabled with an explanatory message until basic salary is
entered, and the Review step now surfaces non-blocking warnings for likely data-entry mistakes
(deductions exceeding gross salary, a joining date after the calculation month, implausibly high
overtime hours). **Before re-uploading**, verify on a real device via `./gradlew clean installRelease` (or
Android Studio's Generate Signed Bundle/APK) — don't re-upload on faith, a buggy upload burns a
review cycle. For every future release: bump `versionCode` by at least 1 (it must strictly
increase, Play Console rejects re-using or lowering it) and update `versionName` to
whatever you want users to see.

## 4. Target API level — DONE (2026-09-05)

`compileSdk`/`targetSdk` are now **36** (Android 16).

Play Console flagged this on the live build: *"Your highest non-compliant target API level is
Android 15 (API level 35)"* — that is `versionCode 5`, which shipped targetSdk 35. Publishing
`versionCode 6` clears it. Hard deadline for continuing to push updates: **Nov 1, 2026**.

If the warning persists in Play Console after this upload, check for stale targetSdk-35 artifacts
still active on the internal / closed / open testing tracks — Play counts every active artifact
across all tracks, not just production.

**Untested risk from this bump — verify before uploading.** Android 16 *enforces* edge-to-edge for
apps targeting API 36; `windowOptOutEdgeToEdgeEnforcement` is ignored. This codebase contains no
insets handling at all — no `enableEdgeToEdge()`, no `WindowCompat`, no `statusBarsPadding`. The
`SalaryNavGraph` Scaffold does apply its `innerPadding` and M3 Scaffold defaults to system-bar
insets, so it will most likely render correctly, but that is an inference, not a test. Run on an
**Android 16 emulator** and check: content under the status bar, clipped bottom nav/ad banner,
and status-bar icon contrast in dark theme. Play will not catch this; users will.

## 5. Building the release bundle

```bash
./gradlew bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab` — this `.aab` is what you upload to
Play Console (App bundles, not the `.apk`, are required for new apps).

**Upload the file from `app/build/outputs/bundle/release/`, not `app/release/`.** Android Studio's
"Generate Signed Bundle / APK" wizard writes its output to `app/release/` instead, so a stale
bundle from an earlier wizard run sits there indefinitely and does *not* get refreshed by
`./gradlew bundleRelease`. Uploading it fails with "Version code N has already been used" — which
looks like a versioning mistake but is actually the wrong file. This happened on the 1.1.2 upload
(2026-09-05): a July 14 bundle at `app/release/app-release.aab` still carried versionCode 5. It has
been renamed `app-release-v1.1.1-OLD-do-not-upload.aab`.

Quick check on any bundle before uploading:

```bash
unzip -p <path>.aab base/manifest/AndroidManifest.xml | strings | grep -oE '1\.[0-9]+\.[0-9]+' | head -1
```

To sanity-check the signed build installs and runs before uploading:

```bash
./gradlew assembleRelease
adb install app/build/outputs/apk/release/app-release.apk
```

## 6. Store listing copy

**App name:** Saudi Salary Calculator

**Short description** (80 char max):
> Calculate net salary, GOSI & EOSB for jobs in Saudi Arabia — fast, bilingual.

**Full description:**

```
Know your real take-home pay before you accept a job offer in Saudi Arabia.

Saudi Salary Calculator walks you through a quick wizard — basic salary, housing/transport/other
allowances, bonuses, commission, overtime, and any deductions — and calculates your accurate net
salary, including GOSI contributions (Saudi and non-Saudi rates) and an estimated end-of-service
benefit (EOSB).

FEATURES
• Guided net salary calculator with GOSI (Existing and New System) and EOSB built in
• Compare two job offers side by side — net salary, GOSI, EOSB, percentage difference
• Reverse salary calculator — find the basic salary you need to hit a target take-home pay
• Offer red-flag checker — screens offer terms against Saudi Labor Law norms and common red flags
• EOSB accrual tracker and leave balance tracker — live running totals for your current job
• Home-screen widget — payday countdown at a glance
• Generate a formatted payslip, export it as a PDF, and share it
• Export a watermarked salary certificate (PDF) — a personal, self-generated record
• Export your saved calculations as a payroll summary (CSV)
• Expat residency cost estimator — dependent fees, exit/re-entry visas, insurance
• Cost-of-living estimator — rent, household, and school-fee ranges by Saudi city
• Ramadan reduced working-hours support
• Save your calculation history — reopen, edit, or delete any past calculation
• Fully bilingual: English and Arabic, with right-to-left layout support
• Light and dark themes

Whether you're evaluating a new offer, negotiating a raise, or just want to understand your
payslip, Saudi Salary Calculator gives you the numbers in seconds — no spreadsheet required.

SOURCES
GOSI contribution rates: https://www.gosi.gov.sa
End-of-service benefit rules (Saudi Labor Law): https://www.hrsd.gov.sa/en/knowledge-centre/articles/317

DISCLAIMER
This app is an independent, unofficial calculator. It is not affiliated with, endorsed by, or
operated by GOSI, the Ministry of Human Resources and Social Development, or any Saudi
government entity. All results are estimates for informational purposes only — always confirm
exact figures with your employer or the official sources above.
```

### Arabic (`ar`) store listing

Play Console lets you add a full translated listing per locale (Store presence → Main store
listing → Manage translations → add Arabic). Adding one is worth doing here specifically — the
app itself is fully bilingual with RTL support, and most of the target audience for a Saudi
payroll calculator searches Play Store in Arabic. App name matches the in-app Arabic name
(`app_name` / `splash_app_name` in `values-ar/strings.xml`) for consistency.

**اسم التطبيق:** حاسبة الراتب السعودية

**الوصف المختصر** (80 حرفًا كحد أقصى، 77 حرفًا):
> احسب صافي راتبك والتأمينات الاجتماعية ومكافأة نهاية الخدمة في السعودية بسرعة.

**الوصف الكامل:**

```
اعرف صافي راتبك الحقيقي قبل قبول أي عرض عمل في المملكة العربية السعودية.

يرشدك تطبيق حاسبة الراتب السعودية عبر خطوات بسيطة — الراتب الأساسي، بدلات السكن والنقل وغيرها،
المكافآت، العمولات، الوقت الإضافي، وأي استقطاعات — ليحسب لك صافي راتبك بدقة، متضمنًا اشتراكات
التأمينات الاجتماعية (جوسي) للسعوديين وغير السعوديين، وتقدير مكافأة نهاية الخدمة.

المميزات
• حاسبة صافي راتب موجّهة تتضمن التأمينات الاجتماعية (النظامين الحالي والجديد) ومكافأة نهاية الخدمة
• قارن بين عرضي عمل جنبًا إلى جنب — صافي الراتب، التأمينات، مكافأة نهاية الخدمة، ونسبة الفرق
• حاسبة الراتب العكسي — اعرف الراتب الأساسي المطلوب للوصول إلى صافي راتب مستهدف
• فاحص عروض العمل — يفحص بنود العرض مقابل أنظمة العمل السعودي وأبرز العلامات التحذيرية الشائعة
• متتبع مكافأة نهاية الخدمة ومتتبع رصيد الإجازات — أرصدة مباشرة مرتبطة بوظيفتك الحالية
• أداة الشاشة الرئيسية — عداد تنازلي ليوم الراتب
• أنشئ قسيمة راتب منسقة، صدّرها كملف PDF، وشاركها
• صدّر شهادة راتب بعلامة مائية (PDF) — سجل شخصي ذاتي الإنشاء
• صدّر حساباتك المحفوظة كملخص رواتب (CSV)
• حاسبة تكاليف الإقامة للمقيمين — رسوم التابعين، تأشيرات الخروج والعودة، التأمين الصحي
• حاسبة تكلفة المعيشة — نطاقات الإيجار وتكاليف المنزل والرسوم الدراسية حسب المدينة السعودية
• دعم ساعات العمل المخفضة في رمضان
• احفظ سجل حساباتك — أعد فتحه أو عدّله أو احذفه في أي وقت
• دعم كامل للغتين العربية والإنجليزية، مع دعم الاتجاه من اليمين لليسار
• الوضعان الفاتح والداكن

سواء كنت تقيّم عرض عمل جديد، تتفاوض على زيادة راتب، أو ببساطة تريد فهم قسيمة راتبك، يمنحك تطبيق
حاسبة الراتب السعودية الأرقام في ثوانٍ — دون الحاجة لأي جدول بيانات.

المصادر
نسب التأمينات الاجتماعية (جوسي): https://www.gosi.gov.sa
قواعد مكافأة نهاية الخدمة (نظام العمل السعودي): https://www.hrsd.gov.sa/en/knowledge-centre/articles/317

إخلاء مسؤولية
هذا التطبيق حاسبة مستقلة وغير رسمية، وهو غير تابع لمؤسسة التأمينات الاجتماعية (جوسي) أو وزارة
الموارد البشرية والتنمية الاجتماعية أو أي جهة حكومية سعودية، ولا يحظى بدعم أو تشغيل من أي منها.
جميع النتائج تقديرية ولأغراض إعلامية فقط — يُرجى دائمًا التحقق من الأرقام الدقيقة مع صاحب العمل أو
المصادر الرسمية أعلاه.
```

**ما الجديد (v1.1.0):** — see the Arabic release-notes blockquote just below the English one
directly under this section; same copy applies here.

**What's new (v1.2.0):**
> New: home-screen payday widget, EOSB accrual tracker, leave balance tracker, offer red-flag
> checker, reverse salary calculator, and a cost-of-living estimator. Plus a big pass of clarity
> and accuracy fixes across GOSI/EOSB comparisons, the salary wizard, and every calculator screen,
> and better accessibility (larger tap targets, screen-reader labels) throughout.

(363 characters — under Play Console's ~500-char release-notes limit.)

> جديد: أداة راتب الشاشة الرئيسية (تعد أيام الراتب)، متتبع مكافأة نهاية الخدمة، متتبع رصيد
> الإجازات، فاحص عروض العمل، حاسبة الراتب العكسي، وحاسبة تكلفة المعيشة. بالإضافة إلى مجموعة كبيرة
> من إصلاحات الدقة والوضوح في مقارنات التأمينات الاجتماعية ومكافأة نهاية الخدمة، ومعالج الراتب،
> وجميع شاشات الحاسبة، وتحسينات في إمكانية الوصول (أهداف لمس أكبر، تسميات لقارئ الشاشة).

**What's new (v1.1.2):**
> Bug fix: resolved a crash on launch that prevented some devices from opening the app. Updated
> for Android 16.

> إصلاح خلل: تم حل مشكلة توقف التطبيق عند بدء التشغيل والتي كانت تمنع فتحه على بعض الأجهزة. وتحديث التطبيق ليتوافق مع نظام Android 16.

**What's new (v1.1.0):**
> New: expat residency cost estimator, city cost-of-living reference, Ramadan reduced-hours
> support, payroll summary export (CSV), and watermarked salary certificate export (PDF).
> Updated GOSI New System contribution rates through 2028. Smarter validation now catches
> likely data-entry mistakes (like deductions bigger than salary) before you calculate. Plus a
> smoother date picker and other UI fixes.

(279 characters — well under Play Console's ~500-char release-notes limit. Arabic translation
below if you want to fill in the `ar` locale release notes in Play Console; the store listing
itself is currently English-only, see Section 6.)

> جديد: حاسبة تكاليف الإقامة للمقيمين، مرجع تكلفة المعيشة حسب المدينة، دعم ساعات العمل المخفضة في
> رمضان، تصدير ملخص الرواتب (CSV)، وتصدير شهادة الراتب بعلامة مائية (PDF). تحديث نسب التأمينات
> الاجتماعية (جوسي) للنظام الجديد حتى عام 2028. تحقق أذكى يكتشف الآن أخطاء الإدخال المحتملة (مثل
> استقطاعات أكبر من الراتب) قبل الحساب. بالإضافة إلى منتقي تاريخ أكثر سلاسة وتحسينات أخرى في الواجهة.

(Previous "what's new" entries, for reference:
v1.0.1 — Bug fix: resolved a crash on launch affecting some devices.
v1.0 — First release: net salary calculator, offer comparison, payslip with PDF export,
calculation history, English/Arabic support, light/dark themes.)

**Category:** Finance (or Tools — Finance is the closer fit for a salary/GOSI calculator).

**Rejected 2026-06-28, fixed same day:** Play Console rejected the first submission for
"Violation of Misleading Claims policy — Missing Source Link for Government Information." The
app references GOSI contribution rates and EOSB, both governed by Saudi government
bodies/legislation, and the original description neither sourced that info nor disclaimed
non-affiliation. Fixed by adding the SOURCES and DISCLAIMER blocks above (official GOSI and
MHRSD links, explicit non-affiliation statement). After pasting the updated description into
Play Console, resubmit via Publishing overview → "Send changes for review."

## 7. Graphics

Generated and placed in `play_store_assets/` (brand gradient + the app's Riyal-glyph mark, same
visual language as the app icon and splash screen):

- `play_store_icon_512.png` — 512×512 hi-res icon (Play Console → Store listing → App icon)
- `feature_graphic_1024x500.png` — 1024×500 feature graphic (Play Console → Store listing → Feature graphic)

**Still needed, and not something that can be produced without a device/emulator:** phone
screenshots (Play Console requires at least 2, recommends 4–8; 16:9 or 9:16, min dimension 320px,
max 3840px). Run the app on a device or emulator and capture the wizard, the result/payslip
screen, and the comparison screen — those three tell the app's story best.

## 8. Privacy policy — done, needs hosting

`index.html` at the repo root is now the real, filled-in privacy policy page (no more
placeholders) — styled, matches the app's brand colors, covers local-only storage, AdMob,
permissions, children's privacy, and contact info.

To publish it:

1. Deploy this repo to Vercel (root `index.html` is picked up automatically — no build config
   needed; framework preset "Other" is fine).
2. Copy the resulting URL (e.g. `https://your-project.vercel.app`).
3. Paste that URL into Play Console → App content → Privacy policy.

### app-ads.txt (AdMob "Verify app" step)

`app-ads.txt` now sits at the repo root next to `index.html`, so the same Vercel deploy serves it
automatically at `https://<your-domain>/app-ads.txt`:

```
google.com, pub-8890346685665889, DIRECT, f08c47fec0942fa0
```

AdMob's "Verify app" flagged "We didn't find a developer website in your app listing on Google
Play" — that's a separate field from the privacy policy URL. Fix:

1. Play Console → your app → Store presence → Store listing → **Contact details** → set
   **Website** to the same deployed domain (e.g. `https://your-project.vercel.app`).
2. Save and publish the store listing change.
3. Back in AdMob → Apps → this app → App ads.txt, click **Check for updates** (crawling can take
   anywhere from a few minutes to ~24 hours after both the site and Play listing are live).

Both the Play Console Website field and the live `app-ads.txt` file need to be in place before
AdMob's crawler will verify — one without the other won't clear the warning.

## 9. Data Safety form (Play Console)

Based on what's actually in the app (local Room database only, no analytics/crash SDK, AdMob for
ads):

- **Data collected:** None collected/shared by the app itself. Under "Data shared with third
  parties," declare AdMob — Play Console has a built-in AdMob preset that auto-fills the typical
  answers (Device or other IDs, collected for Advertising/Marketing, not user-initiated).
- **Data shared:** Advertising ID, shared with Google for ad serving (via AdMob SDK).
- **Security:** Data (the local calculation history) is not encrypted in transit because it never
  leaves the device. No account/login exists, so there's nothing to say about account deletion.
- **Approach:** Go through the AdMob-specific prompts in Play Console's Data Safety section — it
  walks through this exact scenario (local app + AdMob, no other SDKs) and is more reliable than
  guessing at the generic form.

## 10. Content rating questionnaire

Expected answers given the app's actual content (a finance calculator with ads, no
violence/gambling/UGC):

- Violence, sexual content, profanity, controlled substances: None
- Gambling: No real-money gambling or simulated gambling
- User-generated content / user interaction: No (no chat, no sharing between users)
- Ads: Yes, displays third-party ads (AdMob)
- Expected rating: **Everyone** (or equivalent low-end rating in your region's rating board, e.g.
  PEGI 3)

## Before you publish — checklist

- [x] Real AdMob App ID and banner ad unit ID swapped into `app/build.gradle.kts` (`release` block)
- [ ] `release-keystore.jks` + `keystore.properties` backed up off this machine
- [ ] `./gradlew bundleRelease` runs clean and produces a signed `.aab` — **not yet run for this
      release**; everything below was verified statically (brace/paren balance, XML
      well-formedness, EN/AR string parity, every `R.string.*` reference resolves) since no real
      Gradle build is available in this environment. Run a real build before uploading.
- [ ] Privacy policy filled in, hosted, and the URL added to Play Console
- [ ] Phone screenshots captured (min 2) — worth recapturing for this release: the new tools
      (Reverse Salary, Offer Checker, EOSB/Leave trackers, Cost of Living, the home widget) aren't
      represented in any screenshot yet
- [ ] Store listing copy reviewed/edited — FEATURES list in Section 6 updated for 1.2.0's new
      tools, but the rest of the description/short description wasn't rewritten around them; give
      it a read before pasting into Play Console
- [ ] Data Safety form completed in Play Console (no changes expected — the new widget/trackers
      are still local-only, no new data collection or third-party sharing)
- [ ] Content rating questionnaire completed in Play Console
- [x] `targetSdk` bumped to 36 (Section 4) — unchanged since 1.1.2
- [x] `versionCode` bumped to 7 / `versionName` 1.2.0 — Play rejects a reused `versionCode`
- [ ] Upload keystore verified against the certificate Play has registered: alias `key0`,
      SHA1 `9F:9A:90:0A:51:D8:83:67:18:4F:CB:0E:84:DC:0F:B2:7D:C8:F4:C4` — re-verify before
      uploading (last checked 2026-09-05, for versionCode 6)
- [ ] Smoke-tested on an **Android 16** device/emulator for edge-to-edge regressions (Section 4) —
      still open from the 1.1.2 checklist, carried forward
- [ ] New-since-1.1.2 features smoke-tested on a real device: home-screen widget actually renders
      and updates, EOSB/Leave tracker setup flows save and persist correctly, in-app review
      sentiment dialog appears and both Yes/No paths behave as expected — none of this can be
      verified without a device/emulator, which this environment doesn't have
