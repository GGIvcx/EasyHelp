# EasyHelp

Zeigt eine frei konfigurierbare Hilfe-Seite über `/help` an.

Teil der **EasySystems**-Pluginreihe von Ivcx.

## Features

- Ersetzt den eingebauten `/help`-Befehl durch eine eigene, vollständig konfigurierbare Hilfe
- Kopfzeile, Einträge und Fußzeile frei gestaltbar (MiniMessage)
- Alte `&`-Farbcodes in bestehenden Configs funktionieren zeilenweise weiter
- Setzt `plugins`-, `version`- und `help`-Befehle der Server-Software auf OP-only
- Übernimmt die Config automatisch aus dem alten Ordner `plugins/CustomHelp`

## Voraussetzungen

- Paper 26.3
- Java 25
- Keine weiteren Plugins nötig (LuckPerms o. Ä. nur zur Rechtevergabe empfohlen)

## Installation

1. `EasyHelp-1.0.jar` in den Ordner `plugins/` legen.
2. Server neu starten.

Existiert noch `plugins/CustomHelp/config.yml` und noch keine `plugins/EasyHelp/config.yml`, wird sie beim ersten Start automatisch übernommen.

## Befehle

| Befehl | Aliase | Beschreibung | Permission |
|--------|--------|--------------|------------|
| `/help` | `/hilfe`, `/h` | Zeigt die Hilfe an | `easysystems.help.use` |

## Permissions

| Permission | Standard | Beschreibung |
|------------|----------|--------------|
| `easysystems.help.*` | op | Gewährt alle EasyHelp-Berechtigungen |
| `easysystems.help.use` | true | Erlaubt das Anzeigen der Hilfe |

## Konfiguration

`plugins/EasyHelp/config.yml`:

```yaml
# Präfix für Chat-Rückmeldungen (MiniMessage-Format)
prefix: '<gold>EasyHelp</gold> <dark_gray>┃</dark_gray> '
# Unterstützt MiniMessage. Alte &-Farbcodes funktionieren zeilenweise weiterhin.
header: '<dark_gray><st>        </st></dark_gray> <gold>Server-Hilfe</gold> <dark_gray><st>        </st></dark_gray>'
entries:
  - '<dark_gray>»</dark_gray> <yellow>/spawn</yellow> <dark_gray>-</dark_gray> <gray>Teleportiere dich zum Spawn'
  - '<dark_gray>»</dark_gray> <yellow>/home</yellow> <dark_gray>-</dark_gray> <gray>Teleportiere dich nach Hause'
  - '<dark_gray>»</dark_gray> <yellow>/sethome</yellow> <dark_gray>-</dark_gray> <gray>Setze dein Zuhause'
  - '<dark_gray>»</dark_gray> <yellow>/tpa \<Spieler></yellow> <dark_gray>-</dark_gray> <gray>Sende eine Teleport-Anfrage'
  - '<dark_gray>»</dark_gray> <yellow>/msg \<Spieler> \<Text></yellow> <dark_gray>-</dark_gray> <gray>Sende eine private Nachricht'
  - '<dark_gray>»</dark_gray> <yellow>/kit</yellow> <dark_gray>-</dark_gray> <gray>Zeige verfügbare Kits'
  - '<dark_gray>»</dark_gray> <yellow>/warp \<Name></yellow> <dark_gray>-</dark_gray> <gray>Teleportiere dich zu einem Warp'
  - '<dark_gray>»</dark_gray> <yellow>/vote</yellow> <dark_gray>-</dark_gray> <gray>Vote für den Server und erhalte Belohnungen'
footer: '<dark_gray><st>                              </st></dark_gray>'
```

- `prefix`: Präfix für Fehlermeldungen im Chat.
- `header`: Kopfzeile der Hilfe (leer = keine).
- `entries`: Liste der Hilfezeilen, je ein Eintrag pro Zeile. Ist die Liste leer, erscheint eine Fehlermeldung.
- `footer`: Fußzeile der Hilfe (leer = keine).

Alle Texte nutzen das [MiniMessage-Format](https://docs.advntr.dev/minimessage/format.html). Zeilen mit alten `&`-Codes (z. B. `&a`) werden weiterhin im Legacy-Format gelesen. Ein `<` im Text muss als `\<` geschrieben werden.

## Bauen

```bash
JAVA_HOME=/pfad/zu/jdk-25 mvn clean package
```

Das Ergebnis liegt in `target/EasyHelp-1.0.jar`.
