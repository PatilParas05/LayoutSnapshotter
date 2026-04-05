# LayoutSnapshotter

[![](https://jitpack.io/v/PatilParas05/LayoutSnapshotter.svg)](https://jitpack.io/#PatilParas05/LayoutSnapshotter)

Easily capture and export any Android UI as an image or PDF! **LayoutSnapshotter** is a lightweight Kotlin library that lets you capture normal views, scrollable content, Jetpack Compose screens, and even entire RecyclerViews. Save the result to your gallery, export as PDF, or instantly share via the Android share sheet.

---

## Features

- 📸 Capture any Android `View` (including off-screen content in `ScrollView`, `NestedScrollView`, or `RecyclerView`) as a `Bitmap`
- 🧩 Capture Jetpack Compose screens wrapped in `ComposeView`
- 🖼️ Save captured images directly to the Gallery (with proper MediaStore handling)
- 📄 Export any view or captured bitmap as a PDF file
- 📤 Instantly share screenshots or exported PDFs using Android’s native share sheet
- 🟦 100% Kotlin, small footprint

---

## Installation

### Step 1: Add JitPack to your `settings.gradle`

At the end of your root `settings.gradle`:
```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2: Add the dependency

```gradle
dependencies {
    implementation 'com.github.PatilParas05:LayoutSnapshotter:1.0.0'
}
```

---

## Quick Start

Here's how to capture and share a screenshot or PDF of any view (including Jetpack Compose!):

```kotlin
val context: Context = ...      // An Activity or Fragment context
val view: View = ...            // The view you want to capture

// 1. Capture the View as Bitmap
val bitmap = LayoutSnapshotter.capture(view)

// 2. Save to gallery
val uri = LayoutSnapshotter.saveToGallery(context, bitmap)

// 3. Share Screenshot
LayoutSnapshotter.share(context, bitmap, "Share Screenshot")

// 4. Export as PDF
val pdfFile = LayoutSnapshotter.exportPdf(context, view, "my_snapshot")
// Share PDF file
if (pdfFile != null) LayoutSnapshotter.sharePdf(context, pdfFile, "Share PDF")
```

### Jetpack Compose Example

```kotlin
val composeView: ComposeView = ... // Reference to your ComposeView
val bitmap = LayoutSnapshotter.capture(composeView)
```

---

## Demo

A complete usage example is available in `app/src/main/java/dev/paraspatil/snapkit/MainActivity.kt`.

Key actions from the demo app:
- **Capture & Save to Gallery**
- **Share Screenshot**
- **Export as PDF & Share**

---

## API Reference

### Main Methods

- `capture(view: View, backgroundColor: Int = Color.WHITE): Bitmap`
    - Capture any View as a bitmap (including Compose screens wrapped in ComposeView)
- `captureScrollView(scrollView: ScrollView, backgroundColor: Int): Bitmap`
    - Capture entire content, even if scrollable
- `captureRecyclerView(recyclerView: RecyclerView, backgroundColor: Int): Bitmap`
    - Capture all items, even off-screen
- `saveToGallery(context, bitmap, fileName?): Uri?`
    - Save a bitmap to device gallery
- `exportPdf(context, view, fileName?, backgroundColor?): File?`
    - Export the view as a PDF file
- `share(context, bitmap, title?)`
    - Open the Android share sheet to share a screenshot
- `sharePdf(context, pdfFile, title?)`
    - Share a generated PDF

---

## Permissions

Add these permissions to your app’s `AndroidManifest.xml` (for saving to gallery on older Android):

```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="28" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" android:maxSdkVersion="32" />
```

Configure a FileProvider for sharing files:

```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.layoutsnapshotter.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_provider_paths" />
</provider>
```
*(Update `file_provider_paths.xml` as needed for your app.)*

---

## License

This project is licensed under the MIT License.

---


## Links

- [View on JitPack](https://jitpack.io/#PatilParas05/LayoutSnapshotter)
- [Demo App Source](app/src/main/java/dev/paraspatil/snapkit/MainActivity.kt)
- [Issue Tracker](https://github.com/PatilParas05/LayoutSnapshotter/issues)

---

## Author

Made with ❤️ by [@PatilParas05](https://github.com/PatilParas05)
