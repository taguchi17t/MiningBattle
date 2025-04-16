package plugin.miningMaster.command;

import java.time.format.DateTimeFormatter;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import plugin.miningMaster.MPScoreData;
import plugin.miningMaster.Main;
import plugin.miningMaster.mapper.data.MPScore;

public class MiningBattleCommand extends BaseCommand implements Listener {

  public static final String LIST = "list";

  private final Main main;
  private final MPScoreData mpScoreData = new MPScoreData();

  public MiningBattleCommand(Main main) {
    this.main = main;
  }

  @Override
  public boolean onExecutePlayerCommand(Player player, Command command, String label, String[] args) {
    //最初の引数が「list」だったらスコアを一覧表示して処理を終了する。
    if (args.length == 1 && (LIST.equals(args[0]))) {
      sendMPScoreList(player);
      return false;
    }
    return false;
  }

  /**
   * 現在登録されているスコアの一覧をメッセージに送る。
   *
   * @param player プレイヤー
   */
  private void sendMPScoreList(Player player) {
    List<MPScore> mpScoreList = mpScoreData.selectList();
    for(MPScore mpScore : mpScoreList) {
      player.sendMessage(
                mpScore.getId()+ " | "
              + mpScore.getPlayerName() + " | "
              + mpScore.getScore() + " | "
              + mpScore.getRegisteredAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
  }

  @Override
  public boolean onExecuteNPCCommand(CommandSender sender, Command command, String label, String[] args) {
    return false;
  }
}
