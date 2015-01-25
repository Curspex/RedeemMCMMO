package com.flapfactions.redeemMCMMO;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nossr50.api.ExperienceAPI;

public class main extends JavaPlugin {
	
	@Override
	public void onEnable()
	{
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	@Override
	public void onDisable()
	{
		saveConfig();
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {
		if(commandLabel.equalsIgnoreCase("addcredits")) {
			if(args.length <= 1) {
				sender.sendMessage(ChatColor.RED + "Too little arguments!");
			}
			else if(args.length == 2)
			{
				Player target = (Bukkit.getServer().getPlayer(args[0]));
				if (target == null)
				{
					sender.sendMessage(ChatColor.YELLOW + args[0] + ChatColor.RED + " is not online or doesn't exist!");
				}
				else
				{
					String targetPlayer = target.getName();
					String targetName = getConfig().getString(targetPlayer);
					try
					{
						Integer.parseInt(args[1]);
					}
					catch(NumberFormatException ex)
					{
						sender.sendMessage(ChatColor.RED + "The amount of credits must be a number!");
						return true;
					}
					int amount = Integer.parseInt(args[1]);
					if(amount <= 0)
					{
						sender.sendMessage(ChatColor.RED + "The amount must be a positive number!");
						return true;
					}
					if(targetName != null)
					{
						int oldAmount = getConfig().getInt(targetPlayer + ".credits");
						int newAmount = oldAmount+amount;
						getConfig().set(targetPlayer + ".credits", newAmount);
						saveConfig();
						sender.sendMessage(ChatColor.GREEN + "You have given " + ChatColor.GOLD + targetPlayer + ", " + amount + ChatColor.GREEN + " MCMMO credits.");
						target.sendMessage(ChatColor.GREEN + "You have just received " + ChatColor.GOLD + amount + ChatColor.GREEN + " MCMMO credits.");
						target.sendMessage(ChatColor.GREEN + "NEW CREDIT BALANCE: " + ChatColor.GOLD + newAmount);
						target.sendMessage(ChatColor.GREEN + "Do " + ChatColor.AQUA + "/rmhelp" + ChatColor.GREEN + " for help with redeeming them!");
					}
					else
					{
						getConfig().set(targetPlayer + ".credits", amount);
						saveConfig();
						sender.sendMessage(ChatColor.GREEN + "You have given " + ChatColor.GOLD + targetPlayer + ", " + amount + ChatColor.GREEN + " MCMMO credits.");
						target.sendMessage(ChatColor.GREEN + "You have just received " + ChatColor.GOLD + amount + ChatColor.GREEN + " MCMMO credits.");
						target.sendMessage(ChatColor.GREEN + "NEW CREDIT BALANCE: " + ChatColor.GOLD + amount);
						target.sendMessage(ChatColor.GREEN + "Do " + ChatColor.AQUA + "/rmhelp" + ChatColor.GREEN + " for help with redeeming them!");
					}
				}
				return true;
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Too many arguments!");
			}
		}
		else if(commandLabel.equalsIgnoreCase("takecredits"))
		{
			if(args.length <= 1)
				sender.sendMessage(ChatColor.RED + "Too little arguments!");
			else if(args.length == 2)
			{
				Player target = (Bukkit.getServer().getPlayer(args[0]));
				if (target == null)
				{
					sender.sendMessage(ChatColor.YELLOW + args[0] + ChatColor.RED + " is not online or doesn't exist!");
					return true;
				}
				else
				{
					String targetPlayer = target.getName();
					String targetName = getConfig().getString(targetPlayer);
					try
					{
						Integer.parseInt(args[1]);
					}
					catch(NumberFormatException ex)
					{
						sender.sendMessage(ChatColor.RED + "The amount of credits must be a number!");
						return true;
					}
					int amount = Integer.parseInt( args[1] );
					if(amount <= 0)
					{
						sender.sendMessage(ChatColor.RED + "The amount must be a positive number!");
						return true;
					}
					if(targetName != null)
					{
						int oldAmount = getConfig().getInt(targetPlayer + ".credits");
						if(amount <= oldAmount)
						{
							int newAmount = oldAmount-amount;
							getConfig().set(targetPlayer + ".credits", newAmount);
							saveConfig();
							sender.sendMessage(ChatColor.GREEN + "You have taken " + ChatColor.GOLD + amount + ChatColor.GREEN + " credits off " + ChatColor.GOLD + args[0]);
							return true;
						}
						else
						{
							sender.sendMessage(ChatColor.YELLOW + args[0] + ChatColor.RED + " already has 0 credits!");
							return true;
						}
					}
					else
					{
						sender.sendMessage(ChatColor.YELLOW + args[0] + ChatColor.RED + " already has 0 credits!");
						return true;
					}
				}
			}
			else
				sender.sendMessage(ChatColor.RED + "Too many arguments!");
		}
		else if(commandLabel.equalsIgnoreCase("credits"))
		{
			if(sender instanceof Player)
			{
				Player player = (Player) sender;
				if(args.length == 0)
				{
					String name = player.getName();
					String playerName = getConfig().getString(name);
					if(playerName != null) {
						int credits = getConfig().getInt(name + ".credits");
						player.sendMessage(ChatColor.GREEN + "You have " + ChatColor.GOLD + credits + ChatColor.GREEN + " MCMMO credits remaining.");
						return true;
					}
					else
					{
						player.sendMessage(ChatColor.GREEN + "You have " + ChatColor.GOLD + "0" + ChatColor.GREEN + " MCMMO credits remaining.");
						return true;
					}
				}
				else if(args.length == 1)
				{
					String targetName = getConfig().getString(args[0]);
					if(targetName != null)
					{
						int credits = getConfig().getInt(args[0] + ".credits");
						player.sendMessage(ChatColor.GOLD + args[0] + ChatColor.GREEN + " has " + ChatColor.GOLD + credits + ChatColor.GREEN + " MCMMO credits remaining.");
						return true;
					}
					else
					{
						player.sendMessage(ChatColor.GOLD + args[0] + ChatColor.GREEN + " has " + ChatColor.GOLD + "0 " + ChatColor.GREEN + "MCMMO credits remaining.");
						return true;
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + "Too many arguments!");
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "This command can only be run by a player!");
				return true;
			}
		}
		else if (commandLabel.equalsIgnoreCase("rmreload"))
		{
			sender.sendMessage(ChatColor.GREEN + "Reloaded configuration file for RedeemMCMMO!");
			reloadConfig();
			return true;
		}
		else if(commandLabel.equalsIgnoreCase("redeem"))
		{
			if(!(sender instanceof Player))
			{
				sender.sendMessage(ChatColor.RED + "This command can only be run by a player!");
				return true;
			}
			Player player = (Player) sender;
			if(args.length == 2)
			{
				String skillType = args[0];
				int cap;
				switch (skillType.toLowerCase())
				{
					case "acrobatics":
					case "alchemy":
					case "archery":
					case "axes":
					case "excavation":
					case "fishing":
					case "herbalism":
					case "mining":
					case "repair":
					case "swords":
					case "taming":
					case "unarmed":
					case "woodcutting":
						cap = ExperienceAPI.getLevelCap(skillType);
						break;
						
					default:
						player.sendMessage(ChatColor.RED + skillType + " is not a valid skill!");
	                    return true;
				}
				try
				{
					Integer.parseInt(args[1]);
				}
				catch(NumberFormatException ex)
				{
					player.sendMessage(ChatColor.RED + "The amount of credits must be a number!");
					return true;
				}
				int amount = Integer.parseInt(args[1]);
				int oldamount = getConfig().getInt(player.getName() + ".credits");
				if(oldamount < amount)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough credits!");
					return true;
				}
				if(amount <= 0) {
					player.sendMessage(ChatColor.RED + "The amount must be a positive number!");
					return true;
				}
				if(ExperienceAPI.getLevel(player, skillType)+amount>cap) {
					player.sendMessage(ChatColor.RED + "You have reached the maximum for " + skillType);
					return true;
				}
				int newamount = oldamount-amount;
				getConfig().set(player.getName() + ".credits", newamount);
				saveConfig();

				ExperienceAPI.addLevel(player, skillType, amount);
				player.sendMessage(ChatColor.GREEN + "You have added " + ChatColor.GOLD + amount + ChatColor.GREEN +" credits to " + ChatColor.GOLD + skillType + ChatColor.GREEN + ".");
				return true;
			}
			else
			{
					player.sendMessage(ChatColor.RED + "Proper syntax: " + "");
			}
		} else if(commandLabel.equalsIgnoreCase("rmhelp")) {
			sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.BLUE + "RedeemMCMMO Help ~ Player Commands" + ChatColor.YELLOW + " -----");
			sender.sendMessage(ChatColor.AQUA + "/redeem <skill> <amount>" + ChatColor.YELLOW + " - Reedeem your credits into any mcMMO skill.");
			sender.sendMessage(ChatColor.AQUA + "/buycredits <amount>" + ChatColor.YELLOW + " - Buy credits with ingame money if it is enabled.");
			sender.sendMessage(ChatColor.AQUA + "/credits [player]" + ChatColor.YELLOW + " - Veiw your own or another players credit balance.");
			sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.BLUE + "RedeemMCMMO Help ~ Admin Commands" + ChatColor.YELLOW + " -----");
			sender.sendMessage(ChatColor.AQUA + "/addcredits <player> <amount>" + ChatColor.YELLOW + " - Give a player credits.");
			sender.sendMessage(ChatColor.AQUA + "/takecredits <player> <amount>" + ChatColor.YELLOW + " - Take credits away from a player.");
			sender.sendMessage(ChatColor.AQUA + "/rmreload" + ChatColor.YELLOW + " - Reload the configuration file.");
			return true;
		}
		return false;
	}
	
}
