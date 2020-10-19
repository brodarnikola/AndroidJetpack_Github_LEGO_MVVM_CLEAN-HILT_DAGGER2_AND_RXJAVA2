package com.vjezba.androidjetpackgithub.ui.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.leinardi.android.speeddial.SpeedDialView
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.ui.fragments.HomeViewPagerFragmentDirections
import com.vjezba.androidjetpackgithub.viewmodels.LanguagesActivityViewModel
import com.vjezba.data.networking.GithubRepositoryApi
import com.vjezba.data.networking.model.RepositoryResponseApi
import com.vjezba.domain.repository.UserManager
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_languages.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@AndroidEntryPoint
class LanguagesActivity : AppCompatActivity() {

    @Inject
    lateinit var userManager: UserManager

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_languages)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val drawerToggle = ActionBarDrawerToggle(
            this, drawer_layout,
            R.string.open,
            R.string.close
        )
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.view_pager_fragment,
                R.id.paggin_with_network_and_db,
                R.id.rxjava2_tutorial
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()

        rxJava2Tutorials()

        val headerView = nav_view.getHeaderView(0)
        val navUsername = headerView.findViewById(R.id.tvNameOfUser) as TextView
        navUsername.text = "Welcome: ".plus(userManager.getUserName())

        setupSpeedDialView()
        logoutUser()
    }

    private fun logoutUser() {
        val logout = nav_view?.getHeaderView(0)?.findViewById<ImageView>(R.id.ivLogout)
        logout?.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val languagesActivityViewModel: LanguagesActivityViewModel by viewModels()
                languagesActivityViewModel.deleteAllSavedProgrammingLanguagesOfUser()
                userManager.logout()
                withContext(Dispatchers.Main) {
                    val intent = Intent(this@LanguagesActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun setupSpeedDialView() {
        val speedDialView: SpeedDialView = findViewById(R.id.speedDial)

        speedDialView.setOnActionSelectedListener { speedDialActionItem ->
            when (speedDialActionItem.id) {
                R.id.action_slideshow_fragment -> {
                    val direction =
                        HomeViewPagerFragmentDirections.actionViewPagerFragmentToSlideshowFragment()
                    navController.navigate(direction)
                    false // true to keep the Speed Dial open
                }
                R.id.action_dogo -> {
                    Toast.makeText(this, "Dogo clicked", Toast.LENGTH_LONG).show()
                    false // true to keep the Speed Dial open
                }
                else -> {
                    false
                }
            }
        }

        speedDialView.inflate(R.menu.menu_speed_dial)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun rxJava2Tutorials() {

        val BASE_URL = "https://api.github.com/"

        var myCompositeDisposable: CompositeDisposable? = null

        val requestInterface = Retrofit.Builder()

//Set the API’s base URL//

            .baseUrl(BASE_URL)

//Specify the converter factory to use for serialization and deserialization//

            .addConverterFactory(GsonConverterFactory.create())

//Add a call adapter factory to support RxJava return types//

            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

//Build the Retrofit instance//

            .build().create(GithubRepositoryApi::class.java)

//Add all RxJava disposables to a CompositeDisposable//

        myCompositeDisposable = CompositeDisposable()
        myCompositeDisposable.add(
            requestInterface.searchGithubRepositoryWithRxJava2("java", 1, 15)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse)
        )


        val animalsObservable =
            Observable.just("Ant", "Bee", "Cat", "Dog", "Fox")

        val animalObserver= getAnimalsObserver()

        animalsObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { name -> name.toUpperCase() }
            .subscribe(animalObserver)


        val d = Observable.just(1, 2, 3)
            .map { i: Int -> i * i }
            .map { i: Int -> i * i }
            .filter { i: Int -> i > 10 }
            .subscribe { x: Int? ->
                Log.d(ContentValues.TAG, "" + x)
                println(x) }
    }

    private fun handleResponse(repositoryResponseApi: RepositoryResponseApi) {

        Log.d(ContentValues.TAG, "" + repositoryResponseApi.items.size)
        //adapter.submitData(repositoryResponseApi.items.map)
//        myRetroCryptoArrayList = ArrayList(cryptoList)
//        myAdapter = MyAdapter(myRetroCryptoArrayList!!, this)
//
//        //Set the adapter//
//
//        cryptocurrency_list.adapter = myAdapter
    }

    private fun getAnimalsObserver(): Observer<String?> {
        return object : Observer<String?> {

            override fun onNext(s: String) {
                Log.d(ContentValues.TAG, "Name: $s")
            }

            override fun onError(e: Throwable) {
                Log.e(
                    ContentValues.TAG,
                    "onError: " + e.message
                )
            }

            override fun onComplete() {
                Log.d(
                    ContentValues.TAG,
                    "All items are emitted!"
                )
            }

            /**
             * Provides the Observer with the means of cancelling (disposing) the
             * connection (channel) with the Observable in both
             * synchronous (from within [.onNext]) and asynchronous manner.
             * @param d the Disposable instance whose [Disposable.dispose] can
             * be called anytime to cancel the connection
             * @since 2.0
             */
            override fun onSubscribe(d: Disposable) {
                Log.d(ContentValues.TAG, "onSubscribe")
            }
        }
    }





}