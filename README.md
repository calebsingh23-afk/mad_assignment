# Pantry & Plate

Smart Pantry Manager for MAD700, Caleb Singh, 402306657. A Java Android app that suggests only recipes for which every ingredient and sufficient quantity are available.

SQLiteOpenHelper provides private, offline persistence without accounts, internet or backend configuration. Settings use SharedPreferences.

## Run
Open this folder in Android Studio. Install Android SDK 35 and use JDK 17 or 21. Sync Gradle, create an API 35 emulator (or connect an Android 8+ phone), then Run app. On Windows use gradlew.bat assembleDebug. No API keys or permissions required.

On Windows, projects opened from a OneDrive path automatically keep generated build files under `%LOCALAPPDATA%/PantryAndPlate/builds` to avoid sync-related deletion failures. After updating the root build.gradle file, use File > Sync Project with Gradle Files before running so Android Studio discovers the new output location. Projects outside OneDrive retain the normal build folders.

AI assistance was used in implementation and documentation. Review and understand all code before submission. The student must provide their own declaration and narration.

## Matching policy

All required ingredients must be available in sufficient quantity. Common plurals and case/whitespace are normalized. Compatible mass units (g, kg) and volume units (ml, l) convert to base units. Pieces never convert to mass or volume. Duplicate stock and repeated requirements are aggregated. Expired stock is excluded, while stock expiring today remains eligible. No oil, water or seasoning is assumed.

## Verification

Run `powershell -ExecutionPolicy Bypass -File tests/run-tests.ps1` with a JDK on PATH for the 17 pure Java matching checks.

Build device checks with `gradlew.bat assembleDebug assembleDebugAndroidTest`. Install `app/build/outputs/apk/debug/app-debug.apk` and `app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk` using adb. Run `adb shell am instrument -w za.ac.pantry.test/za.ac.pantry.test.DatabaseChecks`. The runner creates and removes only its own test row, checks the 20 seeded recipes, and verifies real SQLite CRUD and reopening persistence.

See `docs/validation-summary.txt` for the tested toolchain and device. Screenshots are unmodified captures from the installed Android application. No web facsimile or mocked screenshot was used.

## Demonstration scenario

Add Bread 2 piece, Tomatoes 1 piece and Olive oil 5 ml. Tomato toast appears in Recipes. Reduce bread to 1 piece and the recipe disappears. Restore 2 pieces and it reappears. Delete oil and it disappears again. Force-stop and reopen the app to verify persisted changes. Settings retains its expiry badge switch.

## Implementation map

- `Matcher.java`: pure Java matching and unit normalization.
- `Database.java` and `Seed.java`: SQLite schema, CRUD and twenty recipes.
- `PantryActivity.java`, `IngredientActivity.java`, `RecipesActivity.java`, `RecipeDetailActivity.java`, `SettingsActivity.java`: five screens.
- `PantryAdapter.java` and `RecipeAdapter.java`: custom ListView rows.
- `Ui.java` and `BaseActivity.java`: visual primitives, explicit navigation and background database work.

## Submission preparation

The student must review the report, sign the supplied cover declaration, record the 5 to 7 minute narrated demonstration, and add the MP4 directly to the submission ZIP. Keep the complete ZIP below 50 MB. Requested report and ZIP filenames follow the student's explicit instructions. The due date is deliberately blank. Git commits retain their true timestamps and were created during a single assisted session; they must not be described as work spread across several days.
