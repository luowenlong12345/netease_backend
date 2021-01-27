package com.netease.homework.repository;

import com.netease.homework.domain.Goods;
import com.netease.homework.domain.User;
import com.netease.homework.domain.pojo.ListGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Integer> {
    @Query(
            value = "select  goods.*,  (select count(*) from transaction where goods_id = goods.id) as sell_numbers " +
                    "from goods WHERE owner_id = ?1",
            countQuery = "SELECT count(*) FROM goods WHERE owner_id = ?1",
            nativeQuery = true
    )
    Page<Goods> findAllByOwner(Integer ownerId, Pageable pageable);
    @Query(
            value = "select  goods.*,  (select count(*) from transaction where goods_id = goods.id) as sell_numbers " +
                    "from goods",
            countQuery = "SELECT count(*) FROM goods",
            nativeQuery = true
    )
    Page<Goods> findAll(Pageable pageable);

    @Query(
            value="select count(*) from transaction where goods_id=?1",
            nativeQuery=true
    )
    Integer getSellNumbers(Integer goodsId);

    @Query(
            value="select count(*) from transaction where goods_id=?1 and buyer_id=?2",
            nativeQuery=true
    )
    Integer getBuyNumbersByUserId(Integer goodsId, Integer userId);
}
