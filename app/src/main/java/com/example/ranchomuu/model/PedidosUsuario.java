package com.example.ranchomuu.model;

public class PedidosUsuario {
    String id, reportePedido, totalPedido;

    public PedidosUsuario() {
    }

    public PedidosUsuario(String id, String reportePedido, String totalPedido) {
        this.id = id;
        this.reportePedido = reportePedido;
        this.totalPedido = totalPedido;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportePedido() {
        return reportePedido;
    }

    public void setReportePedido(String reportePedido) {
        this.reportePedido = reportePedido;
    }

    public String getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(String totalPedido) {
        this.totalPedido = totalPedido;
    }
}
