package com.netease.homework.repository;

import com.netease.homework.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query(
            value = "select transaction.* from transaction WHERE buyer_id=?1",
            countQuery = "select count(*) from transaction WHERE buyer_id=?1",
            nativeQuery = true
    )
    Page<Transaction> findAllByBuyerId(Pageable pageable, Integer buyerId);

    @Query(
            value = "select transaction.* from transaction, goods where goods.owner_id=?1 and transaction.goods_id = goods.id",
            countQuery = "select count(*) from transaction ,goods where goods.owner_id=?1 and transaction.goods_id = goods.id",
            nativeQuery = true
    )
    Page<Transaction> findAllByGoods_OwnerId(Pageable pageable, Integer sellerId);

    @Query(
            value = "select transaction.* from transaction, goods where goods.owner_id=?1 and buyer_id=?2 and transaction.goods_id = goods.id",
            countQuery = "select count(*) from transaction, goods where goods.owner_id=?1 and buyer_id=?2 and transaction.goods_id = goods.id",
            nativeQuery = true
    )
    Page<Transaction> findAllByGoods_OwnerIdAndBuyerId(Pageable pageable, Integer sellerId, Integer buyerId);
    List<Transaction> findAllByGoods_Id(Integer goodsId);
}
