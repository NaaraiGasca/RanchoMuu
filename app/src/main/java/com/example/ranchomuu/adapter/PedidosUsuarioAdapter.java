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
import com.pascmsx.tienda.model.PedidosUsuario;

public class PedidosUsuarioAdapter extends FirestoreRecyclerAdapter<PedidosUsuario, PedidosUsuarioAdapter.ViewHolder> {
    private FirebaseFirestore mfirestore = FirebaseFirestore.getInstance();
    Activity activity;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PedidosUsuarioAdapter(@NonNull FirestoreRecyclerOptions<PedidosUsuario> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PedidosUsuario model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.reportePedido.setText(model.getReportePedido());
        holder.totalPedido.setText(model.getTotalPedido());

        holder.btnCancelarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelarPedido(id);
            }
        });
    }

    private void cancelarPedido(String id)
    {
        mfirestore.collection("pedidosAdmin").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Pedido cancelado.", Toast.LENGTH_SHORT).show();
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pedidos_usuario, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, reportePedido, totalPedido;
        Button btnCancelarPedido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reportePedido = itemView.findViewById(R.id.viDescPedidoUsuario);
            totalPedido = itemView.findViewById(R.id.viTotalPedidoUsuario);
            btnCancelarPedido = itemView.findViewById(R.id.btnCancelarPedidoUsuario);
        }
    }
}
