<h1 align="center">Elyra</h1>

<p align="center">
  A smart home controller for Android — organise, monitor and control your home in realtime.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/platform-Android-3DDC84" alt="Platform">
  <img src="https://img.shields.io/badge/minSdk-28-blue" alt="Min SDK">
  <img src="https://img.shields.io/badge/Kotlin-2.2.10-7F52FF" alt="Kotlin">
  <img src="https://img.shields.io/badge/Jetpack%20Compose-BOM%202026.02-4285F4" alt="Jetpack Compose">
  <img src="https://img.shields.io/badge/Firebase-Auth%20%2B%20Firestore-FFCA28" alt="Firebase">
</p>

---

## Overview

Elyra turns a phone into the control surface for a connected home. Devices are
grouped into floors and rooms, controlled over Cloud Firestore so every client
stays in sync, and watched by a safety layer that cuts power to high-risk
appliances left running too long.

A companion [hardware simulator](../elyra-simulator) stands in for the physical
appliances, so the full control path can be demonstrated without any hardware.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [Data Model](#data-model)
- [Testing](#testing)
- [License](#license)

## Features

| | |
| --- | --- |
| **Device control** | Lights with brightness, outlets, multi-channel gang boxes, safety appliances and security cameras — each with only the controls its type supports. |
| **Floors & rooms** | Rooms are laid out as zones on a floor plan that shows live device state at a glance. |
| **Realtime sync** | Firestore snapshot listeners keep every screen current; a change on any client appears instantly. |
| **Safety cutoff** | Appliances that exceed their configured maximum ON duration are switched off automatically. |
| **Alerts** | Cutoffs, faults and disconnections raise a system notification and are recorded in history. |
| **Usage reports** | Accumulated runtime for the whole home or a single device. |
| **Accounts & themes** | Email sign-up, sign-in and password reset, with light, dark and system themes. |

## Tech Stack

| Layer | Technology |
| --- | --- |
| Language | Kotlin 2.2 |
| UI | Jetpack Compose (Material 3) |
| Architecture | MVVM + Repository |
| Backend | Firebase Authentication, Cloud Firestore |
| Build | Gradle (Kotlin DSL), AGP |

## Getting Started

### Prerequisites

| Requirement | Version |
| --- | --- |
| JDK | 17 or newer |
| Android SDK | 37 (compile), targets 36 |
| Android Studio | Ladybug or newer *(recommended)* |
| Device / emulator | Android 9 (API 28) or newer |

### Installation

```bash
git clone <repository-url>
cd elyra/elyra-mobile
./gradlew assembleDebug
```

`app/google-services.json` is committed, so a fresh clone builds and runs with
no further setup.

### Running

```bash
./gradlew installDebug
```

Or open the project in Android Studio and press **Run**.

### Using your own Firebase project

1. Create a project in the [Firebase console](https://console.firebase.google.com).
2. Register an Android app and replace `app/google-services.json`.
3. Enable **Email/Password** authentication.
4. Deploy the security rules:

   ```bash
   firebase deploy --only firestore:rules
   ```

## Project Structure

```
app/src/main/java/com/keeththigan/elyra/
├── app/         Application shell and navigation graph
├── core/        Connectivity, design system, notifications
├── data/        Models, preferences, Firestore repositories
└── feature/     One package per feature — screens and ViewModels
    ├── auth/          Sign-in, sign-up, password reset
    ├── devices/       Device list, detail and controls
    ├── floors/        Floors, rooms and floor plan
    ├── home/          Dashboard
    ├── notifications/ Alert history
    ├── reports/       Usage reporting
    └── settings/      Profile and preferences
```

## Architecture

MVVM over a repository layer, with Jetpack Compose for all UI.

```
Compose UI  ──►  ViewModel (UiState)  ──►  Repository  ──►  Cloud Firestore
     ▲                                                            │
     └──────────────── realtime Flow ◄────────── snapshot listener ┘
```

- **Repositories** expose realtime `Flow`s backed by Firestore snapshot
  listeners, so changes propagate without a manual refresh.
- **ViewModels** hold a single immutable `UiState` and apply control changes
  optimistically, reverting on failure.
- **Connectivity** is tracked in two independent forms — whether the phone can
  reach the cloud, and whether the device hardware is linked. Device status is
  derived from both rather than stored.

## Data Model

Collections are top-level and scoped by a `userId` field, enforced on every read
and write by [`firestore.rules`](firestore.rules).

| Collection | Contents |
| --- | --- |
| `users` | Profile document, keyed by Firebase uid |
| `floors` | Named floors |
| `rooms` | Named rooms, each referencing a `floorId` |
| `devices` | Devices, optionally assigned to a room |
| `notifications` | Alert history |

> **Note**
> The simulator reads and writes the same documents. Any model change must be
> mirrored in [`elyra-simulator/src/lib/types.ts`](../elyra-simulator/src/lib/types.ts).

## Testing

```bash
./gradlew test                  # unit tests
./gradlew connectedAndroidTest  # instrumented tests (requires a device)
./gradlew lint                  # Android lint
```

## License

This project is currently unlicensed. All rights reserved.
