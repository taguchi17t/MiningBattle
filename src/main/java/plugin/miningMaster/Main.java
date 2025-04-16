package plugin.miningMaster;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.miningMaster.command.MiningBattleCommand;

public final class Main extends JavaPlugin {

  @Override
  public void onEnable() {
    MiningBattleCommand MiningBattle = new MiningBattleCommand(this);
    Bukkit.getPluginManager().registerEvents(MiningBattle, this);
    getCommand("gameStart").setExecutor(MiningBattle);
  }
}

