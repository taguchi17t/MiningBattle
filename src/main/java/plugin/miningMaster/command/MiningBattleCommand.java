package plugin.miningMaster.command;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.ibatis.jdbc.Null;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import plugin.miningMaster.MPScoreData;
import plugin.miningMaster.Main;
import plugin.miningMaster.mapper.data.ExecutingPlayer;
import plugin.miningMaster.mapper.data.MPScore;

public class MiningBattleCommand extends BaseCommand implements Listener {

  public static final int GAME_TIME = 20;
  public static final String LIST = "list";

  private final Main main;
  private final MPScoreData mpScoreData = new MPScoreData();
  private final List<ExecutingPlayer> executingPlayerList = new ArrayList<>();

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
    removePotionEffect(player);
    initPlayerStatus(player);
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

  @EventHandler
  public void onBlockBreak(BlockBreakEvent e){
    Block block = e.getBlock();
    Player player = e.getPlayer();
        int point;
        String message = "";

          switch (block.getType()) {
            case COAL_ORE:
              point = 3;
              message = "石炭鉱石をゲット！現在のスコアは" + (point) + "点!";
              break;
            case IRON_ORE:
              point = 5;
              message = "鉄鉱石をゲット！現在のスコアは" + (point) + "点!";
              break;
            case GOLD_ORE:
              point = 10;
              message = "金鉱石をゲット！現在のスコアは" + (point) + "点!";
              break;
            case DIAMOND_ORE:
              point = 30;
              message = "ダイヤモンド鉱石をゲット！現在のスコアは" + (point) + "点!";
              break;
            default:
              break;
          }

          if(!message.isEmpty()) {
            player.sendMessage(message);
          }

//    for (ExecutingPlayer executingPlayer : executingPlayerList) {
//      if (executingPlayer.getPlayerName().equals(player.getName())) {
//        int point;
//        String message = "";
//
//        switch (block.getType()) {
//          case COAL_ORE:
//            point = 3;
//            message = "石炭鉱石をゲット！現在のスコアは" + (executingPlayer.getScore() + point) + "点!";
//            break;
//          case IRON_ORE:
//            point = 5;
//            message = "鉄鉱石をゲット！現在のスコアは" + (executingPlayer.getScore() + point) + "点!";
//            break;
//          case GOLD_ORE:
//            point = 10;
//            message = "金鉱石をゲット！現在のスコアは" + (executingPlayer.getScore() + point) + "点!";
//            break;
//          case DIAMOND_ORE:
//            point = 30;
//            message = "ダイヤモンド鉱石をゲット！現在のスコアは" + (executingPlayer.getScore() + point) + "点!";
//            break;
//          default:
//            point = 0;
//            break;
//        }
//
//        executingPlayer.setScore(executingPlayer.getScore() + point); // スコアを加算
//        if(!message.isEmpty()) {
//          player.sendMessage(message);
//        }

    }

  /**
   * 現在実行しているプレイヤーのスコア情報を取得する。
   * @param player コマンドを実行したぷれいやー
   * @return 現在実行しているプレイヤーの情報
   */
  private ExecutingPlayer getPlayerScore(Player player) {
    ExecutingPlayer executingPlayer = new ExecutingPlayer(player.getName());

    if (executingPlayerList.isEmpty()) {
      executingPlayer = addNewPlayer(player);
    } else {
      executingPlayer = executingPlayerList.stream().findFirst()
          .map(ps -> ps.getPlayerName().equals(player.getName())
              ? ps
              : addNewPlayer(player)).orElse(executingPlayer);
    }
    executingPlayer.setGameTime(GAME_TIME);
    executingPlayer.setScore(0);
    removePotionEffect(player);
    return executingPlayer;
  }

  /**
   * 新規のプレイヤー情報をリストに追加します。
   *
   * @param player 新規王レイヤー
   * @return 新規プレイヤー
   */
  private ExecutingPlayer addNewPlayer(Player player) {
    ExecutingPlayer newPlayer = new ExecutingPlayer(player.getName());
    executingPlayerList.add(newPlayer);
    return newPlayer;
  }

  /**
   * プレイヤーのステータスを開始時の状態に上書きします
   * @param player コマンドを実行したプレイヤー
   */
  private void initPlayerStatus(Player player) {
    player.setHealth(20);
    player.setFoodLevel(20);
    PlayerInventory inventory = player.getInventory();
    inventory.setItemInMainHand(new ItemStack(Material.DIAMOND_PICKAXE));
  }

  /**
   * プレイヤーに設定されている特殊効果を除外します。
   *
   * @param player　コマンドを実行したプレイヤー
   */
  private void removePotionEffect(Player player) {
    player.getActivePotionEffects().stream()
        .map(PotionEffect::getType)
        .forEach(player::removePotionEffect);
  }

  @Override
  public boolean onExecuteNPCCommand(CommandSender sender, Command command, String label, String[] args) {
    return false;
  }
}
