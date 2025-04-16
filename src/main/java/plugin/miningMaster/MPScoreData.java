package plugin.miningMaster;

import java.io.InputStream;
import java.util.List;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import plugin.miningMaster.mapper.MPMapper;
import plugin.miningMaster.mapper.data.MPScore;

/**
 * DB接続やそれに付随する登録や更新処理を行うクラスです。
 */
public class MPScoreData {

  private final SqlSessionFactory sqlSessionFactory;

  public MPScoreData() {
    try {
      InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
      this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * プレイヤースコアテーブルから一覧でスコア情報を取得する。
   *
   * @return スコア情報の一覧
   */
  public List<MPScore> selectList() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      MPMapper mapper = session.getMapper(MPMapper.class);
      return mapper.selectList();
    }
  }

  /**
   * プレイヤースコアテーブルにスコア情報を登録する。
   *
   * @param mpScore プレイヤースコア
   */
  public void insert(MPScore mpScore) {
    try (SqlSession session = sqlSessionFactory.openSession(true)) {
      MPMapper mapper = session.getMapper(MPMapper.class);
      mapper.insert(mpScore);
    }
  }
}