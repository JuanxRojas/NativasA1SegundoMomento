package com.example.taller1.ui.main.productos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.core.view.LayoutInflaterCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.taller1.R

class ProductoAdapter (
    private val productos: List<Product>
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>(){

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imagen: ImageView = itemView.findViewById(R.id.img_producto)
        val nombre: TextView = itemView.findViewById(R.id.tv_nombre)
        val precio: TextView = itemView.findViewById(R.id.tv_precio)
        val btnAgregar: Button = itemView.findViewById(R.id.btn_agregar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto,parent,false)
        return ProductoViewHolder(view)

    }

    override fun getItemCount(): Int =productos.size

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.imagen.setImageResource(producto.imagenRes)
        holder.nombre.text = producto.nombre
        holder.precio.text = "$${producto.precio}"
        holder.btnAgregar.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "${producto.nombre} agregado al carrito",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}