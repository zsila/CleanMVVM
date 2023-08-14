package sila.cleanmvvm.feature_dictionary.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sila.cleanmvvm.feature_dictionary.data.local.entity.WordInfoEntity


@Dao
interface DictionaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordInfos(wordInfos : List<WordInfoEntity>)

    @Query("SELECT * FROM wordinfoentity WHERE word IS :word")
    suspend fun getWordInfos(word: String): List<WordInfoEntity>

    @Query("DELETE FROM wordinfoentity WHERE word IN(:words)")
    suspend fun deleteWordInfos(words: List<String>)

}