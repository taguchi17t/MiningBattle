package plugin.miningMaster.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.bukkit.scoreboard.Score;
import plugin.miningMaster.mapper.data.MPScore;


public interface MPMapper {

    @Select("SELECT * FROM mp_score")
    List<MPScore> selectList();

    @Insert("insert mp_score(player_name, score, registered_at) values (#{playerName}, #{score}, now())")
    void insert(MPScore mpScore);
}
