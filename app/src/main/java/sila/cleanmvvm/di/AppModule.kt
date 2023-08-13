package sila.cleanmvvm.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sila.cleanmvvm.core.util.DATABASE_NAME
import sila.cleanmvvm.feature_dictionary.data.local.Converters
import sila.cleanmvvm.feature_dictionary.data.local.DictionaryDatabase
import sila.cleanmvvm.feature_dictionary.data.remote.DictionaryAPI
import sila.cleanmvvm.feature_dictionary.data.remote.DictionaryAPI.Companion.BASE_URL
import sila.cleanmvvm.feature_dictionary.data.repository.WordInfoRepositoryImpl
import sila.cleanmvvm.feature_dictionary.data.util.GsonParser
import sila.cleanmvvm.feature_dictionary.domain.repository.WordInfoRepository
import sila.cleanmvvm.feature_dictionary.domain.usecase.GetWordInfo
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGetWordInfoUseCase(repo: WordInfoRepository): GetWordInfo {
        return GetWordInfo(repo)
    }

    @Provides
    @Singleton
    fun provideWordInfoRepository(api: DictionaryAPI, db: DictionaryDatabase): WordInfoRepository {
        return WordInfoRepositoryImpl(api, db.dao)
    }

    @Provides
    @Singleton
    fun provideDictionaryDatabase(app: Application): DictionaryDatabase {
        return Room.databaseBuilder(
            app, DictionaryDatabase::class.java, DATABASE_NAME
        ).addTypeConverter(Converters(GsonParser(Gson())))
            .build()
    }

    @Provides
    @Singleton
    fun provideDictionaryApi(): DictionaryAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryAPI::class.java)
    }

}