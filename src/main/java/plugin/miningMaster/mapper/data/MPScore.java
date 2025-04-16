package plugin.miningMaster.mapper.data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *プレイヤーのスコア情報を扱うオブジェクト
 * DBに存在するテーブルと連動する
 */
@Getter
@Setter
@NoArgsConstructor
public class MPScore {

  private int id;
  private String playerName;
  private int score;
  private LocalDateTime registeredAt;
}
