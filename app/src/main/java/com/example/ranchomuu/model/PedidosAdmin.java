package com.example.ranchomuu.model;

public class PedidosAdmin {
    String cliente, id, reportePedido, totalPedido;

    public PedidosAdmin() {
    }

    public PedidosAdmin(String cliente, String id, String reportePedido, String totalPedido) {
        this.cliente = cliente;
        this.id = id;
        this.reportePedido = reportePedido;
        this.totalPedido = totalPedido;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
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
