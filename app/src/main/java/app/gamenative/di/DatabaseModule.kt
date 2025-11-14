package app.gamenative.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import app.gamenative.db.DATABASE_NAME
import app.gamenative.db.PluviaDatabase
import app.gamenative.db.dao.AppInfoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    companion object {
        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE app_info ADD COLUMN enabled_dlc TEXT")
            }
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PluviaDatabase {
        // The db will be considered unstable during development.
        // Once stable we should add a (room) db migration
        return Room.databaseBuilder(context, PluviaDatabase::class.java, DATABASE_NAME)
            .addMigrations(MIGRATION_5_6)
            .fallbackToDestructiveMigration() // TODO remove before prod
            .build()
    }

    @Provides
    @Singleton
    fun provideSteamLicenseDao(db: PluviaDatabase) = db.steamLicenseDao()

    @Provides
    @Singleton
    fun provideSteamAppDao(db: PluviaDatabase) = db.steamAppDao()

    @Provides
    @Singleton
    fun provideSteamFriendDao(db: PluviaDatabase) = db.steamFriendDao()

    @Provides
    @Singleton
    fun provideAppChangeNumbersDao(db: PluviaDatabase) = db.appChangeNumbersDao()

    @Provides
    @Singleton
    fun provideAppFileChangeListsDao(db: PluviaDatabase) = db.appFileChangeListsDao()

    @Provides
    @Singleton
    fun provideFriendMessagesDao(db: PluviaDatabase) = db.friendMessagesDao()

    @Provides
    @Singleton
    fun provideEmoticonDao(db: PluviaDatabase) = db.emoticonDao()

    @Provides
    @Singleton
    fun provideAppInfoDao(db: PluviaDatabase): AppInfoDao = db.appInfoDao()
}
