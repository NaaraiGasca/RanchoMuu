package com.example.ranchomuu.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pascmsx.tienda.R;
import com.pascmsx.tienda.model.PedidosAdmin;

public class PedidosAdminAdapter extends FirestoreRecyclerAdapter<PedidosAdmin, PedidosAdminAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PedidosAdminAdapter(@NonNull FirestoreRecyclerOptions<PedidosAdmin> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PedidosAdmin model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.id.setText(model.getId());
        holder.totalPedido.setText(model.getTotalPedido());
        holder.reportePedido.setText(model.getReportePedido());
        holder.cliente.setText(model.getCliente());

        holder.btnSurtirPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                surtirPedido(id);
            }
        });
    }

    private void surtirPedido(String id)
    {
        mFirestore.collection("pedidosAdmin").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Pedido surtido correctamente.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Error al surtir.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pedidos_admin, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, reportePedido, totalPedido, cliente;
        Button btnSurtirPedido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.viIdPedido);
            reportePedido = itemView.findViewById(R.id.viDescPedido);
            totalPedido = itemView.findViewById(R.id.viTotalPedido);
            btnSurtirPedido = itemView.findViewById(R.id.btnPedidoSurtido);
            cliente = itemView.findViewById(R.id.viNombreCliente);
        }
    }
}
