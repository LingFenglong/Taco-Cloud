package com.taco.tacocloud.tacos.data;

import com.taco.tacocloud.tacos.Ingredient;
import com.taco.tacocloud.tacos.Taco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Repository
public class JdbcTacoRepository implements TacoRepository{
    private JdbcTemplate jdbc;

    @Autowired
    public JdbcTacoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Taco save(Taco taco) {
        Long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        for (String ingredientId : taco.getIngredients()) {
            saveIngredientToTaco(ingredientId, tacoId);
        }

        return taco;
    }

    /*
     * 保存 taco --> Taco
     */
    private Long saveTacoInfo(Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory psf = new PreparedStatementCreatorFactory(
                "insert into Taco (name, createdAt) values (?, ?)", Types.VARCHAR, Types.TIMESTAMP
        );
        psf.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc = psf.newPreparedStatementCreator(Arrays.asList(
                taco.getName(), new Timestamp(taco.getCreatedAt().getTime())
        ));
        // keyHolder Taco的id
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);

        return keyHolder.getKey().longValue();
    }

    /*
     * 保存 ingredient(id), tacoId(id) --> Taco_Ingredients
     */
    private void saveIngredientToTaco(String ingredientId, Long tacoId) {
        jdbc.update(
                "insert into Taco_Ingredients (taco, ingredient) values (?, ?)",
                tacoId, ingredientId
        );
    }
}
