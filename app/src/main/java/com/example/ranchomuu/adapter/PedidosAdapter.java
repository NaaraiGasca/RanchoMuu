package com.example.ranchomuu.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranchomuu.R;
import com.example.ranchomuu.model.Pedidos;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PedidosAdapter extends FirestoreRecyclerAdapter<Pedidos, PedidosAdapter.viewHolder>
{
    private FirebaseFirestore mfirestore = FirebaseFirestore.getInstance();
    Activity activity;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PedidosAdapter(@NonNull FirestoreRecyclerOptions<Pedidos> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Pedidos model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.nombre.setText(model.getNombreProducto());
        holder.cantidad.setText(model.getCantidadProducto());
        holder.precio.setText(model.getPrecioProducto());

        holder.btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                delete(id);
                //Intent i = new Intent(activity, mostrarCarrito.class);
                //activity.startActivity(i);
            }
        });
    }

    private void delete(String id)
    {
        mfirestore.collection("pedidos").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Producto eliminado del carrito correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.carrito, parent, false);
        return new viewHolder(v);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView nombre, precio, cantidad;
        ImageView btnBorrar;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.viNombreCarrito);
            precio = itemView.findViewById(R.id.viPrecioCarrito);
            cantidad = itemView.findViewById(R.id.viCantidadCarrito);
            btnBorrar = itemView.findViewById(R.id.btnBorrarCarrito);
        }
    }
}
