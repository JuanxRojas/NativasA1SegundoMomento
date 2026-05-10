package com.example.taller1.ui.main.productos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taller1.R


class HomeFragment : Fragment() {

    private val listaProductos = listOf(
        Product(nombre = "Camisa Casual", precio = 29.9, imagenRes = R.drawable.camisa),
        Product(nombre = "Camisa sport", precio = 29.9, imagenRes = R.drawable.camisa),
        Product(nombre = "Jean", precio = 29.9, imagenRes = R.drawable.camisa),
        Product(nombre = "Cargo", precio = 29.9, imagenRes = R.drawable.camisa),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate( R.layout.fragment_home,  container,  false)
        val recyclerView = view.findViewById<RecyclerView>( R.id.recycler_productos)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = ProductoAdapter(listaProductos)
        return view
    }


}