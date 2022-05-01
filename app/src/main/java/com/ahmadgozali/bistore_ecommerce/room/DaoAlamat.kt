package com.ahmadgozali.bistore_ecommerce.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.ahmadgozali.bistore_ecommerce.model.Alamat

@Dao
interface DaoAlamat {

    @Insert(onConflict = REPLACE)
    fun insert(data: Alamat)

    @Delete
    fun delete(data: Alamat)

    @Update
    fun update(data: Alamat): Int

    @Query("SELECT * from alamat ORDER BY id ASC")
    fun getAll(): List<Alamat>

    @Query("SELECT * FROM alamat WHERE id = :id LIMIT 1")
    fun getAlamat(id: Int): Alamat

    @Query("SELECT * FROM alamat WHERE isSelected = :status LIMIT 1")
    fun getByStatus(status: Boolean): Alamat?

    @Query("DELETE FROM alamat")
    fun deleteAll(): Int
}