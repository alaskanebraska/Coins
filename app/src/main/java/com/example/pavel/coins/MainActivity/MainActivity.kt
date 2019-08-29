package com.example.pavel.coins.MainActivity

import android.arch.lifecycle.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.*
import com.example.pavel.coins.Repository.DataListRepository
import com.example.pavel.coins.Repository.DbRepository
import com.example.pavel.coins.Repository.NetWorkState

import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.RecyclerView
import com.example.pavel.coins.Adapter.MyPAgedListAdapter
import com.example.pavel.coins.Api.IMyApi
import com.example.pavel.coins.Api.RetrofitClient
import com.example.pavel.coins.R
import com.example.pavel.coins.db.NumberDAO
import com.example.pavel.coins.db.NumberRoomDatabase


class MainActivity : AppCompatActivity() {

    //private var presenter: ContractMvp.Presenter? = null
   // private lateinit var list: List<Coin>
    //private lateinit var adapter: RecyclerViewAdapter

    private lateinit var viewModel: MainActivityViewModel
    private var numberRoomDb: NumberRoomDatabase?=null
    val TAG: String = "myLog"
    private lateinit var dbRepository: DbRepository
    private lateinit var numberDAO: NumberDAO
    private lateinit var myAdapter: MyPAgedListAdapter
    private val lifecycle: MainActivity = this

    lateinit var dataListRepository: DataListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        if(AccessToInternet.isOnline(this)){
             viewModel.pagedList.observe(this, Observer {
                 myAdapter.submitList(it)
                 refreshLayout.isRefreshing = false
            })



            viewModel.netWorkState.observe(this, Observer {
                progress_bar_popular.visibility = if (viewModel.listIsEmpty() && it == NetWorkState.LOADING) View.VISIBLE else View.GONE
                txt_error_popular.visibility = if (viewModel.listIsEmpty() && it == NetWorkState.ERROR) View.VISIBLE else View.GONE

                if (!viewModel.listIsEmpty()) {
                    if (it != null && refreshLayout.isRefreshing==false) {
                        myAdapter.setNetworkState(it)
                    }
                }
            })

            refreshLayout.setOnRefreshListener {
                viewModel.refresh()
            }
                 }
        else {
            refreshLayout.isEnabled = false
        viewModel.pagedListFromDb.observe(this, Observer {
            myAdapter.submitList(it)
        })

        Log.i(TAG,"just"+numberRoomDb.toString())
        Log.i(TAG,"all"+numberDAO.getAll().toString())
        }

    }


    fun init(){
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.title = "Coins"

        val retrofit = RetrofitClient.instance
        val api: IMyApi = retrofit.create(IMyApi::class.java)
        numberRoomDb = NumberRoomDatabase.getInstance(this)
        numberDAO = numberRoomDb!!.numberDao()
        dbRepository = DbRepository(numberDAO)
        dataListRepository = DataListRepository(api, numberDAO)

        viewModel = getViewModel()

        edText.setOnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_ENTER){
                viewModel.search(edText.text.toString()).observe(this, Observer {
                    Log.i(TAG,"text"+edText.text.toString().trim())
                    myAdapter.submitList(it)
                    myAdapter.notifyDataSetChanged()
                })
                //view.clearFocus()
                return@setOnKeyListener true
            }
            false
        }

        myAdapter = MyPAgedListAdapter(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = myAdapter

        val myHelper = ItemTouchHelper(itemTouchHelper())
        myHelper.attachToRecyclerView(recyclerView)
    }


    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(dataListRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.item1 -> {
                viewModel.pagedSortingByName.observe(this, Observer {
                    myAdapter.submitList(it)
                    myAdapter.notifyDataSetChanged()
                })
                return true
            }
            R.id.item2 -> {
                viewModel.pagedSortingByPrice.observe(this, Observer {
                    myAdapter.submitList(it)
                    myAdapter.notifyDataSetChanged()
                })
                return true
            }
            R.id.item3 -> {
                viewModel.pagedListFromDb.observe(this, Observer {
                    myAdapter.submitList(it)
                    myAdapter.notifyDataSetChanged()
                })
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun itemTouchHelper():ItemTouchHelper.SimpleCallback{
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction==ItemTouchHelper.LEFT){
                    Log.i(TAG,"LEFT"+viewHolder.adapterPosition)
                    viewModel.addToFavourite(viewHolder.adapterPosition).observe(lifecycle, Observer {
                        myAdapter.submitList(it)
                        myAdapter.notifyDataSetChanged()
                    })
                }
                else
                    Log.i(TAG,"RIGHT"+viewHolder.adapterPosition)
                viewModel.deleteFromFavourite(viewHolder.adapterPosition).observe(lifecycle, Observer {
                    myAdapter.submitList(it)
                    myAdapter.notifyDataSetChanged()
                })
            }
        }
        return itemTouchHelperCallback
    }
}
