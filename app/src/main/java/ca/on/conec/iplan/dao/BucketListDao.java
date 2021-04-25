/**
 * FileName : BucketListDao.java
 * Purpose : BucketList Dao using ROOM
 * Revision History :
 *          2021 04 19  Henry   Create DAO
 */
package ca.on.conec.iplan.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;


import ca.on.conec.iplan.entity.BucketList;
import ca.on.conec.iplan.entity.Todo;

@Dao
public interface BucketListDao {
    @Query("SELECT * FROM BucketList ORDER BY `ORDER` ASC")
    LiveData<List<BucketList>> findAll();

    @Query("SELECT * FROM BucketList WHERE `bucketId` == :id")
    LiveData<BucketList> findOne(long id);

    @Insert
    void insertAll(BucketList... bucket);

    @Insert
    void insert(BucketList bucket);

    @Update
    void update(BucketList bucket);

    @Delete
    void delete(BucketList bucket);
}
