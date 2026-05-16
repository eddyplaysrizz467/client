# Falcon

Falcon is now a Fabric Minecraft mod scaffold plus a small movement-checking core.

Implemented:

- Packet movement tracker.
- Collision-based ground calculation through `CollisionProvider`.
- Timer check.
- Basic horizontal speed envelope.
- Replay/debug trace frames.
- Fabric server movement-packet bridge.
- In-game client UI opened with Right Shift.
- Operator commands: `/falcon status`, `/falcon on`, `/falcon off`.

Intentionally not implemented yet:

- Punishments or automated actions.
- Rotation-primary checks.
- Client-name or bypass-mode detection.

Target:

- Minecraft `1.20.1`
- Fabric Loader `0.15.11+`
- Java `17+`

Core question:

> Could this player have reached this state legally from the previous server-known state?

Build the mod jar with Gradle:

```powershell
gradle build
```

The built jar will appear under `build/libs/`.

During development, the no-framework smoke test can still be compiled with plain `javac` by compiling only the core `falcon` packages that do not import Minecraft/Fabric APIs.
