package net.recondev.gangs.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import lombok.Getter;
import net.recondev.commons.builders.PlaceholderReplacer;
import net.recondev.gangs.ReconGangs;
import net.recondev.gangs.gang.Gang;
import net.recondev.gangs.gang.player.GangsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GangsCommand {

    private final ReconGangs plugin;

    @Getter public final CommandAPICommand gangsCommand;

    public GangsCommand(final ReconGangs plugin) {
        this.plugin = plugin;

        CommandAPICommand createCommand = new CommandAPICommand("create").withArguments(new GreedyStringArgument("name").setOptional(false)).executes((sender, args) -> {
                    if(!(sender instanceof  Player)) return;
                    final GangsPlayer gangsPlayer = plugin.getPlayerStorage().get(((Player) sender).getUniqueId());

                    if(gangsPlayer.getGang().equalsIgnoreCase("")) {
                        plugin.getMessageCache().sendMessage((Player) sender, "Messages.ALREADY-IN-GANG");
                        return;
                    }

                    final String name = (String) args.get("name");



                    if(name!=null) {
                        if(name.toCharArray().length>plugin.getSettingsConfig().getInt("Settings.Character-Limit")) {
                            plugin.getMessageCache().sendMessage((Player) sender, "Messages.LETTER-LIMIT");
                            return;
                        }
                        for(final Character c : name.toCharArray()) {
                            if(!plugin.getSettingsConfig().getStringList("Settings.Allowed-Naming-Characters").contains(String.valueOf(c).toLowerCase())) {
                                plugin.getMessageCache().sendMessage((Player) sender, "Messages.ONLY-LETTERS");
                                return;
                            }
                        }
                        if(plugin.getGangStorage().contains(name)) {
                            plugin.getMessageCache().sendMessage((Player) sender, "Messages.NAME-TAKEN");
                            return;
                        }

                        final Gang gang = new Gang(name);
                        gang.setLeader(gangsPlayer.getUuid());
                        plugin.getGangStorage().save(gang);
                        gang.getPlayers().add(gangsPlayer.getUuid());
                        gangsPlayer.setGang(gang.getName());

                        plugin.getMessageCache().sendMessage((Player) sender, "Messages.GANG-CREATED");
                        return;
                    }

                    plugin.getMessageCache().sendMessage(((Player) sender), "Messages.HELP");

        });

        CommandAPICommand kickCommand = new CommandAPICommand("kick").withArguments(new GreedyStringArgument("player").setOptional(false)).executes((sender, args) -> {
                        if(!(sender instanceof Player)) return;

                        final String player = (String) args.get("player");
                        final GangsPlayer senderPlayer = plugin.getPlayerStorage().get(((Player) sender).getUniqueId());

                        final Gang gang = plugin.getGangStorage().get(senderPlayer.getGang());

                        if(gang.getLeader()!=senderPlayer.getUuid())  {
                            plugin.getMessageCache().sendMessage((Player) sender, "Messages.NOT-THE-LEADER");
                            return;
                        }

                        if(player!=null) {
                            final GangsPlayer gangsPlayer = plugin.getPlayerStorage().get(Bukkit.getPlayer(player).getUniqueId());

                            if(!gang.getPlayers().contains(gangsPlayer.getUuid())) {
                                plugin.getMessageCache().sendMessage((Player) sender, "Messages.NOT-IN-GANG");
                                return;
                            }

                            if(gang.getLeader()==gangsPlayer.getUuid()) {
                                plugin.getMessageCache().sendMessage((Player) sender, "Messages.CANNOT-KICK-SELF");
                                return;
                            }

                            gangsPlayer.setGang("");
                            gang.getPlayers().remove(gangsPlayer.getUuid());



                            plugin.getMessageCache().sendMessage((Player) sender, new PlaceholderReplacer().addPlaceholder("%player%", player),"Messages.MEMBER-KICKED");
                            return;
                        }


                        plugin.getMessageCache().sendMessage((Player) sender, "Messages.INVALID-PLAYER");



        });

        CommandAPICommand inviteCommand = new CommandAPICommand("invite").withArguments(new GreedyStringArgument("player").setOptional(false)).executes((sender, args) -> {
                        if(!(sender instanceof Player)) return;

                        final String player = (String) args.get("player");

        });

        CommandAPICommand joinCommand = new CommandAPICommand("join").withArguments(new GreedyStringArgument("gang").setOptional(false)).executes((sender, args) -> {

                        if(!(sender instanceof Player)) return;

                        final String gangName = (String) args.get("gang");

                        final GangsPlayer gangsPlayer = plugin.getPlayerStorage().get(((Player) sender).getUniqueId());

                        if(!gangsPlayer.getGang().equals("")) {
                            plugin.getMessageCache().sendMessage((Player) sender, "Messages.ALREADY-IN-GANG");
                            return;
                        }

                        final Gang gang = plugin.gangStorage.get(gangName);
                        if(!gang.getPendingInvites().contains(gangsPlayer.getUuid())) {
                            plugin.getMessageCache().sendMessage((Player) sender, "Messages.NOT-INVITED");
                            return;
                        }

                        if(gang.getPlayers().size() >=plugin.getSettingsConfig().getInt("Settings.Max-Members")) {
                            plugin.getMessageCache().sendMessage((Player) sender, "Messages.MEMBER-LIMIT-REACHED");
                            return;
                        }

                        for(final UUID player : gang.getPlayers()) {
                            final Player inst = Bukkit.getPlayer(player);
                            if(inst.isOnline()) {
                                plugin.getMessageCache().sendMessage(inst, new PlaceholderReplacer().addPlaceholder("%member%", sender.getName()),"Messages.MEMBER-JOINED");
                            }
                        }

                        gang.getPlayers().add(gangsPlayer.getUuid());
                        gang.getPendingInvites().remove(gangsPlayer.getUuid());
                        gangsPlayer.setGang(gang.getName());

        });

        CommandAPICommand leaveCommand = new CommandAPICommand("leave").executes((sender, args) -> {
                        if(!(sender instanceof Player)) return;

                        final GangsPlayer gangsPlayer = plugin.getPlayerStorage().get(((Player) sender).getUniqueId());

                        if(gangsPlayer.getGang().equals("")) {
                            plugin.getMessageCache().sendMessage((Player) sender, "Messages.NOT-IN-GANG");
                            return;
                        }

                        final Gang gang = plugin.gangStorage.get(gangsPlayer.getGang());

                        if(gang.getLeader()==gangsPlayer.getUuid()) {
                            plugin.getMessageCache().sendMessage((Player) sender, "Messages.LEADER-CANNOT-LEAVE");
                            return;
                        }

                        gang.getPlayers().remove(gangsPlayer.getUuid());
                        gangsPlayer.setGang("");

                        plugin.getMessageCache().sendMessage((Player) sender, new PlaceholderReplacer().addPlaceholder("%gang%", gang.getName()),"Messages.LEFT-GANG");

                        for(final UUID player : gang.getPlayers()) {
                            final Player inst = Bukkit.getPlayer(player);
                            if(inst.isOnline()) {
                                plugin.getMessageCache().sendMessage(inst, new PlaceholderReplacer().addPlaceholder("%member%", sender.getName()),"Messages.MEMBER-LEFT");
                            }
                        }

        });

        CommandAPICommand topCommand = new CommandAPICommand("top").executes((sender, args) -> {

                    if(!(sender instanceof Player)) return;

                    plugin.getTopMenu().openMenu((Player) sender);


        });

        CommandAPICommand disbandCommand = new CommandAPICommand("disband").executes((sender, args) -> {

                    if(!(sender instanceof Player)) return;

                    final GangsPlayer gangsPlayer = plugin.getPlayerStorage().get(((Player) sender).getUniqueId());

                    if(gangsPlayer.getGang().equals("")) {
                        plugin.getMessageCache().sendMessage((Player) sender, "Messages.NOT-IN-GANG");
                        return;
                    }

                    final Gang gang = plugin.gangStorage.get(gangsPlayer.getGang());

                    if(gang.getLeader()!=gangsPlayer.getUuid()) {
                        plugin.getMessageCache().sendMessage((Player) sender, "Messages.NOT-THE-LEADER");
                        return;
                    }

                    for(final UUID player : gang.getPlayers()) {
                        final Player inst = Bukkit.getPlayer(player);
                        plugin.getPlayerStorage().get(player).setGang("");
                        if(inst.isOnline()) {
                            plugin.getMessageCache().sendMessage(inst,"Messages.GANG-DISBANDED");
                        }
                    }
                    plugin.getGangStorage().remove(gang.getName());



        });


        this.gangsCommand = new CommandAPICommand("gang")
                .withAliases("gangs")
                .executes((sender, args) -> {
                    if(args.count()==0) {
                        if(!(sender instanceof Player)) return;

                        plugin.getMessageCache().sendMessage((Player) sender, "Messages.HELP");

                    }
                })
                .withSubcommands(createCommand, kickCommand, disbandCommand, leaveCommand, joinCommand, topCommand, inviteCommand);



        this.gangsCommand.register();
    }


}


