# Elyra

![Platform](https://img.shields.io/badge/platform-Android-3DDC84)
![Min SDK](https://img.shields.io/badge/minSdk-28-blue)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-BOM%202026.02-4285F4)
![Firebase](https://img.shields.io/badge/Firebase-Auth%20%2B%20Firestore-FFCA28)

A smart home controller for Android. Devices are organised into floors and
rooms, controlled in realtime through Cloud Firestore, and protected by a safety
cutoff that switches high-risk appliances off when they have been running too
long.

## Features

- **Devices** — lights (brightness), outlets, multi-switch gang boxes with
  individually named channels, safety appliances (max ON duration) and security
  cameras. Only the fields belonging to a type are persisted.
- **Floors and rooms** — rooms are laid out as zones on an abstract floor plan
  showing live device state. Deleting a floor removes its rooms; devices are
  unassigned rather than destroyed.
- **Safety cutoff** — forces any safety appliance past its configured limit to
  OFF and writes the state to Firestore, so every client sees it.
- **Alerts** — cutoffs, faults and disconnections raise a system notification
  and are recorded in Firestore.
- **Usage reporting** — runtime accumulates per device, for the whole home or
  one device.
- **Accounts** — email sign-up, sign-in and password reset. Light, dark and
  system themes.

## Architecture

MVVM over a repository layer, with Jetpack Compose for all UI.

```
app/src/main/java/com/keeththigan/elyra/
├── app/            Application shell and navigation graph
├── core/           Connectivity, design system, notifications
├── data/           Models, preferences, Firestore repositories
└── feature/        One package per feature: screens and ViewModels
```

Repositories expose realtime `Flow`s backed by Firestore snapshot listeners, so
a change from any client propagates to the UI without a refresh. ViewModels hold
a single immutable `UiState` and apply control changes optimistically, reverting
on failure.

Two notions of "offline" are kept separate: `NetworkMonitor` reports whether the
phone can reach the cloud (Firestore otherwise queues writes silently and the UI
would imply changes that never reached the hardware), while `DeviceConnectivity`
is the hardware's own link state. `DeviceStatus` is derived from both and never
stored — connectivity outranks power, so an unreachable device reads
`DISCONNECTED`, not `ON`.

## Data model

Collections are top-level and scoped by a `userId` field, enforced on every read
and write by [`firestore.rules`](firestore.rules).

| Collection      | Contents                                     |
| --------------- | -------------------------------------------- |
| `users`         | Profile document, keyed by Firebase uid      |
| `floors`        | Named floors                                 |
| `rooms`         | Named rooms, each referencing a `floorId`    |
| `devices`       | Devices, optionally assigned to a room       |
| `notifications` | Alert history                                |

`Device.isOn` and `SwitchChannel.isOn` carry an explicit `@PropertyName("isOn")`.
Without it Firestore writes the field as `on` and reads it back as `isOn`,
deserialising every device to off.

## Getting started

Requires JDK 17+ and Android SDK 37. The app targets SDK 36 and supports API 28
and above.

```bash
./gradlew assembleDebug
```

`app/google-services.json` is committed, so a fresh clone builds and runs
without further setup. To use a different Firebase project, replace that file,
enable Email/Password authentication, and run
`firebase deploy --only firestore:rules`.

## Testing

```bash
./gradlew test                  # unit tests
./gradlew connectedAndroidTest  # instrumented tests, requires a device
```

Coverage is currently limited to the generated scaffolding.
