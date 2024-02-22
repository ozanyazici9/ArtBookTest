package com.ozanyazici.artbooktest.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ozanyazici.artbooktest.R
import com.ozanyazici.artbooktest.api.RetrofitAPI
import com.ozanyazici.artbooktest.repo.ArtRepository
import com.ozanyazici.artbooktest.repo.ArtRepositoryInterface
import com.ozanyazici.artbooktest.roomdb.ArtDao
import com.ozanyazici.artbooktest.roomdb.ArtDatabase
import com.ozanyazici.artbooktest.util.Util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//dosyayı Object olarak açmamızın sebebide singleton bir obje oluşturulacağı için.

@Module
@InstallIn(SingletonComponent::class) //Room ve Retrofit uygulama genelinde ve singleton olarak inject edilebilsin diye singletonComponenet dedik.
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDatabase(
        @ApplicationContext context: Context) = Room.databaseBuilder(
            context,ArtDatabase::class.java,"ArtBookDB"
        ).build()

    @Singleton
    @Provides
    fun injectDao(database: ArtDatabase) = database.artDao()

    @Singleton
    @Provides
    fun injectRetrofitAPI(): RetrofitAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(RetrofitAPI::class.java)
    }

    //burada As diyerek artrepository sınıfının nesnesinini artrepositoryınterface türünde alıyorum
    //bu artrepository sınıfına özgü detaylardan bağımsız olmamı sağlıyor.
    @Singleton
    @Provides
    fun injectNormalRepo(dao: ArtDao, api:RetrofitAPI) = ArtRepository(dao,api) as ArtRepositoryInterface

    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) = Glide.with(context)
        .setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
        )
}