# Falcon

Falcon is a Fabric Minecraft mod scaffold plus a small movement-checking core for Minecraft `1.21.11`, the Spear Update.

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

- Minecraft `1.21.11` only
- Fabric Loader `0.18.4+`
- Fabric API `0.141.4+1.21.11`
- Java `21+`

Core question:

> Could this player have reached this state legally from the previous server-known state?

Build the mod jar with Gradle:

```powershell
gradle build
```

The built jar will appear under `build/libs/`.

During development, the no-framework smoke test can still be compiled with plain `javac` by compiling only the core `falcon` packages that do not import Minecraft/Fabric APIs.
