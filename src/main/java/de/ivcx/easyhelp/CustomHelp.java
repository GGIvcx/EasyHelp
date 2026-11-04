package de.ivcx.easyhelp;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;

public class CustomHelp extends JavaPlugin implements CommandExecutor, Listener {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final Pattern LEGACY = Pattern.compile("&[0-9a-fk-or]", Pattern.CASE_INSENSITIVE);
    private static final String PREFIX_DEFAULT = "<gold>EasyHelp</gold> <dark_gray>┃</dark_gray> ";

    @Override
    public void onEnable() {
        migrateOldConfig();
        saveDefaultConfig();

        getCommand("help").setExecutor(this); // only serves aliases hilfe/h: plugin.yml cannot take over "help"
        // Bukkit's built-in help is not overridable via plugin.yml, so register /help through Paper's command API
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->
            event.registrar().register("help", "Zeigt die Hilfe an.",
                (stack, args) -> sendHelpMessage(stack.getSender())));

        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("EasyHelp wurde aktiviert.");
    }

    /** Übernimmt die Config aus dem alten Ordner plugins/CustomHelp, falls vorhanden. */
    private void migrateOldConfig() {
        File old = new File(getDataFolder().getParentFile(), "CustomHelp/config.yml");
        File now = new File(getDataFolder(), "config.yml");
        if (!old.exists() || now.exists()) return;
        try {
            getDataFolder().mkdirs();
            Files.copy(old.toPath(), now.toPath());
            getLogger().info("Alte Konfiguration aus plugins/CustomHelp wurde übernommen.");
        } catch (IOException e) {
            getLogger().warning("Alte Konfiguration konnte nicht übernommen werden: " + e.getMessage());
        }
    }

    // Core permissions are registered only after all plugins are enabled (and again on /reload), so onEnable is too early
    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        for (String node : List.of("bukkit.command.plugins", "bukkit.command.version", "bukkit.command.help", "minecraft.command.help")) {
            Permission perm = getServer().getPluginManager().getPermission(node);
            if (perm != null) perm.setDefault(PermissionDefault.OP);
        }
        // Refresh command tab completion for online players (needed after /reload)
        for (Player player : getServer().getOnlinePlayers()) {
            player.updateCommands();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("EasyHelp wurde deaktiviert.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sendHelpMessage(sender);
        return true;
    }

    // Einzige erlaubte Legacy-Nutzung: bestehende Server-Configs mit &-Codes bleiben lesbar.
    private Component parse(String line) {
        return LEGACY.matcher(line).find()
            ? LegacyComponentSerializer.legacyAmpersand().deserialize(line)
            : MM.deserialize(line);
    }

    private void error(CommandSender sender, String miniMessage) {
        String prefix = getConfig().getString("prefix", PREFIX_DEFAULT);
        sender.sendMessage(MM.deserialize(prefix + "<red>" + miniMessage));
        if (sender instanceof Player p) p.playSound(p, org.bukkit.Sound.ENTITY_VILLAGER_NO, 1f, 1f);
    }

    private void sendHelpMessage(CommandSender sender) {
        if (!sender.hasPermission("easysystems.help.use")) {
            error(sender, "Dafür hast du keine Berechtigung.");
            return;
        }
        String header = getConfig().getString("header", "");
        if (!header.isEmpty()) sender.sendMessage(parse(header));

        List<String> entries = getConfig().getStringList("entries");
        if (entries.isEmpty()) {
            error(sender, "Es sind keine Hilfe-Einträge konfiguriert.");
        } else {
            entries.forEach(e -> sender.sendMessage(parse(e)));
        }

        String footer = getConfig().getString("footer", "");
        if (!footer.isEmpty()) sender.sendMessage(parse(footer));
    }
}
