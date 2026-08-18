# 🧮 Applied Theory of Computational Methods — Kotlin Multiplatform App

A cross-platform mobile application built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**, targeting Android and iOS from a single shared codebase.

The app implements core numerical methods from computational mathematics — including numerical integration and differential equation solving — as an interactive educational tool.

---

## 📱 Platforms

| Platform | Status |
|---|---|
| Android | ✅ Supported |
| iOS | ✅ Supported |

---

## 🎯 Features

- Numerical integration methods (e.g. Trapezoidal rule, Simpson's rule)
- Solving ordinary differential equations (ODEs) numerically
- Shared computation logic across Android and iOS via KMP
- UI built with Compose Multiplatform

---

## 🛠 Tech Stack

| Tool | Purpose |
|---|---|
| Kotlin | Core language |
| Kotlin Multiplatform (KMP) | Shared logic for Android & iOS |
| Compose Multiplatform | Shared UI layer |
| Gradle (KTS) | Build system |

---

## 📁 Project Structure

```
AppliedTheoryOfComputationalMethods/
├── composeApp/
│   └── src/
│       ├── commonMain/     # Shared code (algorithms + UI)
│       ├── androidMain/    # Android-specific code
│       └── iosMain/        # iOS-specific code
├── iosApp/                 # iOS entry point (Xcode)
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 🚀 How to Run

### Android
```bash
./gradlew :composeApp:assembleDebug
```
Or run directly from Android Studio.

### iOS
Open the `/iosApp` folder in **Xcode** and run on a simulator or device.

---

## 👤 Author

**Zhora Manukyan**
- GitHub: [@ZhoraManukyan](https://github.com/ZhoraManukyan)
- LinkedIn: [linkedin.com/in/zhora-manukyan](https://linkedin.com/in/zhora-manukyan)
- Email: zhora.a.manukyan@gmail.com
