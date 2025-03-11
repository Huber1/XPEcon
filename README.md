# XPEcon
This is an Economy Plugin for Minecraft Paper Servers

It implements the [Vault API](https://www.spigotmc.org/resources/vault.34315/), so Vault needs to be installed

## Features
- `/eco` and `/balance` commands to manage levels (standard xp command can also be used, but the bukkit xp method is inaccurate on big values)
- offline player support
- compatible with anything that uses [Vault](https://www.spigotmc.org/resources/vault.34315/)

## Building & Installing
```shell
./gradlew clean build
```
Put the file `build/libs/XPEcon-1.0-SNAPSHOT-all.jar` into your plugins folder
Works on 1.21.4 Paper

> [WARNING]
> The plugin does not work on Spigot or Bukkit.
> It uses the new Brigadier command-system from paper