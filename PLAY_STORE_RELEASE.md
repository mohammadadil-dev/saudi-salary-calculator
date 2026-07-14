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

`versionCode = 4`, `versionName = "1.1.0"` (`app/build.gradle.kts`) as of 2026-07-14 — bumped from
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

## 4. Target API level — heads-up for later in 2026

Play Store requires new apps/updates to target a recent Android API level. As of today
(2026-06-27) `compileSdk`/`targetSdk = 35` (Android 15) is compliant. **From August 31, 2026**,
Google requires new app submissions and updates to target **API 36 (Android 16)**. If you submit
this release before that date you're fine as-is; if you're still iterating past it, bump both
`compileSdk` and `targetSdk` to 36 (and re-test — a major SDK bump can shift Compose/Material3
behavior) before your next upload.

## 5. Building the release bundle

```bash
./gradlew bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab` — this `.aab` is what you upload to
Play Console (App bundles, not the `.apk`, are required for new apps).

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
• Generate a formatted payslip, export it as a PDF, and share it
• Export a watermarked salary certificate (PDF) — a personal, self-generated record
• Export your saved calculations as a payroll summary (CSV)
• Expat residency cost estimator — dependent fees, exit/re-entry visas, insurance
• City cost-of-living reference for comparing offers across Saudi cities
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
• أنشئ قسيمة راتب منسقة، صدّرها كملف PDF، وشاركها
• صدّر شهادة راتب بعلامة مائية (PDF) — سجل شخصي ذاتي الإنشاء
• صدّر حساباتك المحفوظة كملخص رواتب (CSV)
• حاسبة تكاليف الإقامة للمقيمين — رسوم التابعين، تأشيرات الخروج والعودة، التأمين الصحي
• مرجع تكلفة المعيشة حسب المدينة لمقارنة العروض بين مدن السعودية
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
- [ ] `./gradlew bundleRelease` runs clean and produces a signed `.aab`
- [ ] Privacy policy filled in, hosted, and the URL added to Play Console
- [ ] Phone screenshots captured (min 2)
- [ ] Store listing copy reviewed/edited (Section 6 is a draft, not final copy)
- [ ] Data Safety form completed in Play Console
- [ ] Content rating questionnaire completed in Play Console
- [ ] If submitting after 2026-08-31, `targetSdk` bumped to 36 first (Section 4)
