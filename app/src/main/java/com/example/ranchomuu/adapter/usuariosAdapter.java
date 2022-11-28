package com.example.ranchomuu.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pascmsx.tienda.AltaProductoPedido;
import com.pascmsx.tienda.R;
import com.pascmsx.tienda.model.producto;
import com.squareup.picasso.Picasso;

public class usuariosAdapter extends FirestoreRecyclerAdapter<producto, usuariosAdapter.ViewHolder>
{

    private FirebaseFirestore mfirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public usuariosAdapter(@NonNull FirestoreRecyclerOptions<producto> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull producto model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.nombreProducto.setText(model.getNombreProducto());
        holder.precioProducto.setText(model.getPrecioProducto());
        holder.cantidadProducto.setText(model.getCantidadProducto());
        String photoProducto = model.getPhoto();
        try {
            if (!photoProducto.equals(""))
            {
                Picasso.with(activity.getApplicationContext())
                        .load(photoProducto)
                        .resize(150, 150)
                        .into(holder.photo_Producto);
            }
        }catch (Exception e)
        {
            Log.d("Exception", "error: " + e);
        }

        holder.btnPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, AltaProductoPedido.class);
                i.putExtra("id_producto", id);
                activity.startActivity(i);
            }
        });



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pedidosusuarios, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreProducto, precioProducto, cantidadProducto;
        ImageView  btnPedido, photo_Producto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreProducto = itemView.findViewById(R.id.viNombre2);
            precioProducto = itemView.findViewById(R.id.viPrecio2);
            cantidadProducto = itemView.findViewById(R.id.viCantidad2);
            btnPedido = itemView.findViewById(R.id.btnAgregarCarrito);
            photo_Producto = itemView.findViewById(R.id.btnFotoProductoM);
        }
    }
}

