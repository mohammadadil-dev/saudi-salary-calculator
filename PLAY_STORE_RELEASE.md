# Play Store Release Guide

Everything needed to take this app from source to a signed, submittable Play Store release.
Read the whole **Before you publish** checklist at the bottom before uploading anything.

## 1. Signing

A release keystore has already been generated:

- Keystore file: `release-keystore.jks` (project root)
- Key alias: `saudisalarycalculator`
- Credentials: `keystore.properties` (project root)

Both files are listed in `.gitignore` and must **never** be committed. `app/build.gradle.kts`
reads them automatically — if `keystore.properties` is missing, the release build type just
builds unsigned (so a fresh clone without the file still compiles).

**Back this up now, somewhere other than this machine** (password manager, encrypted cloud
storage) — both `release-keystore.jks` and `keystore.properties`. If you lose the keystore, Google
does not have a recovery path: you would never be able to publish an update to this app listing
again, only a brand-new listing under a new package name. The passwords were also shared in chat
when they were generated — save them now if you haven't.

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

Currently `versionCode = 1`, `versionName = "1.0"` (`app/build.gradle.kts`) — correct for a first
release, no change needed. For every future release: bump `versionCode` by at least 1 (it must
strictly increase, Play Console rejects re-using or lowering it) and update `versionName` to
whatever you want users to see (e.g. `"1.1"`).

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

## 6. Store listing copy (draft)

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
• Guided net salary calculator with GOSI and EOSB built in
• Compare two job offers side by side — net salary, GOSI, EOSB, percentage difference
• Generate a formatted payslip, export it as a PDF, and share it
• Save your calculation history — reopen, edit, or delete any past calculation
• Fully bilingual: English and Arabic, with right-to-left layout support
• Light and dark themes

Whether you're evaluating a new offer, negotiating a raise, or just want to understand your
payslip, Saudi Salary Calculator gives you the numbers in seconds — no spreadsheet required.
```

**What's new (v1.0):**
> First release: net salary calculator, offer comparison, payslip with PDF export, calculation
> history, English/Arabic support, light/dark themes.

**Category:** Finance (or Tools — Finance is the closer fit for a salary/GOSI calculator).

Treat all of the above as a draft — Mohammad should read it over and adjust tone/wording before
submitting; this wasn't run past anyone else for legal or marketing review.

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
