package com.idanatz.sample.persistence

import androidx.room.Dao
import androidx.room.Query
import com.idanatz.sample.models.MessageModel
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class MessageDao : BaseDao<MessageModel>() {

    @Query("SELECT * FROM messages")
    abstract fun observeTable(): Flowable<List<MessageModel>>

    @Query("SELECT * FROM messages WHERE headerId == :headerId")
    abstract fun getMessageWithHeaderId(headerId: Int): Single<List<MessageModel>>

    @Query("SELECT * FROM messages WHERE id == :id")
    abstract fun getMessageWithId(id: Int): Single<MessageModel>

    @Query("DELETE FROM messages")
    abstract fun deleteAll()

    @Query("DELETE FROM messages WHERE id % 2 == 0")
    abstract fun deleteEvenIds()
}