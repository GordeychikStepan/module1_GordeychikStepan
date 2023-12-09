package com.bignerdranch.android.cinemaapp

import android.app.Activity.RESULT_OK
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class NavigationCollectionsFragment : Fragment(), CollectionAdapter.OnCollectionClickListener {

    private lateinit var collectionAdapter: CollectionAdapter
    private val collections = mutableListOf<Collection>()
    private lateinit var sharedPrefsHelper: SharedPrefsHelper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPrefsHelper = SharedPrefsHelper(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_navigation_collections, container, false)

        // инициализация адаптера коллекций
        collectionAdapter = CollectionAdapter(collections)
        collectionAdapter.setOnCollectionClickListener(this)

        // установка адаптера для RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.collectionsRecyclerView)
        recyclerView.adapter = collectionAdapter

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        // загрузка коллекций из Shared Preferences
        collections.addAll(sharedPrefsHelper.loadCollections())
        collectionAdapter.notifyDataSetChanged()

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false // Нам не нужна функциональность перемещения
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedCollection = collections[position] // Сохраняем удаляемую коллекцию

                collections.removeAt(position)
                collectionAdapter.notifyItemRemoved(position)

                Snackbar.make(viewHolder.itemView, "Коллекция удалена", Snackbar.LENGTH_LONG)
                    .setAction("ОТМЕНИТЬ") {
                        collections.add(position, deletedCollection)
                        collectionAdapter.notifyItemInserted(position)
                    }.addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)

                            if (event != DISMISS_EVENT_ACTION) {
                                collections.remove(deletedCollection)
                                collectionAdapter.notifyItemRemoved(position)
                                sharedPrefsHelper.saveCollections(collections)
                            }
                        }
                    }).show()
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val createCollectionButton = view.findViewById<Button>(R.id.createCollectionButton)
        createCollectionButton.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), createCollectionButton)
            popupMenu.inflate(R.menu.menu_add)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_add -> {

                        val intent = Intent(requireContext(), CreateCollectionScreen::class.java)
                        val options = ActivityOptions.makeCustomAnimation(
                            requireContext(),
                            R.anim.slide_in_right,
                            R.anim.fade_out
                        )
                        startActivityForResult(intent, 1, options.toBundle())
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val name = data?.getStringExtra(CreateCollectionScreen.EXTRA_COLLECTION_NAME)
            val iconId = data?.getIntExtra(CreateCollectionScreen.EXTRA_SELECTED_ICON, R.drawable.ico1)
            if (name != null && iconId != null) {
                val collection = Collection(name, iconId)
                collections.add(collection)
                collectionAdapter.notifyDataSetChanged()
                sharedPrefsHelper.saveCollections(collections)
            }
        }
    }

    override fun onDeleteClicked(collection: Collection) {
        collections.remove(collection)
        collectionAdapter.notifyDataSetChanged()
        sharedPrefsHelper.saveCollections(collections)
    }
}
