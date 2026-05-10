package com.example.taller1.ui.main.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taller1.R

class CatalogoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalogo, container, false)

        val productos = listOf(
            Product("Camisa Clásica", 49990.0, R.drawable.camisa),
            Product("Zapatos Deportivos", 129990.0, R.drawable.camisa),
            Product("Gorra Premium", 29990.0, R.drawable.camisa),
            Product("Bolso Ejecutivo", 89990.0, R.drawable.camisa)
        )

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerProductos)
        recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        recycler.adapter = ProductoAdapter(productos)

        return view
    }
}